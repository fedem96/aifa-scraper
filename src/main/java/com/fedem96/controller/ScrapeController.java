package com.fedem96.controller;

import com.fedem96.dao.LastUpdateDao;
import com.fedem96.model.*;
import com.fedem96.requester.RequestFactory;
import tech.tablesaw.api.DateTimeColumn;
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
import java.util.Date;
import java.util.HashMap;
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

    public void scrapeURL(String url, Integer maxRows) throws IOException {
        String csvText = RequestFactory.scrapeFromUrl(url).rows(maxRows).send();
        processScraped(csvText);
    }

    public void scrapeFile(String file, Integer maxRows) throws IOException {
        String csvText = RequestFactory.scrapeFromFile(file).rows(maxRows).send();
        processScraped(csvText);
    }

    private void processScraped(String csvText) throws IOException {
        Table table = textToTable(csvText, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        table = dropOldRecords(table, "ds_last_comment_or_change");

        Table tabMedicines = table.select("sm_field_codice_farmaco", "sm_field_descrizione_farmaco", "sm_field_link_fi", "sm_field_link_rcp",
                "sm_field_codice_atc", "sm_field_codice_ditta", "ds_last_comment_or_change").dropDuplicateRows(); // TODO: handle missing values
        Table tabCompanies = table.select("sm_field_codice_ditta", "sm_field_descrizione_ditta").dropDuplicateRows();
        Table tabPrinciples = table.select("sm_field_codice_atc", "sm_field_descrizione_atc").dropDuplicateRows();
        Table tabPackagings = table.select("sm_field_aic", "sm_field_descrizione_confezione", "sm_field_stato_farmaco",
                "sm_field_codice_farmaco").dropDuplicateRows();

        int maxPerTransaction = 1000;
        Map<Long, Company> mapCompanies = addCompanies(tabCompanies, maxPerTransaction);
        Map<String, Principle> mapPrinciples = addPrinciples(tabPrinciples, maxPerTransaction);
        Map<Long, Medicine> medicinesMap = addMedicines(tabMedicines, mapCompanies, mapPrinciples, maxPerTransaction);
        addPackagings(tabPackagings, medicinesMap, maxPerTransaction);
    }

    private Table textToTable(String csvText, String dateTimePattern) throws IOException {
        return Table.read().csv(CsvReadOptions.builder(new StringReader(csvText)).header(true).dateTimeFormat(DateTimeFormatter.ofPattern(dateTimePattern)));
    }

    private Table dropOldRecords(Table table, String dateColumn){
        DateTimeColumn date = table.dateTimeColumn(dateColumn);
        LocalDate lastUpdate = getLastUpdateDate();
        return table.where(date.isOnOrAfter(lastUpdate));
    }

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

    public Map<Long, Medicine> addMedicines(Table tab, Map<Long, Company> mapCompanies, Map<String, Principle> mapPrinciples, int maxPerTransaction){
        Map<Long, Medicine> medicinesMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<Long, Medicine> addedMedicinesMap = medicineController.addMedicines(ts, mapCompanies, mapPrinciples);
            medicinesMap.putAll(addedMedicinesMap);
        }
        return medicinesMap;
    }

    public Map<Long, Packaging> addPackagings(Table tab, Map<Long, Medicine> mapMedicines, int maxPerTransaction){
        Map<Long, Packaging> packagingsMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<Long, Packaging> addedPackagingsMap = packagingController.addPackagings(ts, mapMedicines);
            packagingsMap.putAll(addedPackagingsMap);
        }
        return packagingsMap;
    }

    public Map<String, Principle> addPrinciples(Table tab, int maxPerTransaction){
        Map<String, Principle> principlesMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<String, Principle> addedPrinciplesMap = principleController.addPrinciples(ts);
            principlesMap.putAll(addedPrinciplesMap);
        }
        return principlesMap;
    }
}
