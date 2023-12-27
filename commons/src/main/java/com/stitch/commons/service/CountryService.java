package com.stitch.commons.service;

import com.stitch.commons.model.dto.CountryDto;

import java.util.List;

public interface CountryService {


    List<CountryDto> getActiveCountries();

    String getCurrencyByCountryName(String country);
}
