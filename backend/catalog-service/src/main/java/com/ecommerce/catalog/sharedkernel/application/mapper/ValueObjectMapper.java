package com.ecommerce.catalog.sharedkernel.application.mapper;

import com.ecommerce.catalog.sharedkernel.application.dto.MoneyDTO;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.Money;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonNegativeInteger;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;

import java.util.Currency;

/**
 * Clase especializada que proporciona funciones genéricas de mapeo para la conversión de
 * Value Objects (Objetos de valor) a sus representaciones internas y viceversa.
 * Diseñada como componente base para extender y agregar lógica de mapeo personalizada en proyectos específicos.
 */
@Component
public final class ValueObjectMapper {
    /**
     * Convierte un NonBlankString a su valor String interno.
     * @param nbString El Value Object NonBlankString (puede ser null).
     * @return El String interno, o null si nbString es null.
     */
    public String nonBlankStringToString(NonBlankString nbString) {
        return nbString == null ? null : nbString.value();
    }

    /**
     * Convierte un NonNegativeInteger a su valor Integer interno.
     * @param nbInteger El Value Object NonNegativeInteger (puede ser null).
     * @return El Integer interno, o null si nbInteger es null.
     */
    public Integer nonNegativeIntegerToInteger(NonNegativeInteger nbInteger) {
        return nbInteger == null ? null : nbInteger.value();
    }

    /**
     * Convierte el VO Money del dominio al DTO para la API
     * @param money Moneda entidad con todos sus datos.
     * @return Moneda dto convertida.
     */
    public MoneyDTO moneyToMoneyDTO(Money money) {
        if (money == null) return null;
        return new MoneyDTO(money.amount(), money.currency().getCurrencyCode());
    }

    /**
     * Convierte el MoneyDTO de la API al VO Money del dominio.
     * @param dto clase de DTO de la moneda.
     * @return la entidad de la moneda.
     */
    public Money moneyDTOToMoney(MoneyDTO dto) {
        if (dto == null || dto.currencyCode() == null || dto.amount() == null) {
            throw new IllegalArgumentException("Money DTO must have a non-null value.");
        }
        try {
            // Valida que el código de moneda sea válido al intentar obtener la instancia
            Currency currency = Currency.getInstance(dto.currencyCode());
            // Llama al constructor del record Money (que valida amount/currency no nulos)
            return new Money(dto.amount(), currency);
        } catch (IllegalArgumentException e) {
            // Código de moneda inválido
            throw new MappingException("Código de moneda inválido en MoneyDTO: " + dto.currencyCode(), e);
        }
    }
}
