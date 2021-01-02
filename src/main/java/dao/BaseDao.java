package dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseDao<T> implements Serializable {

    private final Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseDao( Class<T> type ) {
        this.type = type;
    }

    public T findById(Long id ) {
        return entityManager.find( type, id );
    }

    public void save( T entity ) {
        entityManager.persist( entity );
    }

    public void update( T entity ) {
        entityManager.merge( entity );
    }

    public void delete( T entity ) {
        entityManager.remove( entity );
    }

}
