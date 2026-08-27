package com.gft.prices.infrastructure.entrypoint.controller;

import com.gft.prices.application.usecase.GetApplicablePriceUseCase;
import com.gft.prices.domain.model.Price;
import com.gft.prices.generated.model.PriceResponse;
import com.gft.prices.infrastructure.entrypoint.mapper.PriceDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

    @Mock
    private GetApplicablePriceUseCase getApplicablePriceUseCase;

    @Mock
    private PriceDtoMapper priceDtoMapper;

    @InjectMocks
    private PriceController controller;

    @Test
    void shouldReturnApplicablePrice() {
        Long brandId = 1L;
        Long productId = 35455L;
        OffsetDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0)
                        .atOffset(ZoneOffset.UTC);

        Price price = mock(Price.class);
        PriceResponse response = mock(PriceResponse.class);

        when(getApplicablePriceUseCase.getApplicablePrice(
                brandId,
                productId,
                applicationDate.toLocalDateTime()))
                .thenReturn(price);

        when(priceDtoMapper.toResponse(price))
                .thenReturn(response);

        ResponseEntity<PriceResponse> result =
                controller.getApplicablePrice(
                        applicationDate,
                        productId,
                        brandId);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(result.getBody())
                .isSameAs(response);

        verify(getApplicablePriceUseCase)
                .getApplicablePrice(
                        brandId,
                        productId,
                        applicationDate.toLocalDateTime());

        verify(priceDtoMapper)
                .toResponse(price);

        verifyNoMoreInteractions(
                getApplicablePriceUseCase,
                priceDtoMapper);
    }
}
