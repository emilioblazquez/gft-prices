package com.gft.prices.infrastructure.persistence.mapper;

import com.gft.prices.domain.model.Price;
import com.gft.prices.infrastructure.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    Price toDomain(PriceEntity entity);
}
