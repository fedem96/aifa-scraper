package com.fedem96.controller;

import com.fedem96.dao.LastUpdateDao;
import com.fedem96.model.Company;
import com.fedem96.model.LastUpdate;
import com.fedem96.model.Medicine;
import com.fedem96.model.Principle;
import com.fedem96.requester.RequestFactory;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class ScrapeController {

    @Inject
    CompanyController companyController;
    @Inject
    MedicineController medicineController;
    @Inject
    PackagingController packagingController;
    @Inject
    PrincipleController principleController;

    @Inject
    LastUpdateDao lastUpdateDao;

    @Transactional
    public void scrapeURL(String url, Integer maxRows) throws IOException {
        String csvText = RequestFactory.scrapeFromUrl(url).rows(maxRows).send();
        processScraped(csvText);
    }

    @Transactional
    public void scrapeFile(String file, Integer maxRows) throws IOException {
        String csvText = RequestFactory.scrapeFromFile(file).rows(maxRows).send();
        processScraped(csvText);
    }

    public void processScraped(String csvText) throws IOException {
        Table table = Table.read().csv(CsvReadOptions.builder(new StringReader(csvText)).header(true).dateTimeFormat(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));

        table = discardOld(table);

        Table tabMedicines = table.select("sm_field_codice_farmaco", "sm_field_descrizione_farmaco", "sm_field_link_fi", "sm_field_link_rcp",
                "sm_field_codice_atc", "sm_field_codice_ditta", "ds_last_comment_or_change").dropDuplicateRows(); // TODO: handle missing values
        Table tabCompanies = table.select("sm_field_codice_ditta", "sm_field_descrizione_ditta").dropDuplicateRows();
        Table tabPrinciples = table.select("sm_field_codice_atc", "sm_field_descrizione_atc").dropDuplicateRows();
        Table tabPackagings = table.select("sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
                "sm_field_codice_farmaco").dropDuplicateRows();

        Map<Long, Company> mapCompanies = companyController.addCompanies(tabCompanies);
        Map<String, Principle> mapPrinciples = principleController.addPrinciples(tabPrinciples);
        Map<Long, Medicine> medicinesMap = medicineController.addMedicines(tabMedicines, mapCompanies, mapPrinciples);
        packagingController.addPackagings(tabPackagings, medicinesMap);
        updated();
    }

    @Transactional
    public LocalDate getLastUpdateDate(){
        return lastUpdateDao.findSingleton().getLastUpdateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Transactional
    public void updated(){
        LastUpdate lu = lastUpdateDao.findSingleton();
        lu.setLastUpdateDate(new Date());
        lastUpdateDao.save(lu);
    }

    public Table discardOld(Table table){
        DateTimeColumn date = table.dateTimeColumn("ds_last_comment_or_change");
        LocalDate lastUpdate = getLastUpdateDate();
        return table.where(date.isOnOrAfter(lastUpdate));
    }
}
