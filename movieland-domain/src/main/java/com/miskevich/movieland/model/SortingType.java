package com.miskevich.movieland.model;

public enum SortingType {

    DESC("DESC"),
    ASC("ASC");

    private String value;

    public String getValue() {
        return value;
    }

    SortingType(String value) {
        this.value = value;
    }

    public static SortingType getSortingTypeByName(String type) {
        for (SortingType sortingType : values()) {
            if (sortingType.value.equalsIgnoreCase(type)) {
                return sortingType;
            }
        }
        String message = "Incorrect sorting type in request parameters! Please use: ASC/DESC";
        throw new IllegalArgumentException(message);
    }


}
