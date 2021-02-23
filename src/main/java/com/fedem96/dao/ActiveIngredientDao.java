package com.fedem96.dao;

import com.fedem96.model.ActiveIngredient;

import javax.persistence.NoResultException;
import java.util.List;

public class ActiveIngredientDao extends BaseDao<ActiveIngredient> {
    public ActiveIngredientDao() {
        super(ActiveIngredient.class);
    }

    public ActiveIngredient findByAtc(String atc) {
        try {
            return (ActiveIngredient) entityManager.createQuery("from ActiveIngredient where atc=:atc").setParameter("atc", atc).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<ActiveIngredient> getAll() {
        try {
            return entityManager.createQuery("from ActiveIngredient").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<ActiveIngredient> search(String activeIngredient) { // TODO: handle max results / pages
        activeIngredient = activeIngredient.toLowerCase();
        return entityManager.createQuery("FROM ActiveIngredient " +
                "WHERE LOWER(description) LIKE :activeIngredient")
                .setParameter("activeIngredient", "%" + activeIngredient + "%")
                .setMaxResults(10)
                .getResultList(); // TODO: order
    }
}
