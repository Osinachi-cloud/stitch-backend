package com.stitch.commons.model.entity;


import lombok.*;

import jakarta.persistence.*;


@Entity
@Table(name = "country")
public class Country extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "is_active")
    private boolean active;

    public Country() {
    }

    public Country(String name, String countryCode, String currencyCode, boolean active) {
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
        return "Country{" +
                "active=" + active +
                ", name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
