package com.gft.prices.infrastructure.persistence.mapper;

import com.gft.prices.domain.model.Price;
import com.gft.prices.infrastructure.persistence.entity.PriceEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

class PriceMapperTest {

    private final PriceMapper mapper = getMapper(PriceMapper.class);

    @Test
    void shouldMapEntityToDomain() {

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

        Price result = mapper.toDomain(entity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(entity.getId());
        assertThat(result.getBrandId()).isEqualTo(entity.getBrandId());
        assertThat(result.getProductId()).isEqualTo(entity.getProductId());
        assertThat(result.getPriceList()).isEqualTo(entity.getPriceList());
        assertThat(result.getPriority()).isEqualTo(entity.getPriority());
        assertThat(result.getPrice()).isEqualByComparingTo(entity.getPrice());
        assertThat(result.getCurrency()).isEqualTo(entity.getCurrency());
        assertThat(result.getStartDate()).isEqualTo(entity.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(entity.getEndDate());
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }
}