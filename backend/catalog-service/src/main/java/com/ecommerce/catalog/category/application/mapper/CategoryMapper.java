package com.ecommerce.catalog.category.application.mapper;

import com.ecommerce.catalog.category.application.dto.response.CategoryResponseDTO;
import com.ecommerce.catalog.category.domain.model.Category;
import com.ecommerce.libs.application.mapper.ValueObjectMapper;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interfaz de mapeo para la conversión entre la entidad {@link Category} y su correspondiente Objeto de Transferencia de Datos (DTO),
 * {@link CategoryResponseDTO}. Se utiliza para facilitar la transformación de datos de categoría entre el dominio de la aplicación y su capa de representación.
 * dominio de la aplicación y su capa de representación.
 * El mapeador aprovecha MapStruct para generar automáticamente la implementación basada en los mapeos definidos.
 * Está diseñado para ser utilizado en un contexto Spring y utiliza mapeadores adicionales como {@link ValueObjectMapper} para la transformación de valores personalizados.
 */
@Mapper(
        componentModel = "spring",
        uses = {ValueObjectMapper.class} // Usa el mapper común para NonBlankString -> String
)
public interface CategoryMapper {
    /**
     * Convierte una entidad {@link Category} en un {@link CategoryResponseDTO}.
     * @param category la entidad {@code Category} que contiene los datos de la categoría a convertir.
     * Debe incluir campos como name, description, parentId, ancestors,
     * así como marcas de tiempo de creación y última actualización.
     * @return  un {@code CategoryResponseDTO} que contiene los datos transformados de la {@code Category} dada.
     */
    CategoryResponseDTO toResponseDTO(Category category);

    /**
     * Transforma una lista de entidades {@code Category} en una lista de objetos de transferencia de datos
     * ({@code CategoryResponseDTO}), que encapsulan la información relevante de las categorías.
     * @param categories la lista de entidades {@code Category} que se desea convertir.
     * Cada objeto en la lista representa una categoría con atributos como nombre, descripción,
     * identificador del padre, jerarquía de ancestros y marcas de tiempo.
     * @return una lista de {@code CategoryResponseDTO} que contiene los datos transformados de las categorías proporcionadas.
     * Si la lista de entrada es {@code null} o vacía, se retorna una lista vacía.
     */
    List<CategoryResponseDTO> toResponseDTOs(List<Category> categories);

    /**
     * Convierte una {@code Page} de entidades {@link Category} en una {@code Page} de {@link CategoryResponseDTO}.
     * Cada entidad {@code Category} se transforma en su correspondiente representación {@code CategoryResponseDTO}.
     * @param categoryPage la {@code Página} de entidades {@code Categoría} que se va a convertir.
     * Puede contener múltiples objetos {@code Category} con atributos como nombre, descripción, parentId,
     * ancestors, y marcas de tiempo para la creación y la última actualización. Puede ser nulo, en cuyo caso se devuelve una {@code Page} vacía.
     * @return una {@code Page} de {@code CategoryResponseDTO}, donde cada DTO encapsula los detalles de una {@code Category}.
     * Si la {@code Page} de entrada es nula, se devuelve una {@code Page} vacía.
     */
    default Page<CategoryResponseDTO> toCategoryResponseDTOPage(Page<Category> categoryPage) {
        if (categoryPage == null) {
            return Page.empty();
        }
        return categoryPage.map(this::toResponseDTO);
    }
}
