package com.stitch.commons.repository;

import com.stitch.commons.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByActiveTrue();

    Optional<Country> findByName(String countryName);
}
