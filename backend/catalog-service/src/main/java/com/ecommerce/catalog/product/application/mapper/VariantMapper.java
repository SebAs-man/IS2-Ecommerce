package com.ecommerce.catalog.product.application.mapper;

import com.ecommerce.catalog.product.application.dto.response.VariantResponseDTO;
import com.ecommerce.catalog.product.domain.model.Variant;
import com.ecommerce.catalog.sharedkernel.application.dto.MoneyDTO;
import com.ecommerce.catalog.sharedkernel.application.mapper.ValueObjectMapper;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.Money;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interfaz de mapeo para la conversión entre la entidad {@link Variant} y su correspondiente Objeto de Transferencia de Datos (DTO),
 * {@link VariantResponseDTO}. Se utiliza para facilitar la transformación de datos de categoría entre el dominio de la aplicación y su capa de representación.
 * dominio de la aplicación y su capa de representación.
 */
@Mapper(
        componentModel = "spring",
        uses = {ValueObjectMapper.class}
)
public interface VariantMapper {
    /**
     * Convierte una entidad {@link Variant} en su DTO de respuesta {@link VariantResponseDTO}.
     * @param variant la instancia del producto con todos sus datos.
     * @return un {@link VariantResponseDTO} con los datos requeridos por el cliente.
     */
    VariantResponseDTO toResponseDTO(Variant variant);

    /**
     * Convierte una lista de entidades {@link Variant} en una lista de sus DTO de respuesta {@link VariantResponseDTO}.
     * @param variants la lista de instancias de productos con sus datos completos.
     * @return una {@link List<VariantResponseDTO>} que contiene los datos transformados para cada producto.
     */
    List<VariantResponseDTO> toResponseDTO(List<Variant> variants);

    /**
     * Convierte una {@code Page} de entidades {@link Variant} en una {@code Page} de {@link VariantResponseDTO}.
     * @param variantPage una {@code Page} de {@code VariantResponseDTO}, donde cada DTO encapsula los detalles de una {@code Variant}.
     * Si la {@code Page} de entrada es nula, se devuelve una {@code Page} vacía.
     */
    default Page<VariantResponseDTO> toProductResponseDTOPage(Page<Variant> variantPage) {
        if (variantPage == null) {
            return Page.empty();
        }
        return variantPage.map(this::toResponseDTO);
    }
}
