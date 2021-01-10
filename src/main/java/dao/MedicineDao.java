package dao;

import model.Medicine;

import javax.persistence.NoResultException;
import java.util.List;

public class MedicineDao extends BaseDao<Medicine> {
    public MedicineDao() {
        super(Medicine.class);
    }

    public List<Medicine> search(String medicine) { // TODO: handle max results / pages
        return entityManager.createQuery("FROM Medicine " +
                "WHERE description LIKE :medicine")
                .setParameter("medicine", "%" + medicine + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public Medicine findByCode(long code) { // TODO: speedup
        try {
            return (Medicine) entityManager.createQuery("from Medicine where code=:code").setParameter("code", code).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
