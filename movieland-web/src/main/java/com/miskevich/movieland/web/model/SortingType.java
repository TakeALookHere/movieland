package com.miskevich.movieland.web.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SortingType {

    DESC("desc"),
    ASC("asc");

    private String sortingType;
    private static final Logger LOG = LoggerFactory.getLogger(SortingType.class);

    public String getSortingType() {
        return sortingType;
    }

    SortingType(String sortingType) {
        this.sortingType = sortingType;
    }

//    public static SortingType getTypeByName(String type){
//        for(SortingType sortingType : values()){
//            if(sortingType.sortingType.equalsIgnoreCase(type)){
//                return sortingType;
//            }
//        }
//        String message = "Incorrect sorting type: " + type;
//        LOG.error(message);
//        throw new IllegalArgumentException(message);
//    }
}
