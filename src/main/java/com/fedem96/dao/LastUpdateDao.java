package com.fedem96.dao;

import com.fedem96.model.LastUpdate;
import com.fedem96.model.ModelFactory;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LastUpdateDao extends BaseDao<LastUpdate> {
    public LastUpdateDao() {
        super(LastUpdate.class);
    }

    public LastUpdate findSingleton() {
        try {
            return (LastUpdate) entityManager.createQuery("FROM LastUpdate").getSingleResult();
        }
        catch (NoResultException nre){
            LastUpdate lu = ModelFactory.lastUpdate();
            lu.setLastUpdateDate(new Date(0));
//            save(lu);
            return lu;
        }
    }

    public LocalDate getLastUpdateDate(){
        return this.findSingleton().getLastUpdateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Transactional
    public void setLastUpdate(){
        LastUpdate lu = this.findSingleton();
        lu.setLastUpdateDate(new Date());
        this.save(lu);
    }



}
