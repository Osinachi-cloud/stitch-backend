package com.stitch.payment.service;

import com.stitch.payment.model.dto.TierConfigDto;

import java.util.List;

public interface TierConfigService {

    TierConfigDto setUpTier(TierConfigDto request);

    List<TierConfigDto> fetchAllTierConfig();

//    void cumulativeDailyLimits(DailyLimitRequest dailyLimitRequest);
}
