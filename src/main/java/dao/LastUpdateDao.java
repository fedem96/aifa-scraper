package dao;

import model.LastUpdate;
import model.Medicine;
import model.ModelFactory;

import javax.persistence.NoResultException;
import java.util.Date;

public class LastUpdateDao extends BaseDao<LastUpdate> {
    public LastUpdateDao() {
        super(LastUpdate.class);
    }

    public LastUpdate findSingleton() {
        try {
            return (LastUpdate) entityManager.createQuery("from LastUpdate").getSingleResult();
        }
        catch (NoResultException nre){
            LastUpdate lu = ModelFactory.lastUpdate();
            lu.setLastUpdateDate(new Date(0));
//            save(lu);
            return lu;
        }
    }
}
