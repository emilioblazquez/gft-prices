package com.gft.prices.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class Price {
    private Long id;

    private Long brandId;

    private Long productId;

    private Long priceList;

    private Integer priority;

    private BigDecimal price;

    private String currency;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
