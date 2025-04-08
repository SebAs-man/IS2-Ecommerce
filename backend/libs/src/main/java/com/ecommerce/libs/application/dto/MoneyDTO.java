package com.ecommerce.libs.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public record MoneyDTO(
        @NotNull(message = "Amount cannot be null.")
        @PositiveOrZero(message = "Amount cannot be less to zero.")
        BigDecimal amount,

        @NotNull(message = "Currency cannot be null.")
        String currencyCode
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
