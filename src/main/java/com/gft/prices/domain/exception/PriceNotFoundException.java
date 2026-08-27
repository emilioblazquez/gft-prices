package com.gft.prices.domain.exception;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(Long brandId,
                                  Long productId,
                                  LocalDateTime applicationDate) {
        super(String.format(
                "No applicable price found for brandId=%d, productId=%d and applicationDate=%s",
                brandId,
                productId,
                applicationDate));
    }
}
