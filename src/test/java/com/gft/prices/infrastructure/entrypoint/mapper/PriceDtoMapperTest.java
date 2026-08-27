package com.gft.prices.infrastructure.entrypoint.mapper;

import com.gft.prices.domain.model.Price;
import com.gft.prices.generated.model.PriceResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

class PriceDtoMapperTest {

    private final PriceDtoMapper mapper = getMapper(PriceDtoMapper.class);

    @Test
    void shouldMapDomainToResponse() {

        Price price = Price.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(2L)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .price(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();

        PriceResponse response = mapper.toResponse(price);

        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(35455L);
        assertThat(response.getBrandId()).isEqualTo(1L);
        assertThat(response.getPriceList()).isEqualTo(2L);
        assertThat(response.getPrice()).isEqualTo(25.45);
        assertThat(response.getCurrency()).isEqualTo("EUR");

        assertThat(response.getStartDate())
                .isEqualTo(price.getStartDate().atOffset(ZoneOffset.UTC));

        assertThat(response.getEndDate())
                .isEqualTo(price.getEndDate().atOffset(ZoneOffset.UTC));
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        assertThat(mapper.toResponse(null)).isNull();
    }
}