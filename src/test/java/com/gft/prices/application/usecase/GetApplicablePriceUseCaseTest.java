package com.gft.prices.application.usecase;

import com.gft.prices.domain.exception.PriceNotFoundException;
import com.gft.prices.domain.repository.PriceRepository;
import com.gft.prices.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetApplicablePriceUseCaseTest {

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private GetApplicablePriceUseCaseImpl useCase;

    @Test
    void shouldReturnApplicablePriceWhenPriceExists() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        Price expectedPrice = Price.builder()
                .brandId(1L)
                .productId(35455L)
                .priceList(2L)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .price(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();

        when(priceRepository.findApplicablePrice(
                1L,
                35455L,
                applicationDate))
                .thenReturn(Optional.of(expectedPrice));

        Price result = useCase.getApplicablePrice(
                1L,
                35455L,
                applicationDate);

        assertThat(result).isEqualTo(expectedPrice);

        verify(priceRepository).findApplicablePrice(
                1L,
                35455L,
                applicationDate);

        verifyNoMoreInteractions(priceRepository);
    }

    @Test
    void shouldThrowPriceNotFoundExceptionWhenPriceDoesNotExist() {

        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        when(priceRepository.findApplicablePrice(
                1L,
                99999L,
                applicationDate))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                useCase.getApplicablePrice(
                        1L,
                        99999L,
                        applicationDate))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessage(
                        "No applicable price found for brandId=1, productId=99999 and applicationDate=2020-06-14T10:00");

        verify(priceRepository).findApplicablePrice(
                1L,
                99999L,
                applicationDate);

        verifyNoMoreInteractions(priceRepository);
    }
}
