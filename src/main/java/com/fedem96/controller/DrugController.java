package com.fedem96.controller;

import com.fedem96.dao.DrugDao;
import com.fedem96.dto.DrugDto;
import com.fedem96.mapper.DrugMapper;
import com.fedem96.model.Company;
import com.fedem96.model.Drug;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.ActiveIngredient;
import tech.tablesaw.api.Row;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
            ActiveIngredient rowActiveIngredient = mapActiveIngredients.get(row.getString("sm_field_codice_atc"));
            if(drug == null){   // drug not already in DB
                drug = ModelFactory.drug();
                drug.setActiveIngredient(rowActiveIngredient);
            } else {            // drug already in DB
                // in data obtained from aifa services, the active ingredient can be different between packagings of the same drug
                String oldATC = drug.getActiveIngredient().getAtc();
                if(mustUpdateATC(oldATC, rowActiveIngredient.getAtc())) // decide if ATC must be updated
                    drug.setActiveIngredient(rowActiveIngredient);
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

    private boolean validATC(String atc){
        return Pattern.matches("^[A-DG-HJL-NPR-SV]((\\d)(\\d([A-Z]([A-Z](\\d){0,2})?)?)?)?$", atc);
    }

    private boolean mustUpdateATC(String oldATC, String newATC){ // true if newATC is longer than oldATC
        if(oldATC == null || oldATC.equals(""))
            return true;
        if(newATC == null || newATC.equals(""))
            return false;

        if(!validATC(newATC)){
            System.err.println("WARNING: update rejected due to invalid ATC '" + newATC + "'");
            return false;
        }

        if(!validATC(oldATC)){ // old ATC can be invalid because no check is done on the first insert
            System.err.println("WARNING: replaced invalid ATC '" + newATC + "' with '" + newATC + "'");
            return true;
        }

        if(oldATC.length() < newATC.length()) { // if new ATC is longer than the old one
            if(!newATC.startsWith(oldATC))
                System.err.println("WARNING: found a longer ATC, but not more specific than the saved one. Old: '" + oldATC + "'. New: '" + newATC + "'");
            return true;
        }

        if(newATC.length() < oldATC.length() && !oldATC.startsWith(newATC))
            System.err.println("WARNING: found a shorter ATC, but the saved one is not more specific. Old: '" + oldATC + "'. New: '" + newATC + "'");

        return false;
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
