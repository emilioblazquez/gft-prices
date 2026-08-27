package com.gft.prices.infrastructure.persistence.adapter;

import com.gft.prices.domain.model.Price;
import com.gft.prices.infrastructure.persistence.entity.PriceEntity;
import com.gft.prices.infrastructure.persistence.mapper.PriceMapper;
import com.gft.prices.infrastructure.persistence.repository.PriceJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceRepositoryAdapterTest {

    @Mock
    private PriceJpaRepository priceJpaRepository;

    @Mock
    private PriceMapper priceMapper;

    @InjectMocks
    private PriceRepositoryAdapter adapter;

    @Test
    void shouldReturnMappedDomainPriceWhenEntityExists() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        PriceEntity entity = PriceEntity.builder()
                .id(1L)
                .brandId(1L)
                .productId(35455L)
                .priceList(2L)
                .priority(1)
                .price(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .build();

        Price expectedPrice = Price.builder()
                .id(entity.getId())
                .brandId(entity.getBrandId())
                .productId(entity.getProductId())
                .priceList(entity.getPriceList())
                .priority(entity.getPriority())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();

        when(priceMapper.toDomain(entity)).thenReturn(expectedPrice);

        when(priceJpaRepository
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        1L,
                        35455L,
                        applicationDate,
                        applicationDate))
                .thenReturn(Optional.of(entity));

        Optional<Price> result =
                adapter.findApplicablePrice(1L, 35455L, applicationDate);

        assertThat(result).isPresent();

        Price price = result.get();

        assertThat(price.getId()).isEqualTo(entity.getId());
        assertThat(price.getBrandId()).isEqualTo(entity.getBrandId());
        assertThat(price.getProductId()).isEqualTo(entity.getProductId());
        assertThat(price.getPriceList()).isEqualTo(entity.getPriceList());
        assertThat(price.getPriority()).isEqualTo(entity.getPriority());
        assertThat(price.getPrice()).isEqualByComparingTo(entity.getPrice());
        assertThat(price.getCurrency()).isEqualTo(entity.getCurrency());
        assertThat(price.getStartDate()).isEqualTo(entity.getStartDate());
        assertThat(price.getEndDate()).isEqualTo(entity.getEndDate());

        verify(priceJpaRepository)
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        1L,
                        35455L,
                        applicationDate,
                        applicationDate);

        verifyNoMoreInteractions(priceJpaRepository);
    }

    @Test
    void shouldReturnEmptyOptionalWhenEntityDoesNotExist() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        when(priceJpaRepository
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        1L,
                        35455L,
                        applicationDate,
                        applicationDate))
                .thenReturn(Optional.empty());

        Optional<Price> result =
                adapter.findApplicablePrice(1L, 35455L, applicationDate);

        assertThat(result).isEmpty();

        verify(priceJpaRepository)
                .findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        1L,
                        35455L,
                        applicationDate,
                        applicationDate);

        verifyNoMoreInteractions(priceJpaRepository);
    }
}
