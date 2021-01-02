package model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "principles")
public class Principle extends BaseEntity {

    String atc;
    String description;

    protected Principle() {}
    public Principle(String uuid) {
        super(uuid);
    }

    public String getAtc() {
        return atc;
    }

    public void setAtc(String atc) {
        this.atc = atc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
