package com.gft.prices.infrastructure.persistence.adapter;

import com.gft.prices.domain.repository.PriceRepository;
import com.gft.prices.domain.model.Price;
import com.gft.prices.infrastructure.persistence.mapper.PriceMapper;
import com.gft.prices.infrastructure.persistence.repository.PriceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepository {
    private final PriceJpaRepository priceJpaRepository;
    private final PriceMapper priceMapper;

    @Override
    public Optional<Price> findApplicablePrice(Long brandId, Long productId, LocalDateTime applicationDate) {
        return priceJpaRepository
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        brandId,
                        productId,
                        applicationDate,
                        applicationDate)
                .map(priceMapper::toDomain);
    }
}
