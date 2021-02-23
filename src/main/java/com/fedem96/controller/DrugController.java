package com.fedem96.controller;

import com.fedem96.dao.DrugDao;
import com.fedem96.dto.DrugDto;
import com.fedem96.mapper.DrugMapper;
import com.fedem96.model.Company;
import com.fedem96.model.Drug;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.ActiveIngredient;
import tech.tablesaw.api.Row;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrugController {

    @Inject
    private DrugDao drugDao;
    @Inject
    private DrugMapper drugMapper;

    @Transactional
    public Map<Long, Drug> addDrugs(Iterable<Row> rows, Map<Long, Company> mapCompanies, Map<String, ActiveIngredient> mapActiveIngredients){
        Map<Long, Drug> map = new HashMap<>();
        for (Row row: rows){
            long code = row.getInt("sm_field_codice_farmaco");
            Drug drug = drugDao.findByCode(code);
            if(drug == null){
                drug = ModelFactory.drug();
                drug.setActiveIngredient(mapActiveIngredients.get(row.getString("sm_field_codice_atc")));
            } else {
                ActiveIngredient newActiveIngredient = mapActiveIngredients.get(row.getString("sm_field_codice_atc"));
                String oldATC = drug.getActiveIngredient().getAtc();
                if(oldATC == null || oldATC.length() < newActiveIngredient.getAtc().length()) { // if new ATC is more specific
                    drug.setActiveIngredient(newActiveIngredient);
                    if(!oldATC.equals(newActiveIngredient.getAtc().substring(0, oldATC.length())))
                        System.err.println("Longer ATC but not more specific. Old: '" + oldATC + "'. New: '" + newActiveIngredient.getAtc());
                }
            }
            drug.setCode(code);
            drug.setDescription(row.getString("sm_field_descrizione_farmaco"));
            drug.setLinkFi(row.getString("sm_field_link_fi"));
            drug.setLinkRcp(row.getString("sm_field_link_rcp"));
            drug.setCompany(mapCompanies.get((long) row.getInt("sm_field_codice_ditta")));
            drugDao.save(drug);
            map.put(code, drug);
        }
        return map;
    }

    public DrugDto getById(Long drugId) {
        return drugMapper.convert(drugDao.findById(drugId));
    }

    public List<DrugDto> getAllDrugs() {
        return drugMapper.convert(drugDao.getAll());
    }

    public List<DrugDto> searchByDrug(String drug){
        return drugMapper.convert(drugDao.searchByDrug(drug));
    }

    public List<DrugDto> searchByCompany(String company){
        return drugMapper.convert(drugDao.searchByCompany(company));
    }

    public List<DrugDto> searchByActiveIngredient(String activeIngredient){
        return drugMapper.convert(drugDao.searchByActiveIngredient(activeIngredient));
    }

    public List<DrugDto> search(String text){
        return drugMapper.convert(drugDao.search(text));
    }
}
