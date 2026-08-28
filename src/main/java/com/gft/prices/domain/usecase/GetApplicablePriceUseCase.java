package com.gft.prices.domain.usecase;

import com.gft.prices.domain.model.Price;

import java.time.LocalDateTime;

public interface GetApplicablePriceUseCase {
    Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate);
}
