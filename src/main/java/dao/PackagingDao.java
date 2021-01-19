package dao;

import model.Packaging;

import javax.persistence.NoResultException;

public class PackagingDao extends BaseDao<Packaging> {
    public PackagingDao() {
        super(Packaging.class);
    }

    public Packaging findByAic(long aic) { // TODO: speedup
        try {
            return (Packaging) entityManager.createQuery("from Packaging where aic=:aic").setParameter("aic", aic).getResultList().get(0);
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
