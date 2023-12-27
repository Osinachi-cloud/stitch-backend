package com.stitch.payment.service.impl;

import com.stitch.currency.model.enums.Currency;
import com.stitch.currency.service.ExchangeRateService;
import com.stitch.payment.exception.TransVolumeException;
import com.stitch.payment.model.dto.DailyLimitRequest;
import com.stitch.payment.model.dto.TierConfigDto;
import com.stitch.payment.model.entity.TierConfig;
import com.stitch.payment.model.entity.Transaction;
import com.stitch.payment.model.enums.TierActionType;
import com.stitch.payment.model.enums.TransactionStatus;
import com.stitch.payment.model.enums.TransactionType;
import com.stitch.payment.repository.TierConfigRepository;
import com.stitch.payment.repository.TransactionRepository;
import com.stitch.payment.service.TierConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TierConfigServiceImpl implements TierConfigService {

    private final TierConfigRepository tierConfigRepository;

    private final TransactionRepository transactionRepository;

    private final ExchangeRateService exchangeRateService;


    public TierConfigDto setUpTier(TierConfigDto request){
        TierConfig tierConfig = tierConfigRepository.findByTierIgnoreCaseAndActionTypeAndCurrency(request.getTier(), request.getActionType(), request.getCurrency()).orElseGet(TierConfig::new);
        tierConfig.setTier(request.getTier());
        tierConfig.setAmount(request.getAmount());
        tierConfig.setCurrency(request.getCurrency());
        tierConfig.setActionType(request.getActionType());
        tierConfigRepository.saveAndFlush(tierConfig);
        return request;
    }

    public List<TierConfigDto> fetchAllTierConfig(){
        return tierConfigRepository.findAll().stream().map(item -> TierConfigDto.builder()
                .tier(item.getTier().toUpperCase())
                .currency(item.getCurrency())
                .amount(item.getAmount())
                .actionType(item.getActionType())
                .build())
            .collect(Collectors.toList());
    }

    public BigDecimal fetchTierConfig(String tier, TierActionType actionType, Currency currency){
        Optional<TierConfig> tierConfig = tierConfigRepository.findByTierIgnoreCaseAndActionTypeAndCurrency(tier, actionType, currency);
        if (tierConfig.isPresent()) return tierConfig.get().getAmount();
        return new BigDecimal("10000");
    }

    public void cumulativeDailyLimits(DailyLimitRequest dailyLimitRequest){
        List<String> exemptedCategory = List.of("DATA", "INTERNET", "CABLE-TV");

        if(TierActionType.W.equals(dailyLimitRequest.getActionType())){
            if(dailyLimitRequest.getCategory() != null &&
                exemptedCategory.contains(dailyLimitRequest.getCategory().toUpperCase())) return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();
        Instant startDateTime = Timestamp.valueOf(startDate.atStartOfDay()).toInstant();
        Instant endDateTime = Timestamp.valueOf(endDate.atTime(LocalTime.MAX)).toInstant();

        List<Transaction> transactions;
        if(TierActionType.F.equals(dailyLimitRequest.getActionType())){
            transactions = transactionRepository.findAllByCustomerIdAndTransactionTypeAndStatusInAndSrcCurrencyAndDateCreatedBetween(dailyLimitRequest.getCustomerId(),
                TransactionType.FUND_WALLET, Arrays.asList(TransactionStatus.P, TransactionStatus.C), dailyLimitRequest.getCurrency(), startDateTime, endDateTime);
        } else {
            transactions = transactionRepository.findAllByCustomerIdAndTransactionTypeAndStatusInAndSrcCurrencyAndProductCategoryNotInIgnoreCaseAndDateCreatedBetween(dailyLimitRequest.getCustomerId(),
                TransactionType.PAY_BILL, Arrays.asList(TransactionStatus.P, TransactionStatus.C), dailyLimitRequest.getCurrency(), exemptedCategory, startDateTime, endDateTime);
        }

        BigDecimal volume = BigDecimal.ZERO;

        Currency currency = dailyLimitRequest.getCurrency();
        BigDecimal loadAmount = dailyLimitRequest.getLoadAmount();

        if(!currency.equals(Currency.NGN)){
            for(Transaction value: transactions){
                if(value.getSrcAmount() != null){
                    volume = volume.add(value.getSrcAmount());
                }
            }
        } else {
            for(Transaction value: transactions){
                if(value.getAmount() != null){
                    volume = volume.add(value.getAmount());
                }
            }
        }


        BigDecimal tierAmount = fetchTierConfig(dailyLimitRequest.getTier(), dailyLimitRequest.getActionType(), currency);

        if(volume.add(loadAmount).compareTo(tierAmount) > 0){
            BigDecimal allowedAmount = tierAmount.subtract(volume);
            String action = TierActionType.F.equals(dailyLimitRequest.getActionType()) ? "funding" : "transaction";
            throw new TransVolumeException(String.format("Daily %s Limit Exceeded " +
                "You've reached your daily %s limit. " +
                "Remaining: %s%s. For higher limits, contact customer support.", action, action, currency, allowedAmount));
        }
    }

}
