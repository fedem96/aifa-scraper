package com.fedem96.controller;

import com.fedem96.dao.PackagingDao;
import com.fedem96.model.ActiveIngredient;
import com.fedem96.model.Drug;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.Packaging;
import tech.tablesaw.api.Row;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PackagingController {
    @Inject
    PackagingDao packagingDao;

    @Transactional
    public Map<String, Packaging> addPackagings(Iterable<Row> rows, Map<Long, Drug> drugsMap, Map<String, ActiveIngredient> mapActiveIngredients){
        Map<String, Packaging> map = new HashMap<>();
        for (Row row: rows){
            String aic = "" + row.getInt("sm_field_aic");
            Packaging packaging = packagingDao.findByAic(aic);
            if(packaging == null){
                packaging = ModelFactory.packaging();
            }
            packaging.setAic(aic);
            packaging.setDescription(row.getString("sm_field_descrizione_confezione"));
            packaging.setState(row.getString("sm_field_stato_farmaco"));
            String atc = row.getString("sm_field_codice_atc");
            if(!validATC(atc))
                System.err.println("WARNING: detected invalid ATC '" + atc + "'");
            packaging.setActiveIngredient(mapActiveIngredients.get(atc));
            packaging.setDrug(drugsMap.get((long) row.getInt("sm_field_codice_farmaco")));
            packagingDao.save(packaging);
            map.put(aic, packaging);
        }
        return map;
    }

    private boolean validATC(String atc){
        return Pattern.matches("^[A-DG-HJL-NPR-SV]((\\d)(\\d([A-Z]([A-Z](\\d){0,2})?)?)?)?$", atc);
    }
}
