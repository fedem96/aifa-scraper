package dao;

import model.LastUpdate;

public class LastUpdateDao extends BaseDao<LastUpdate> {
    public LastUpdateDao() {
        super(LastUpdate.class);
    }

    public LastUpdate findSingleton() {
        return findById(1L);
    }
}
