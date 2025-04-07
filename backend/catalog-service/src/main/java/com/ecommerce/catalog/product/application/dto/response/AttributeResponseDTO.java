package com.ecommerce.catalog.product.application.dto.response;

import com.ecommerce.catalog.product.domain.constant.AttributeType;

import java.io.Serial;
import java.io.Serializable;

public record AttributeResponseDTO(
        String key,
        String label,
        AttributeType type,
        Boolean isVariantOption,
        Boolean isRequired,
        Object defaultValue
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
