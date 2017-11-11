package com.miskevich.movieland.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Bean;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateDto {

    public RateDto() {
    }

    @JsonProperty(value = "cc")
    private String currencyName;
    private double rate;

    public RateDto(String currencyName, double rate) {
        this.currencyName = currencyName;
        this.rate = rate;
    }

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
