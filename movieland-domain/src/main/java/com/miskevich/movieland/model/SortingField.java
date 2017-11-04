package com.miskevich.movieland.model;

public enum SortingField {

    RATING("RATING"),
    PRICE("PRICE"),
    NAME_RUSSIAN("NAME_RUSSIAN");

    private String value;

    SortingField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortingField getSortingFieldByName(String name) {
        for (SortingField sortingField : values()) {
            if (sortingField.value.equalsIgnoreCase(name)) {
                return sortingField;
            }
        }
        String message = "No sorting achievable by field: " + name + "! Please use: RATING/PRICE/NAME_RUSSIAN";
        throw new IllegalArgumentException(message);
    }
}
