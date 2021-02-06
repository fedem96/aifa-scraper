package com.fedem96.model;

import java.util.UUID;

public class ModelFactory {

    private ModelFactory(){
    }

    public static Company company() {
        return new Company(UUID.randomUUID().toString());
    }

    public static Medicine medicine() {
        return new Medicine(UUID.randomUUID().toString());
    }

    public static Packaging packaging() {
        return new Packaging(UUID.randomUUID().toString());
    }

    public static Principle principle() {
        return new Principle(UUID.randomUUID().toString());
    }

    public static LastUpdate lastUpdate() {
        return new LastUpdate(UUID.randomUUID().toString());
    }
}
