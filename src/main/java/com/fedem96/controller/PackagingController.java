package com.fedem96.controller;

import com.fedem96.dao.PackagingDao;
import com.fedem96.model.Medicine;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.Packaging;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Map;

public class PackagingController {
    @Inject
    PackagingDao packagingDao;

    @Transactional  // TODO: change timeout?
    public void addPackagings(Table tab, Map<Long, Medicine> medicinesMap){ // TODO: batch insert
        for (Row row: tab){
            long aic = row.getInt("sm_field_aic");
            Packaging packaging = packagingDao.findByAic(aic);
            if(packaging == null){
                packaging = ModelFactory.packaging();
            }
            packaging.setAic(aic);
            packaging.setDescription(row.getString("sm_field_descrizione_confezione"));
            packaging.setState(row.getString("sm_field_stato_farmaco"));
            packaging.setMedicine(medicinesMap.get((long) row.getInt("sm_field_codice_farmaco")));
            packagingDao.save(packaging);
        }
    }
}