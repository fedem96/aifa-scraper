package dao;

import model.Principle;

import javax.persistence.NoResultException;

public class PrincipleDao extends BaseDao<Principle> {
    public PrincipleDao() {
        super(Principle.class);
    }

    public Principle findByAtc(String atc) { // TODO: speedup
        try {
            return (Principle) entityManager.createQuery("from Principle where atc=:atc").setParameter("atc", atc).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
