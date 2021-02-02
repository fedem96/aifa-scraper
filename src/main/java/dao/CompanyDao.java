package dao;

import model.Company;

import javax.persistence.NoResultException;
import java.util.List;

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

    public List<Company> search(String company) { // TODO: handle max results / pages
        return entityManager.createQuery("FROM Company " +
                "WHERE description LIKE :company")
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
