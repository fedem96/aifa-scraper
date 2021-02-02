package model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "packagings")
public class Packaging extends BaseEntity {

    Long aic;
    String description;
    String state;

    @ManyToOne
    private Medicine medicine;

    protected Packaging() {}
    public Packaging(String uuid) {
        super(uuid);
    }

    public Long getAic() {
        return aic;
    }

    public void setAic(Long aic) {
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

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
}
