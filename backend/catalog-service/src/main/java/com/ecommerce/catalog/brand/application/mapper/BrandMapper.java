package com.ecommerce.catalog.brand.application.mapper;

import com.ecommerce.catalog.brand.application.dto.response.BrandResponseDTO;
import com.ecommerce.catalog.brand.domain.model.Brand;
import com.ecommerce.catalog.sharedkernel.application.mapper.ValueObjectMapper;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interfaz que define un mapper para realizar transformaciones entre entidades de dominio
 * y objetos de transferencia de datos relacionados con marcas.
 * Esta interfaz facilita la conversión bidireccional entre la entidad {@link Brand},
 * que representa una marca persistente en el sistema, y el DTO {@link BrandResponseDTO}.
 * Es utilizada comúnmente en servicios y controladores para encapsular y devolver
 * datos relevantes sobre marcas en una estructura de datos más conveniente.
 */
@Mapper(
        componentModel = "spring",
        uses = {ValueObjectMapper.class} // Usa el mapper común para NonBlankString -> String
)
public interface BrandMapper {
    /**
     * Convierte una entidad {@link Brand} en un objeto de transferencia de datos {@link BrandResponseDTO}.
     *
     * @param brand la entidad de tipo {@link Brand} que contiene la información de la marca.
     * @return una instancia de {@link BrandResponseDTO} que representa los datos de la marca de manera estructurada,
     *         incluyendo su nombre, slug, descripción, logotipo y fechas de creación y actualización.
     */
    BrandResponseDTO toResponseDTO(Brand brand);

    /**
     * Convierte una lista de entidades {@link Brand} en una lista de objetos {@link BrandResponseDTO}.
     *
     * @param brands la lista de entidades {@link Brand} a transformar.
     * @return  una lista de instancias {@link BrandResponseDTO} que representan los datos de las marcas proporcionadas en un formato estructurado.
     */
    List<BrandResponseDTO> toResponseDTOs(List<Brand> brands);

    /**
     * Mapea una {@link Page} de entidades {@link Brand} a una {@link Page} de {@link BrandResponseDTO}.
     * @param brandPage la {@link Page} de entidades {@link Brand} a convertir; puede ser null.
     * @return una {@link Page} que contiene instancias de {@link BrandResponseDTO}, or una {@link Page} vacía
     * si la entrada es nula.
     */
    default Page<BrandResponseDTO> toBrandResponseDTOPage(Page<Brand> brandPage) {
        if (brandPage == null) {
            return Page.empty();
        }
        return brandPage.map(this::toResponseDTO);
    }
}
