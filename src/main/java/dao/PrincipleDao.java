package dao;

import model.Principle;

import javax.persistence.NoResultException;
import java.util.List;

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

    public List<Principle> search(String principle) { // TODO: handle max results / pages
        return entityManager.createQuery("FROM Principle " +
                "WHERE description LIKE :principle")
                .setParameter("principle", "%" + principle + "%")
                .setMaxResults(10)
                .getResultList(); // TODO: order
    }
}
