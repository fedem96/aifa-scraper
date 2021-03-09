package com.fedem96.dto;

import java.util.List;

public class DrugDto extends BaseDto{

    // This DTO is defined in this way to be compatible with an existing client

    private Long id;
    private String name;
    private String activeIngredient;
    private List<PackagingDto> versions;

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

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public List<PackagingDto> getVersions() {
        return versions;
    }

    public void setVersions(List<PackagingDto> packagings) {
        this.versions = packagings;
    }
}
