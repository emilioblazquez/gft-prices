package com.gft.prices.infrastructure.entrypoint.mapper;

import com.gft.prices.domain.model.Price;
import com.gft.prices.generated.model.PriceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceDtoMapper {

    @Mapping(
            target = "startDate",
            expression = "java(price.getStartDate().atOffset(java.time.ZoneOffset.UTC))"
    )
    @Mapping(
            target = "endDate",
            expression = "java(price.getEndDate().atOffset(java.time.ZoneOffset.UTC))"
    )
    @Mapping(
            target = "price",
            expression = "java(price.getPrice().doubleValue())"
    )
    PriceResponse toResponse(Price price);
}