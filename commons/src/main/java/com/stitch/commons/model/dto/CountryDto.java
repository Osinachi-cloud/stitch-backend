package com.stitch.commons.model.dto;

public class CountryDto {

    private String name;
    private String countryCode;
    private String currencyCode;
    private boolean active;

    public CountryDto(String name, String countryCode, String currencyCode, boolean active) {
        this.name = name;
        this.countryCode = countryCode;
        this.currencyCode = currencyCode;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "CountryDto{" +
                "name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", active=" + active +
                '}';
    }
}
