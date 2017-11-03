package com.miskevich.movieland.model;

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

    public static SortingType getSortingTypeByName(String type) {
        for (SortingType sortingType : values()) {
            if (sortingType.sortingType.equalsIgnoreCase(type)) {
                return sortingType;
            }
        }
        String message = "Incorrect sorting type in request parameters. Use DESC for ratings or ASC/DESC for price";
        throw new IllegalArgumentException(message);
    }


}
