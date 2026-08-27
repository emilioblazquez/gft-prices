package com.gft.prices.domain.repository;

import com.gft.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findApplicablePrice(
            Long brandId,
            Long productId,
            LocalDateTime applicationDate
    );
}
