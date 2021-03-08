package com.fedem96.controller;

import com.fedem96.dao.LastUpdateDao;
import com.fedem96.model.ActiveIngredient;
import com.fedem96.model.Company;
import com.fedem96.model.Drug;
import com.fedem96.requester.RequestFactory;
import org.apache.commons.text.StringEscapeUtils;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScrapeController {

    @Inject
    CompanyController companyController;
    @Inject
    DrugController drugController;
    @Inject
    PackagingController packagingController;
    @Inject
    ActiveIngredientController activeIngredientController;

    @Inject
    LastUpdateDao lastUpdateDao;

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String LAST_UPDATE_DATE_TIME_COLUMN = "ds_changed";

    private static final String[] ACTIVE_INGREDIENT_COLUMNS = {"sm_field_codice_atc", "sm_field_descrizione_atc"};
    private static final String[] COMPANY_COLUMNS = {"sm_field_codice_ditta", "sm_field_descrizione_ditta"};
    private static final String[] DRUG_COLUMNS = {
            "sm_field_codice_farmaco", "sm_field_descrizione_farmaco", "sm_field_link_fi", "sm_field_link_rcp",
            "sm_field_codice_ditta"
    };
    private static final String[] PACKAGING_COLUMNS = {"sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
            "sm_field_codice_farmaco", "sm_field_codice_atc"};


    public void scrapeURL(String url, int start, int rowsPerPage, int maxRows, Integer year, boolean checkLastUpdate) throws IOException {
        Set<String> columns = new HashSet<>();
        columns.addAll(Arrays.asList(ACTIVE_INGREDIENT_COLUMNS));
        columns.addAll(Arrays.asList(COMPANY_COLUMNS));
        columns.addAll(Arrays.asList(DRUG_COLUMNS));
        columns.addAll(Arrays.asList(PACKAGING_COLUMNS));
        columns.add(LAST_UPDATE_DATE_TIME_COLUMN);
        for (int pageStart = start; pageStart < start+maxRows; pageStart+=rowsPerPage) {
            String csvText = RequestFactory.scrapeFromUrl(url).year(year).columns(columns).start(pageStart).rows(rowsPerPage).send();
            processScraped(csvText, checkLastUpdate);
        }
    }

    public void scrapeFile(String file, int start, int rowsPerPage, int maxRows, boolean checkLastUpdate) throws IOException {
        for (int pageStart = start; pageStart < start+maxRows; pageStart+=rowsPerPage) {
            String csvText = RequestFactory.scrapeFromFile(file).start(pageStart).rows(rowsPerPage).send();
            if(csvText == null) // file has no more rows
                break;
            processScraped(csvText, checkLastUpdate);
        }
    }

    private void processScraped(String csvText, boolean checkLastUpdate) throws IOException {
        Table table = textToTable(csvText, DATE_TIME_PATTERN);

        if(checkLastUpdate)
            table = dropOldRecords(table, LAST_UPDATE_DATE_TIME_COLUMN);

        for (String colName : new String[]{"sm_field_link_fi", "sm_field_link_rcp", "sm_field_descrizione_confezione"}) {
            StringColumn newCol = table.stringColumn(colName).map(StringEscapeUtils::unescapeHtml4);
            table.removeColumns(colName);
            table.addColumns(newCol);
        }

        Table tabDrugs = table.select(DRUG_COLUMNS).dropDuplicateRows(); // TODO: handle missing values
        Table tabCompanies = table.select(COMPANY_COLUMNS).dropDuplicateRows();
        Table tabActiveIngredients = table.select(ACTIVE_INGREDIENT_COLUMNS).dropDuplicateRows();
        Table tabPackagings = table.select(PACKAGING_COLUMNS).dropDuplicateRows();

        int maxPerTransaction = 1000;
        Map<Long, Company> mapCompanies = companyController.addCompanies(tabCompanies, maxPerTransaction);
        Map<String, ActiveIngredient> mapActiveIngredients = activeIngredientController.addActiveIngredients(tabActiveIngredients, maxPerTransaction);
        Map<Long, Drug> drugsMap = drugController.addDrugs(tabDrugs, mapCompanies, maxPerTransaction);
        packagingController.addPackagings(tabPackagings, drugsMap, mapActiveIngredients, maxPerTransaction);
    }

    private Table textToTable(String csvText, String dateTimePattern) throws IOException {
        return Table.read().csv(CsvReadOptions.builder(new StringReader(csvText)).header(true).dateTimeFormat(DateTimeFormatter.ofPattern(dateTimePattern)));
    }

    private Table dropOldRecords(Table table, String dateColumn){
        DateTimeColumn date = table.dateTimeColumn(dateColumn);
        LocalDate lastUpdate = lastUpdateDao.getLastUpdateDate();
        return table.where(date.isOnOrAfter(lastUpdate));
    }

    public void setLastUpdate() {
        lastUpdateDao.setLastUpdate();
    }
}
