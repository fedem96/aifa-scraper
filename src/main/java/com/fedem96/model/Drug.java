package com.fedem96.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "drugs")
public class Drug extends BaseEntity {

    @Column(unique = true)
    private Long code;
    private String description;

    @Column(length = 255)
    private String linkFi;

    @Column(length = 255)
    private String linkRcp;

    @OneToMany(mappedBy = "drug")
    private List<Packaging> packagings;

    @ManyToOne
    private Company company;

    @ManyToOne
    private ActiveIngredient activeIngredient;

    protected Drug() {}
    public Drug(String uuid) {
        super(uuid);
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkFi() {
        return linkFi;
    }

    public void setLinkFi(String linkFi) {
        this.linkFi = linkFi;
    }

    public String getLinkRcp() {
        return linkRcp;
    }

    public void setLinkRcp(String linkRcp) {
        this.linkRcp = linkRcp;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ActiveIngredient getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(ActiveIngredient activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public List<Packaging> getPackagings() {
        return packagings;
    }

    public void setPackagings(List<Packaging> packagings) {
        this.packagings = packagings;
    }
}
