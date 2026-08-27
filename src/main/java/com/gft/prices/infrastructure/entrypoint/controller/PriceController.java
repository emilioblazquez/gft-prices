package com.gft.prices.infrastructure.entrypoint.controller;

import com.gft.prices.application.usecase.GetApplicablePriceUseCase;
import com.gft.prices.domain.model.Price;
import com.gft.prices.generated.api.PricesApi;
import com.gft.prices.generated.model.PriceResponse;
import com.gft.prices.infrastructure.entrypoint.mapper.PriceDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class PriceController implements PricesApi {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;
    private final PriceDtoMapper priceDtoMapper;

    @Override
    public ResponseEntity<PriceResponse> getApplicablePrice(
            OffsetDateTime applicationDate,
            Long productId,
            Long brandId) {

        Price price = getApplicablePriceUseCase.getApplicablePrice(
                brandId,
                productId,
                applicationDate.toLocalDateTime());

        return ResponseEntity.ok(
                priceDtoMapper.toResponse(price)
        );
    }
}