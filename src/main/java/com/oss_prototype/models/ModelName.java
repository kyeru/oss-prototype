package com.oss_prototype.models;

public enum ModelName {
    MalignantPackageIdentifier("MPI");

    private final String name;

    ModelName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
