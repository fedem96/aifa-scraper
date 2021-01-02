package controller;

import dao.PrincipleDao;
import model.ModelFactory;
import model.Principle;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class PrincipleController {

    @Inject
    PrincipleDao principleDao;

    @Transactional
    public void addPrinciples(Table tab){
        for (Row row: tab){
            String atc = row.getString("sm_field_codice_atc");
            Principle principle = principleDao.findByAtc(atc);
            if(principle == null){
                principle = ModelFactory.principle();
            }
            principle.setAtc(atc);
            principle.setDescription(row.getString("sm_field_descrizione_atc"));
            principleDao.save(principle);
        }
    }
}
