package com.stitch.currency.service.impl;

import com.stitch.currency.model.dto.CurrencyDTO;
import com.stitch.currency.model.dto.CurrencyRequest;
import com.stitch.currency.model.entity.CurrencyDetails;
import com.stitch.currency.repository.CurrencyDetailsRepository;
import com.stitch.currency.service.CurrencyDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CurrencyDetailsServiceImpl implements CurrencyDetailsService {

    private final CurrencyDetailsRepository currencyDetailsRepository;

    public CurrencyDetailsServiceImpl(CurrencyDetailsRepository currencyDetailsRepository) {
        this.currencyDetailsRepository = currencyDetailsRepository;
    }


    @Override
    public CurrencyDTO createCurrencyDetails(CurrencyRequest currencyRequest) {
        CurrencyDetails currencyDetails = this.currencyDetailsRepository.findByCurrencyCode(currencyRequest.getCurrencyCode());
        CurrencyDTO currencyDTO;
        if (currencyDetails != null) {
            currencyDTO = new CurrencyDTO(this.currencyDetailsRepository.saveAndFlush(currencyDetails));
            return currencyDTO;
        } else {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setMessage("Currency does not exist");
            return currencyDTO;
        }
    }


    @Override
    public List<CurrencyDTO> getAllCurrency() {
        List<CurrencyDetails> currencyDetails = currencyDetailsRepository.findAll();

        return currencyDetails.stream()
                .map(CurrencyDTO::new)
                .collect(Collectors.toList());
    }


    @Override
    public CurrencyDTO updateCurrencyMarkup(CurrencyRequest currencyRequest) {
        CurrencyDetails currencyDetails = this.currencyDetailsRepository.findByCurrencyCode(currencyRequest.getCurrencyCode());
        CurrencyDTO currencyDTO;

        if (currencyDetails != null && currencyRequest.getCurrencyMarkUp() != null) {
            currencyDetails.setCurrencyMarkUp(currencyRequest.getCurrencyMarkUp());
            currencyDTO = new CurrencyDTO(this.currencyDetailsRepository.saveAndFlush(currencyDetails));
            currencyDTO.setSuccess(true);
            return currencyDTO;
        } else {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setMessage("Currency does not exist");
            return currencyDTO;
        }

    }

    @Override
    public CurrencyDTO updateCurrencyRate(CurrencyRequest currencyRequest) {
        CurrencyDetails currencyDetails = this.currencyDetailsRepository.findByCurrencyCode(currencyRequest.getCurrencyCode());
        CurrencyDTO currencyDTO;

        if (currencyDetails != null && currencyRequest.getCurrencyRate() != null) {
            currencyDetails.setCurrencyRate(currencyRequest.getCurrencyRate());
            currencyDTO = new CurrencyDTO(this.currencyDetailsRepository.saveAndFlush(currencyDetails));
            currencyDTO.setMessage("Currency does not exist");

            return currencyDTO;
        } else {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setMessage("Currency does not exist");

            return currencyDTO;
        }
    }




}
