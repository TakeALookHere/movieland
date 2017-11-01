package com.miskevich.movieland.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.miskevich.movieland.entity.Country;
import com.miskevich.movieland.entity.Genre;
import com.miskevich.movieland.entity.Review;
import com.miskevich.movieland.web.json.LocalDateToStringSerializer;

import java.time.LocalDate;
import java.util.List;

public class MovieDto {

    private int id;
    private String nameRussian;
    private String nameNative;
    @JsonSerialize(using = LocalDateToStringSerializer.class)
    private LocalDate releasedDate;
    private List<Country> countries;
    private String plot;
    private double rating;
    private double price;
    private String picturePath;
    private List<Genre> genres;
    private List<Review> reviews;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameRussian() {
        return nameRussian;
    }

    public void setNameRussian(String nameRussian) {
        this.nameRussian = nameRussian;
    }

    public String getNameNative() {
        return nameNative;
    }

    public void setNameNative(String nameNative) {
        this.nameNative = nameNative;
    }

    public LocalDate getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(LocalDate releasedDate) {
        this.releasedDate = releasedDate;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "id=" + id +
                ", nameRussian='" + nameRussian + '\'' +
                ", nameNative='" + nameNative + '\'' +
                ", releasedDate=" + releasedDate +
                ", countries=" + countries +
                ", plot='" + plot + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", picturePath='" + picturePath + '\'' +
                ", genres=" + genres +
                ", reviews=" + reviews +
                '}';
    }
}
