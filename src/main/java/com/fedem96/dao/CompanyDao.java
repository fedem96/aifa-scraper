package com.fedem96.dao;

import com.fedem96.model.Company;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class CompanyDao extends BaseDao<Company> {

    public CompanyDao() {
        super(Company.class);
    }

    public Company findByCode(long code) {
        try {
            return (Company) entityManager.createQuery("FROM Company WHERE code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Company> search(String company, Integer firstResult, Integer maxResults) {
        company = company.toLowerCase();
        Query q = entityManager.createQuery("FROM Company " +
                "WHERE LOWER(description) LIKE :company")
                .setParameter("company", "%" + company + "%");
        if(firstResult != null)
            q.setFirstResult(firstResult);
        if(maxResults != null)
            q.setMaxResults(maxResults);
        return q.getResultList();
    }

    public List<Company> getAll() {
        try {
            return entityManager.createQuery("FROM Company").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
