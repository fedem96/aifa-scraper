package com.fedem96.controller;

import com.fedem96.dao.ActiveIngredientDao;
import com.fedem96.model.ModelFactory;
import com.fedem96.model.ActiveIngredient;
import tech.tablesaw.api.Row;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveIngredientController {

    @Inject
    ActiveIngredientDao activeIngredientDao;

    @Transactional
    public Map<String, ActiveIngredient> addActiveIngredients(Iterable<Row> rows){
        Map<String, ActiveIngredient> map = new HashMap<>();
        for (Row row: rows){
            String atc = row.getString("sm_field_codice_atc");
            ActiveIngredient activeIngredient = activeIngredientDao.findByAtc(atc);
            if(activeIngredient == null){
                activeIngredient = ModelFactory.activeIngredient();
            }
            activeIngredient.setAtc(atc);
            activeIngredient.setDescription(row.getString("sm_field_descrizione_atc"));
            activeIngredientDao.save(activeIngredient);
            map.put(atc, activeIngredient);
        }
        return map;
    }

    public ActiveIngredient getById(Long activeIngredientId) {
        return activeIngredientDao.findById(activeIngredientId);
    }

    public List<ActiveIngredient> getAllActiveIngredients() {
        return activeIngredientDao.getAll();
    }

    public List<ActiveIngredient> search(String activeIngredient){
        return activeIngredientDao.search(activeIngredient);
    }
}
