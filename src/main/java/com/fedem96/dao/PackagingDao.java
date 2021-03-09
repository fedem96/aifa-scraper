package com.fedem96.dao;

import com.fedem96.model.Packaging;

import javax.persistence.NoResultException;

public class PackagingDao extends BaseDao<Packaging> {
    public PackagingDao() {
        super(Packaging.class);
    }

    public Packaging findByAic(String aic) {
        try {
            return (Packaging) entityManager.createQuery("FROM Packaging WHERE aic=:aic").setParameter("aic", aic).getResultList().get(0);
        }
        catch (IndexOutOfBoundsException | NoResultException e){
            return null;
        }
    }
}
