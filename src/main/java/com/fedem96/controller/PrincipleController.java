package com.fedem96.controller;

import com.fedem96.dao.PrincipleDao;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.Principle;
import tech.tablesaw.api.Row;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrincipleController {

    @Inject
    PrincipleDao principleDao;

    @Transactional
    public Map<String, Principle> addPrinciples(Iterable<Row> rows){
        Map<String, Principle> map = new HashMap<>();
        for (Row row: rows){
            String atc = row.getString("sm_field_codice_atc");
            Principle principle = principleDao.findByAtc(atc);
            if(principle == null){
                principle = ModelFactory.principle();
            }
            principle.setAtc(atc);
            principle.setDescription(row.getString("sm_field_descrizione_atc"));
            principleDao.save(principle);
            map.put(atc, principle);
        }
        return map;
    }

    public List<Principle> search(String principle){
        return principleDao.search(principle);
    }


    public Principle getById(Long principleId) {
        return principleDao.findById(principleId);
    }

    public List<Principle> getAllPrinciples() {
        return principleDao.getAll();
    }
}
