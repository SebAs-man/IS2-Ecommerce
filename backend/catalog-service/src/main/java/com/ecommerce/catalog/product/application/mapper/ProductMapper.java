package com.ecommerce.catalog.product.application.mapper;

import com.ecommerce.catalog.product.application.dto.response.AttributeResponseDTO;
import com.ecommerce.catalog.product.application.dto.response.ProductResponseDTO;
import com.ecommerce.catalog.product.domain.model.Product;
import com.ecommerce.catalog.product.domain.model.vo.Attribute;
import com.ecommerce.catalog.sharedkernel.application.mapper.ValueObjectMapper;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interfaz de mapeo para la conversión entre la entidad {@link Product} y su correspondiente Objeto de Transferencia de Datos (DTO),
 * {@link ProductResponseDTO}. Se utiliza para facilitar la transformación de datos de categoría entre el dominio de la aplicación y su capa de representación.
 * dominio de la aplicación y su capa de representación.
 */
@Mapper(
        componentModel = "spring",
        uses = {ValueObjectMapper.class}
)
public interface ProductMapper {
    /**
     * Convierte una entidad {@link Product} en su DTO de respuesta {@link ProductResponseDTO}.
     * @param product la instancia del producto con todos sus datos.
     * @return un {@link ProductResponseDTO} con los datos requeridos por el cliente.
     */
    ProductResponseDTO toResponseDTO(Product product);

    /**
     * Convierte una lista de entidades {@link Product} en una lista de sus DTO de respuesta {@link ProductResponseDTO}.
     * @param products la lista de instancias de productos con sus datos completos.
     * @return una {@link List<ProductResponseDTO>} que contiene los datos transformados para cada producto.
     */
    List<ProductResponseDTO> toResponseDTOs(List<Product> products);

    /**
     * Convierte una {@code Page} de entidades {@link Product} en una {@code Page} de {@link ProductResponseDTO}.
     * @param productPage una {@code Page} de {@code ProductResponseDTO}, donde cada DTO encapsula los detalles de una {@code Product}.
     *      * Si la {@code Page} de entrada es nula, se devuelve una {@code Page} vacía.
     */
    default Page<ProductResponseDTO> toProductResponseDTOPage(Page<Product> productPage) {
        if (productPage == null) {
            return Page.empty();
        }
        return productPage.map(this::toResponseDTO);
    }

    /**
     * Convierte un Object Value Attribute en su DTO de respuesta.
     * @param attribute Object Value a convertir.
     * @return un DTO de respuesta para el cliente.
     */
    AttributeResponseDTO toAttributeResponseDTO(Attribute attribute);

    /**
     * Convierte una lista de Object Value Attribute en sus DTO de respuesta.
     * @param attributes Lista de Object Value a convertir.
     * @return una lista de DTO de respuesta.
     */
    List<AttributeResponseDTO> toAttributeResponseDTOs(List<Attribute> attributes);
}
