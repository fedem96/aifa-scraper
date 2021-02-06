package com.fedem96.dao;

import com.fedem96.model.Principle;

import javax.persistence.NoResultException;
import java.util.List;

public class PrincipleDao extends BaseDao<Principle> {
    public PrincipleDao() {
        super(Principle.class);
    }

    public Principle findByAtc(String atc) { // TODO: speedup
        try {
            System.out.println("fasfasfa");
            return (Principle) entityManager.createQuery("from Principle where atc=:atc").setParameter("atc", atc).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<Principle> search(String principle) { // TODO: handle max results / pages
        return entityManager.createQuery("FROM Principle " +
                "WHERE description LIKE :principle")
                .setParameter("principle", "%" + principle + "%")
                .setMaxResults(10)
                .getResultList(); // TODO: order
    }

    public List<Principle> getAll() {
        try {
            return entityManager.createQuery("from Principle").getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
