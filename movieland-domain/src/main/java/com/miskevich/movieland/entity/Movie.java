package com.miskevich.movieland.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.miskevich.movieland.util.LocalDateToStringSerializer;

import java.time.LocalDate;
import java.util.List;

public class Movie {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (Double.compare(movie.rating, rating) != 0) return false;
        if (Double.compare(movie.price, price) != 0) return false;
        if (nameRussian != null ? !nameRussian.equals(movie.nameRussian) : movie.nameRussian != null) return false;
        if (nameNative != null ? !nameNative.equals(movie.nameNative) : movie.nameNative != null) return false;
        if (releasedDate != null ? !releasedDate.equals(movie.releasedDate) : movie.releasedDate != null) return false;
        if (countries != null ? !countries.equals(movie.countries) : movie.countries != null) return false;
        if (plot != null ? !plot.equals(movie.plot) : movie.plot != null) return false;
        if (picturePath != null ? !picturePath.equals(movie.picturePath) : movie.picturePath != null) return false;
        if (genres != null ? !genres.equals(movie.genres) : movie.genres != null) return false;
        return reviews != null ? reviews.equals(movie.reviews) : movie.reviews == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (nameRussian != null ? nameRussian.hashCode() : 0);
        result = 31 * result + (nameNative != null ? nameNative.hashCode() : 0);
        result = 31 * result + (releasedDate != null ? releasedDate.hashCode() : 0);
        result = 31 * result + (countries != null ? countries.hashCode() : 0);
        result = 31 * result + (plot != null ? plot.hashCode() : 0);
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (picturePath != null ? picturePath.hashCode() : 0);
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
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
