package com.fedem96.controller;

import com.fedem96.dao.PackagingDao;
import com.fedem96.model.Drug;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.Packaging;
import tech.tablesaw.api.Row;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

public class PackagingController {
    @Inject
    PackagingDao packagingDao;

    @Transactional
    public Map<String, Packaging> addPackagings(Iterable<Row> rows, Map<Long, Drug> drugsMap){
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
            packaging.setDrug(drugsMap.get((long) row.getInt("sm_field_codice_farmaco")));
            packagingDao.save(packaging);
            map.put(aic, packaging);
        }
        return map;
    }
}
