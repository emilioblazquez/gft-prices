package com.gft.prices.application.usecase;

import com.gft.prices.domain.exception.PriceNotFoundException;
import com.gft.prices.domain.repository.PriceRepository;
import com.gft.prices.domain.model.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetApplicablePriceUseCaseImpl implements GetApplicablePriceUseCase {
    private final PriceRepository priceRepository;

    public Price getApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceRepository.findApplicablePrice(
                        brandId,
                        productId,
                        applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(
                        brandId,
                        productId,
                        applicationDate));
    }
}
