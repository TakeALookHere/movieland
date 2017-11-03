package com.miskevich.movieland.model;

public class SortPower {

    private SortingType ratingSort;
    private SortingType priceSort;

    public SortingType getRatingSort() {
        return ratingSort;
    }

    public void setRatingSort(SortingType ratingSort) {
        this.ratingSort = ratingSort;
    }

    public SortingType getPriceSort() {
        return priceSort;
    }

    public void setPriceSort(SortingType priceSort) {
        this.priceSort = priceSort;
    }
}