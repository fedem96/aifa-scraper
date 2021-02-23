package com.fedem96.model;

import java.util.UUID;

public class ModelFactory {

    private ModelFactory(){
    }

    public static Company company() {
        return new Company(UUID.randomUUID().toString());
    }

    public static Drug drug() {
        return new Drug(UUID.randomUUID().toString());
    }

    public static Packaging packaging() {
        return new Packaging(UUID.randomUUID().toString());
    }

    public static ActiveIngredient activeIngredient() {
        return new ActiveIngredient(UUID.randomUUID().toString());
    }

    public static LastUpdate lastUpdate() {
        return new LastUpdate(UUID.randomUUID().toString());
    }
}
