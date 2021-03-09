package com.fedem96.dao;

import com.fedem96.model.ActiveIngredient;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ActiveIngredientDao extends BaseDao<ActiveIngredient> {
    public ActiveIngredientDao() {
        super(ActiveIngredient.class);
    }

    public ActiveIngredient findByAtc(String atc) {
        try {
            return (ActiveIngredient) entityManager.createQuery("FROM ActiveIngredient WHERE atc=:atc").setParameter("atc", atc).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<ActiveIngredient> getAll() {
        try {
            return entityManager.createQuery("FROM ActiveIngredient").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<ActiveIngredient> search(String activeIngredient, Integer firstResult, Integer maxResults) {
        activeIngredient = activeIngredient.toLowerCase();
        Query q = entityManager.createQuery("FROM ActiveIngredient " +
                "WHERE LOWER(description) LIKE :activeIngredient")
                .setParameter("activeIngredient", "%" + activeIngredient + "%");
        if(firstResult != null)
            q.setFirstResult(firstResult);
        if(maxResults != null)
            q.setMaxResults(maxResults);
        return q.getResultList();
    }
}
