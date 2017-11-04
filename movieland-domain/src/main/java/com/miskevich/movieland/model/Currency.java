package com.miskevich.movieland.model;

public enum Currency {
    USD("USD"),
    EUR("EUR");

    private String value;

    Currency(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Currency getCurrencyByName(String name) {
        for (Currency currency : values()) {
            if (currency.getValue().equalsIgnoreCase(name)) {
                return currency;
            }
        }
        String message = "Illegal CCY code! Please use USD/EUR";
        throw new IllegalArgumentException(message);
    }
}
