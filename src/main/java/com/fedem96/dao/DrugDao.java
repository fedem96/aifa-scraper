package com.fedem96.dao;

import com.fedem96.model.Drug;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class DrugDao extends BaseDao<Drug> {
    public DrugDao() {
        super(Drug.class);
    }

    public Drug findByCode(long code) {
        try {
            return (Drug) entityManager.createQuery("FROM Drug WHERE code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Drug> getAll() {
        try {
            return entityManager.createQuery("FROM Drug d LEFT JOIN FETCH d.packagings").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Drug> searchByDrug(String drug, Integer firstResult, Integer maxResults) {
        drug = drug.toLowerCase();
        Query q = entityManager.createQuery("FROM Drug d LEFT JOIN FETCH d.packagings " +
                "WHERE LOWER(d.description) LIKE :drug")
                .setParameter("drug", "%" + drug + "%");
        if(firstResult != null)
            q.setFirstResult(firstResult);
        if(maxResults != null)
            q.setMaxResults(maxResults);
        return q.getResultList();
    }

    public List<Drug> searchByCompany(String company, Integer firstResult, Integer maxResults) {
        company = company.toLowerCase();
        Query q = entityManager.createQuery("FROM Drug d LEFT JOIN FETCH d.packagings " +
                "WHERE LOWER(d.company.description) LIKE :company")
                .setParameter("company", "%" + company + "%");
        if(firstResult != null)
            q.setFirstResult(firstResult);
        if(maxResults != null)
            q.setMaxResults(maxResults);
        return q.getResultList();
    }

    public List<Drug> searchByActiveIngredient(String activeIngredient, Integer firstResult, Integer maxResults) {
        activeIngredient = activeIngredient.toLowerCase();
        Query q = entityManager.createQuery("FROM Drug d LEFT JOIN FETCH d.packagings " +
                "WHERE EXISTS (FROM Packaging p WHERE p.drug=d AND LOWER(P.activeIngredient.description) LIKE :activeIngredient)")
                .setParameter("activeIngredient", "%" + activeIngredient + "%");
        if(firstResult != null)
            q.setFirstResult(firstResult);
        if(maxResults != null)
            q.setMaxResults(maxResults);
        return q.getResultList();
    }

    public List<Drug> search(String text, Integer firstResult, Integer maxResults) {
        text = text.toLowerCase();
        Query q = entityManager.createQuery("FROM Drug d LEFT JOIN FETCH d.packagings " +
                "WHERE LOWER(d.description) LIKE :text OR LOWER(d.company.description) LIKE :text OR EXISTS (FROM Packaging p WHERE p.drug=d AND LOWER(P.activeIngredient.description) LIKE :text)")
                .setParameter("text", "%" + text + "%");
        if(firstResult != null)
            q.setFirstResult(firstResult);
        if(maxResults != null)
            q.setMaxResults(maxResults);
        return q.getResultList();
    }
}
