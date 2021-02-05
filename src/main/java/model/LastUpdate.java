package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "lastupdates")
public class LastUpdate extends BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate;

    protected LastUpdate(){}

    public LastUpdate(String uuid){
        super(uuid);
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
