package com.fedem96.controller;

import com.fedem96.dao.DrugDao;
import com.fedem96.dto.DrugDto;
import com.fedem96.mapper.DrugMapper;
import com.fedem96.model.Company;
import com.fedem96.model.Drug;
import com.fedem96.model.ModelFactory;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;
import tech.tablesaw.table.TableSlice;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrugController {

    @Inject
    private DrugDao drugDao;
    @Inject
    private DrugMapper drugMapper;

    public Map<Long, Drug> addDrugs(Table tab, Map<Long, Company> mapCompanies, int maxPerTransaction){
        Map<Long, Drug> drugsMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<Long, Drug> addedDrugsMap = extractDrugs(ts, mapCompanies);
            drugDao.save(addedDrugsMap.values());
            drugsMap.putAll(addedDrugsMap);
        }
        return drugsMap;
    }

    public Map<Long, Drug> extractDrugs(Iterable<Row> rows, Map<Long, Company> mapCompanies){
        Map<Long, Drug> map = new HashMap<>();
        for (Row row: rows){
            long code = row.getInt("sm_field_codice_farmaco");
            Drug drug = drugDao.findByCode(code);
            if(drug == null){
                drug = ModelFactory.drug();
            }
            drug.setCode(code);
            drug.setDescription(row.getString("sm_field_descrizione_farmaco"));
            drug.setLinkFi(row.getString("sm_field_link_fi"));
            drug.setLinkRcp(row.getString("sm_field_link_rcp"));
            drug.setCompany(mapCompanies.get((long) row.getInt("sm_field_codice_ditta")));
//            drugDao.save(drug);
            map.put(code, drug);
        }
        return map;
    }

    public DrugDto getById(Long drugId) {
        return drugMapper.convert(drugDao.findById(drugId));
    }

    public List<DrugDto> getAllDrugs(boolean returnRetired) {
        return drugMapper.convert(drugDao.getAll(), returnRetired);
    }

    public List<DrugDto> searchByDrug(String drug, Integer firstResult, Integer maxResults, boolean returnRetired){
        return drugMapper.convert(drugDao.searchByDrug(drug, firstResult, maxResults), returnRetired);
    }

    public List<DrugDto> searchByCompany(String company, Integer firstResult, Integer maxResults, boolean returnRetired){
        return drugMapper.convert(drugDao.searchByCompany(company, firstResult, maxResults), returnRetired);
    }

    public List<DrugDto> searchByActiveIngredient(String activeIngredient, Integer firstResult, Integer maxResults, boolean returnRetired){
        return drugMapper.convert(drugDao.searchByActiveIngredient(activeIngredient, firstResult, maxResults), returnRetired);
    }

    public List<DrugDto> search(String text, Integer firstResult, Integer maxResults, boolean returnRetired){
        return drugMapper.convert(drugDao.search(text, firstResult, maxResults), returnRetired);
    }
}
