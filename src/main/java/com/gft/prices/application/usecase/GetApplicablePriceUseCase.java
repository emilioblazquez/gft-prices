package com.gft.prices.application.usecase;

import com.gft.prices.domain.model.Price;

import java.time.LocalDateTime;

public interface GetApplicablePriceUseCase {
    Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
