package com.fedem96.dto;

public class PackagingDto extends BaseDto{

    // This DTO is defined in this way to be compatible with an existing client

    private Long id;
    private String name;
    private String versionClass;
    private String type;
    private String description;
    private Float price;
    private String leaflet;
    private Boolean gluten;
    private Boolean lactose;
    private Long company;
    private String companyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionClass() {
        return versionClass;
    }

    public void setVersionClass(String versionClass) {
        this.versionClass = versionClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getLeaflet() {
        return leaflet;
    }

    public void setLeaflet(String leaflet) {
        this.leaflet = leaflet;
    }

    public Boolean getGluten() {
        return gluten;
    }

    public void setGluten(Boolean gluten) {
        this.gluten = gluten;
    }

    public Boolean getLactose() {
        return lactose;
    }

    public void setLactose(Boolean lactose) {
        this.lactose = lactose;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
