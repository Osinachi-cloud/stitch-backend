package com.stitch.commons.service.impl;

import com.stitch.commons.exception.CountryNotFoundException;
import com.stitch.commons.model.dto.CountryDto;
import com.stitch.commons.model.entity.Country;
import com.stitch.commons.repository.CountryRepository;
import com.stitch.commons.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Cacheable(value = "countryCache")
    @Override
    public List<CountryDto> getActiveCountries() {
        return countryRepository.findByActiveTrue()
                .stream().map(c -> new CountryDto(c.getName(), c.getCountryCode(), c.getCurrencyCode(), c.isActive()))
                .collect(Collectors.toList());
    }

    @Override
    public String getCurrencyByCountryName(String countryName) {
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new CountryNotFoundException("Country not available: "+countryName));
        return country.getCurrencyCode();
    }
}
