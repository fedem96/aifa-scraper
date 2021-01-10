package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {//TODO: tostring JSON

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( unique = true )
    private String uuid;

    protected BaseEntity() {
    }

    public BaseEntity( String uuid ) {
        if ( uuid == null ) {
            throw new IllegalArgumentException( "uuid cannot be null" );
        }
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + uuid.hashCode();
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( !( obj instanceof BaseEntity ) ) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return uuid.equals( other.getUuid() );
    }

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}