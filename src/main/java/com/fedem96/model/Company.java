package com.fedem96.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {

    @Column(unique = true)
    Long code;
    String description;

    protected Company() {}
    public Company(String uuid) {
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
}
