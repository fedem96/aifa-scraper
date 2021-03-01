package com.fedem96.controller;

import com.fedem96.dao.LastUpdateDao;
import com.fedem96.model.*;
import com.fedem96.requester.RequestFactory;
import org.apache.commons.text.StringEscapeUtils;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.selection.Selection;
import tech.tablesaw.table.TableSlice;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            "sm_field_codice_atc", "sm_field_codice_ditta"
    };
    private static final String[] PACKAGING_COLUMNS = {"sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
            "sm_field_codice_farmaco"};


    public void scrapeURL(String url, int start, int rowsPerPage, int maxRows) throws IOException {
        Set<String> columns = new HashSet<>();
        columns.addAll(Arrays.asList(ACTIVE_INGREDIENT_COLUMNS));
        columns.addAll(Arrays.asList(COMPANY_COLUMNS));
        columns.addAll(Arrays.asList(DRUG_COLUMNS));
        columns.addAll(Arrays.asList(PACKAGING_COLUMNS));
        columns.add(LAST_UPDATE_DATE_TIME_COLUMN);
        for (int pageStart = start; pageStart < maxRows; pageStart+=rowsPerPage) {
            String csvText = RequestFactory.scrapeFromUrl(url).columns(columns).start(pageStart).rows(rowsPerPage).send();
            processScraped(csvText);
        }
    }

    public void scrapeFile(String file, int start, int rowsPerPage, int maxRows) throws IOException {
        for (int pageStart = start; pageStart < maxRows; pageStart+=rowsPerPage) {
            String csvText = RequestFactory.scrapeFromFile(file).start(pageStart).rows(rowsPerPage).send();
            processScraped(csvText);
        }
    }

    private void processScraped(String csvText) throws IOException {
        Table table = textToTable(csvText, DATE_TIME_PATTERN);
        table = dropOldRecords(table, LAST_UPDATE_DATE_TIME_COLUMN);

        Table tabDrugs = table.select(DRUG_COLUMNS).dropDuplicateRows(); // TODO: handle missing values
        Table tabCompanies = table.select(COMPANY_COLUMNS).dropDuplicateRows();
        Table tabActiveIngredients = table.select(ACTIVE_INGREDIENT_COLUMNS).dropDuplicateRows();
        Table tabPackagings = table.select(PACKAGING_COLUMNS).dropDuplicateRows();

        for (String colName : new String[]{"sm_field_link_fi", "sm_field_link_rcp"}) {
            StringColumn newCol = tabDrugs.stringColumn(colName).map(StringEscapeUtils::unescapeHtml4);
            tabDrugs.removeColumns(colName);
            tabDrugs.addColumns(newCol);
        }

        int maxPerTransaction = 1000;
        Map<Long, Company> mapCompanies = addCompanies(tabCompanies, maxPerTransaction);
        Map<String, ActiveIngredient> mapActiveIngredients = addActiveIngredients(tabActiveIngredients, maxPerTransaction);
        Map<Long, Drug> drugsMap = addDrugs(tabDrugs, mapCompanies, mapActiveIngredients, maxPerTransaction);
        addPackagings(tabPackagings, drugsMap, maxPerTransaction);
    }

    private Table textToTable(String csvText, String dateTimePattern) throws IOException {
        return Table.read().csv(CsvReadOptions.builder(new StringReader(csvText)).header(true).dateTimeFormat(DateTimeFormatter.ofPattern(dateTimePattern)));
    }

    private Table dropOldRecords(Table table, String dateColumn){
        DateTimeColumn date = table.dateTimeColumn(dateColumn);
        LocalDate lastUpdate = getLastUpdateDate();
        return table.where(date.isOnOrAfter(lastUpdate));
    }

    // TODO: no transactional in this class
    @Transactional
    private LocalDate getLastUpdateDate(){
        return lastUpdateDao.findSingleton().getLastUpdateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Transactional
    public void setLastUpdate(){
        LastUpdate lu = lastUpdateDao.findSingleton();
        lu.setLastUpdateDate(new Date());
        lastUpdateDao.save(lu);
    }


    public Map<Long, Company> addCompanies(Table tab, int maxPerTransaction){
        Map<Long, Company> companiesMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<Long, Company> addedCompaniesMap = companyController.addCompanies(ts);
            companiesMap.putAll(addedCompaniesMap);
        }
        return companiesMap;
    }

    public Map<Long, Drug> addDrugs(Table tab, Map<Long, Company> mapCompanies, Map<String, ActiveIngredient> mapActiveIngredients, int maxPerTransaction){
        Map<Long, Drug> drugsMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<Long, Drug> addedDrugsMap = drugController.addDrugs(ts, mapCompanies, mapActiveIngredients);
            drugsMap.putAll(addedDrugsMap);
        }
        return drugsMap;
    }

    public Map<String, Packaging> addPackagings(Table tab, Map<Long, Drug> mapDrugs, int maxPerTransaction){
        Map<String, Packaging> packagingsMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<String, Packaging> addedPackagingsMap = packagingController.addPackagings(ts, mapDrugs);
            packagingsMap.putAll(addedPackagingsMap);
        }
        return packagingsMap;
    }

    public Map<String, ActiveIngredient> addActiveIngredients(Table tab, int maxPerTransaction){
        Map<String, ActiveIngredient> activeIngredientsMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<String, ActiveIngredient> addedActiveIngredientsMap = activeIngredientController.addActiveIngredients(ts);
            activeIngredientsMap.putAll(addedActiveIngredientsMap);
        }
        return activeIngredientsMap;
    }
}
