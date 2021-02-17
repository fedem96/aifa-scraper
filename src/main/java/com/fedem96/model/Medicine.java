package com.fedem96.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "medicines")
public class Medicine extends BaseEntity {

    @Column(unique = true)
    private Long code;
    private String description;
    private String linkFi;
    private String linkRcp;

    @OneToMany(mappedBy = "medicine")
    private List<Packaging> packagings;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Principle principle;

    protected Medicine() {}
    public Medicine(String uuid) {
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

    public Principle getPrinciple() {
        return principle;
    }

    public void setPrinciple(Principle principle) {
        this.principle = principle;
    }

    public List<Packaging> getPackagings() {
        return packagings;
    }

    public void setPackagings(List<Packaging> packagings) {
        this.packagings = packagings;
    }
}
