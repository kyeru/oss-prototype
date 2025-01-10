package com.oss_prototype.models;

public enum ModelName {
    MalignantPackageIdentifier("MPI"),
    MockUpModel("mockup");

    private final String name;

    ModelName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
