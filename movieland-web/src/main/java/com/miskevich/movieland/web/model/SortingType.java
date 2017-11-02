package com.miskevich.movieland.web.model;

public enum SortingType {

    DESC("desc"),
    ASC("asc");

    private String sortingType;

    public String getSortingType() {
        return sortingType;
    }

    SortingType(String sortingType) {
        this.sortingType = sortingType;
    }
}
