package dao;

import model.Company;

import javax.persistence.NoResultException;

public class CompanyDao extends BaseDao<Company> {

    public CompanyDao() {
        super(Company.class);
    }

    public Company findByCode(long code) { // TODO: speedup
        try {
            return (Company) entityManager.createQuery("from Company where code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
