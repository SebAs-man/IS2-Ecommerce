package com.ecommerce.catalog.product.application.dto.request;

import com.ecommerce.catalog.product.domain.model.vo.Attribute;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

/**
 * Objeto de transferencia de datos (DTO) que representa una solicitud de creación o actualización de un producto.
 * Este registro encapsula detalles sobre el producto, incluyendo su nombre, descripción,
 * marca, categorías asociadas, atributos estáticos, atributos dinámicos para variantes,
 * y los detalles de la variante principal.
 * @param name - El nombre del producto. Debe ser una cadena no en blanco con un máximo de 200 caracteres.
 * @param description - La descripción del producto. Puede tener una longitud máxima de 5000 caracteres o ser nula.
 * @param brandId - Un identificador de la marca asociada al producto. Puede ser nulo.
 * @param categoryId - Una lista de identificadores de categoría a la que pertenece el producto. Puede estar vacía o ser nula si no se especifican categorías.
 * @param staticAttributes - Un mapa de pares clave-valor estáticos que representan atributos predefinidos
 * que se aplican al producto en su conjunto (por ejemplo, material, color).
 * @param dynamicAttributes - Una lista de atributos que definen posibles opciones de variantes. Estos atributos se utilizan
 * para generar dinámicamente variantes del producto. Se requiere al menos un atributo
 * si el producto tiene múltiples variantes. Estos atributos deben ser válidos y contener
 * suficientes detalles sobre la clave, el tipo y las restricciones.
 * @param variant - La variante inicial o primaria del producto. Debe facilitarse y su estructura
 * se valida con arreglo a las normas definidas.
 */
public record ProductRequestDTO(
        @NotBlank @Size(max = 200) String name,
        @Size(max = 5000) String description,
        String brandId,
        List<String> categoryId,
        Map<String, String> staticAttributes,
        @NotNull @Size(min = 1, message = "Debe definir al menos una opción de variante si el producto no es simple.")
        @Valid List<Attribute> dynamicAttributes,
        @NotNull @Valid VariantRequestDTO variant
) {
}
