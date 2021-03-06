package com.fedem96.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "activeIngredients")
public class ActiveIngredient extends BaseEntity {

    @Column(unique = true)
    private String atc;
    private String description;

    protected ActiveIngredient() {}
    public ActiveIngredient(String uuid) {
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
