package com.fedem96.dao;

import com.fedem96.model.Company;

import javax.persistence.NoResultException;
import java.util.List;

public class CompanyDao extends BaseDao<Company> {

    public CompanyDao() {
        super(Company.class);
    }

    public Company findByCode(long code) {
        try {
            return (Company) entityManager.createQuery("from Company where code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Company> search(String company) { // TODO: handle max results / pages
        company = company.toLowerCase();
        return entityManager.createQuery("FROM Company " +
                "WHERE LOWER(description) LIKE :company")
                .setParameter("company", "%" + company + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<Company> getAll() {
        try {
            return entityManager.createQuery("from Company").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
