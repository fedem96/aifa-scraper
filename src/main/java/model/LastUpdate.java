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
    private Date company;

    @Temporal(TemporalType.TIMESTAMP)
    private Date medicine;

    @Temporal(TemporalType.TIMESTAMP)
    private Date packaging;

    @Temporal(TemporalType.TIMESTAMP)
    private Date principle;

    protected LastUpdate(){}

    public LastUpdate(String uuid){
        super(uuid);
    }

    public Date getCompany() {
        return company;
    }

    public void setCompany(Date company) {
        this.company = company;
    }

    public Date getMedicine() {
        return medicine;
    }

    public void setMedicine(Date medicine) {
        this.medicine = medicine;
    }

    public Date getPackaging() {
        return packaging;
    }

    public void setPackaging(Date packaging) {
        this.packaging = packaging;
    }

    public Date getPrinciple() {
        return principle;
    }

    public void setPrinciple(Date principle) {
        this.principle = principle;
    }
}
