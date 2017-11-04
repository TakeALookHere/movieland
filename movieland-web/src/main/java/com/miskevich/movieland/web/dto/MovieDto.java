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
    private LocalDate yearOfRelease;
    private List<Country> countries;
    private String description;
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

    public LocalDate getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(LocalDate yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        MovieDto movieDto = (MovieDto) o;

        if (id != movieDto.id) return false;
        if (Double.compare(movieDto.rating, rating) != 0) return false;
        if (Double.compare(movieDto.price, price) != 0) return false;
        if (nameRussian != null ? !nameRussian.equals(movieDto.nameRussian) : movieDto.nameRussian != null)
            return false;
        if (nameNative != null ? !nameNative.equals(movieDto.nameNative) : movieDto.nameNative != null) return false;
        if (yearOfRelease != null ? !yearOfRelease.equals(movieDto.yearOfRelease) : movieDto.yearOfRelease != null)
            return false;
        if (countries != null ? !countries.equals(movieDto.countries) : movieDto.countries != null) return false;
        if (description != null ? !description.equals(movieDto.description) : movieDto.description != null)
            return false;
        if (picturePath != null ? !picturePath.equals(movieDto.picturePath) : movieDto.picturePath != null)
            return false;
        if (genres != null ? !genres.equals(movieDto.genres) : movieDto.genres != null) return false;
        return reviews != null ? reviews.equals(movieDto.reviews) : movieDto.reviews == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (nameRussian != null ? nameRussian.hashCode() : 0);
        result = 31 * result + (nameNative != null ? nameNative.hashCode() : 0);
        result = 31 * result + (yearOfRelease != null ? yearOfRelease.hashCode() : 0);
        result = 31 * result + (countries != null ? countries.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
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
        return "MovieDto{" +
                "id=" + id +
                ", nameRussian='" + nameRussian + '\'' +
                ", nameNative='" + nameNative + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", countries=" + countries +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", price=" + price +
                ", picturePath='" + picturePath + '\'' +
                ", genres=" + genres +
                ", reviews=" + reviews +
                '}';
    }
}
