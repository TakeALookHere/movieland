package com.miskevich.movieland.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateDto {

    @JsonProperty(value = "cc")
    private String currencyName;
    private double rate;

    public String getCurrencyName() {
        return currencyName;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "RateDto{" +
                "currencyName='" + currencyName + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
