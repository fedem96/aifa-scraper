package com.fedem96.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "packagings")
public class Packaging extends BaseEntity {

    @Column(unique = true, length = 31)
    private String aic;

    @Column(length = 1023)
    private String description;
    private String state;

    @ManyToOne
    private ActiveIngredient activeIngredient;

    @ManyToOne(optional = false)
    private Drug drug;

    protected Packaging() {}
    public Packaging(String uuid) {
        super(uuid);
    }

    public String getAic() {
        return aic;
    }

    public void setAic(String aic) {
        this.aic = aic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ActiveIngredient getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(ActiveIngredient activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }
}
