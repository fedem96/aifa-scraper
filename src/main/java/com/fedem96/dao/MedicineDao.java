package com.fedem96.dao;

import com.fedem96.model.Medicine;

import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.List;

public class MedicineDao extends BaseDao<Medicine> {
    public MedicineDao() {
        super(Medicine.class);
    }

    public Medicine findByCode(long code) {
        try {
            return (Medicine) entityManager.createQuery("from Medicine where code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Medicine> getAll() {
        try {
            return entityManager.createQuery("from Medicine m").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Medicine> searchByMedicine(String medicine) { // TODO: handle max results / pages
        medicine = medicine.toLowerCase();
        return entityManager.createQuery("FROM Medicine m LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.description) LIKE :medicine")
                .setParameter("medicine", "%" + medicine + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Medicine> searchByCompany(String company) {
        company = company.toLowerCase();
        return entityManager.createQuery("FROM Medicine m LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.company.description) LIKE :company")
                .setParameter("company", "%" + company + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Medicine> searchByPrinciple(String principle) {
        principle = principle.toLowerCase();
        return entityManager.createQuery("FROM Medicine m LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.principle.description) LIKE :principle")
                .setParameter("principle", "%" + principle + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Medicine> search(String text) { // TODO: handle max results / pages
        text = text.toLowerCase();
        return entityManager.createQuery("FROM Medicine m LEFT JOIN FETCH m.packagings " +
                "WHERE LOWER(m.description) LIKE :text OR LOWER(m.company.description) LIKE :text OR LOWER(m.principle.description) LIKE :text")
                .setParameter("text", "%" + text + "%")
                .setMaxResults(10)
                .getResultList();
    }
}
