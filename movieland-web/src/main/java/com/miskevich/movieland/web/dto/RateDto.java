package com.miskevich.movieland.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RateDto {

    private String cc;
    private double rate;

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
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
                "cc='" + cc + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
