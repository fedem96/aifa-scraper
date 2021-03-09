package com.fedem96.controller;

import com.fedem96.dao.ActiveIngredientDao;
import com.fedem96.model.ActiveIngredient;
import com.fedem96.model.ModelFactory;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;
import tech.tablesaw.selection.Selection;
import tech.tablesaw.table.TableSlice;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActiveIngredientController {

    @Inject
    ActiveIngredientDao activeIngredientDao;

    public Map<String, ActiveIngredient> addActiveIngredients(Table tab, int maxPerTransaction){
        Map<String, ActiveIngredient> activeIngredientsMap = new HashMap<>();
        int numRows = tab.rowCount();
        for(int index = 0; index < numRows; index += maxPerTransaction) {
            TableSlice ts = new TableSlice(tab, Selection.withRange(index, Math.min(index+maxPerTransaction, numRows)));
            Map<String, ActiveIngredient> addedActiveIngredientsMap = extractActiveIngredients(ts);
            activeIngredientDao.save(addedActiveIngredientsMap.values());
            activeIngredientsMap.putAll(addedActiveIngredientsMap);
        }
        return activeIngredientsMap;
    }

    public Map<String, ActiveIngredient> extractActiveIngredients(Iterable<Row> rows){
        Map<String, ActiveIngredient> map = new HashMap<>();
        List<ActiveIngredient> activeIngredients = new LinkedList<>();
        for (Row row: rows){
            String atc = row.getString("sm_field_codice_atc");
            activeIngredients.add(activeIngredientDao.findByAtc(atc));
            ActiveIngredient activeIngredient = activeIngredients.get(activeIngredients.size() - 1);
            if(activeIngredient == null){
                activeIngredient = ModelFactory.activeIngredient();
            }
            activeIngredient.setAtc(atc);
            activeIngredient.setDescription(row.getString("sm_field_descrizione_atc"));
//            activeIngredientDao.save(activeIngredient);
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

    public List<ActiveIngredient> search(String activeIngredient, Integer firstResult, Integer maxResults){
        return activeIngredientDao.search(activeIngredient, firstResult, maxResults);
    }
}
