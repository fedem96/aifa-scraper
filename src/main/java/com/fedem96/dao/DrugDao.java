package com.fedem96.dao;

import com.fedem96.model.Drug;

import javax.persistence.NoResultException;
import java.util.List;

public class DrugDao extends BaseDao<Drug> {
    public DrugDao() {
        super(Drug.class);
    }

    public Drug findByCode(long code) {
        try {
            return (Drug) entityManager.createQuery("from Drug where code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Drug> getAll() {
        try {
            return entityManager.createQuery("from Drug d").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Drug> searchByDrug(String drug) { // TODO: handle max results / pages
        drug = drug.toLowerCase();
        return entityManager.createQuery("FROM Drug d LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.description) LIKE :drug")
                .setParameter("drug", "%" + drug + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Drug> searchByCompany(String company) {
        company = company.toLowerCase();
        return entityManager.createQuery("FROM Drug d LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.company.description) LIKE :company")
                .setParameter("company", "%" + company + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Drug> searchByActiveIngredient(String activeIngredient) {
        activeIngredient = activeIngredient.toLowerCase();
        return entityManager.createQuery("FROM Drug d LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.activeIngredient.description) LIKE :activeIngredient")
                .setParameter("activeIngredient", "%" + activeIngredient + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Drug> search(String text) { // TODO: handle max results / pages
        text = text.toLowerCase();
        return entityManager.createQuery("FROM Drug d LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.description) LIKE :text OR LOWER(m.company.description) LIKE :text OR LOWER(m.activeIngredient.description) LIKE :text")
                .setParameter("text", "%" + text + "%")
                .setMaxResults(10)
                .getResultList();
    }
}
