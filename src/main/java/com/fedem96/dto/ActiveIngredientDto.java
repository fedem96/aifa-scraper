package com.fedem96.dto;

public class ActiveIngredientDto extends BaseDto {

    private String atc;
    private String description;

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
