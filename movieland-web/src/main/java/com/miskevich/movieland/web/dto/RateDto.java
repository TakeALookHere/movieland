package com.miskevich.movieland.web.dto;

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

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "RateDto{" +
                "currencyName='" + currencyName + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
