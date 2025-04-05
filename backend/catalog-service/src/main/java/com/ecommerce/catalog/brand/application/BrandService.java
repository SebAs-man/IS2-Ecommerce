package com.ecommerce.catalog.brand.application;

import com.ecommerce.catalog.brand.application.dto.response.BrandResponseDTO;
import com.ecommerce.catalog.brand.application.dto.request.BrandRequestDTO;
import com.ecommerce.catalog.brand.application.mapper.BrandMapper;
import com.ecommerce.catalog.brand.domain.model.Brand;
import com.ecommerce.catalog.brand.domain.repository.BrandRepository;
import com.ecommerce.catalog.sharedkernel.application.DeleteService;
import com.ecommerce.catalog.sharedkernel.application.ReadService;
import com.ecommerce.catalog.sharedkernel.application.exception.ResourceNotFoundException;
import com.ecommerce.catalog.sharedkernel.application.util.IdGenerator;
import com.ecommerce.catalog.sharedkernel.domain.model.vo.NonBlankString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de aplicación para la gestión de marcas dentro del catálogo de productos.
 * Esta clase se encarga de coordinar la lógica relacionada con las marcas, proporcionando
 * métodos para interactuar con el repositorio de marcas y realizar conversiones entre
 * entidades de dominio y DTO mediante el mapper.
 * También utiliza el generador de slugs para asignar identificadores únicos y legibles
 * en las URL para las marcas. Este servicio encapsula la lógica empresarial y facilita
 * las interacciones de la capa de persistencia con la capa de presentación o API.
 */
@Service
public class BrandService implements ReadService<BrandResponseDTO, String>, DeleteService<String> {
    private static final Logger log = LoggerFactory.getLogger(BrandService.class);
    // --- Dependencias ---
    private final BrandRepository repository;
    private final BrandMapper mapper;

    /**
     * Constructor del servicio de gestión de marcas dentro del catálogo de productos.
     * Inicializa las dependencias necesarias para interactuar con el repositorio de marcas,
     * mapear datos entre entidades y DTO, y generar slugs únicos para las marcas.
     * @param repository el repositorio utilizado para acceder y persistir datos relacionados con las marcas.
     * @param mapper el mapeador para convertir entre entidades de dominio y objetos de transferencia de datos (DTO).
     */
    public BrandService(BrandRepository repository, BrandMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // --- Métodos heredados ---

    @Override
    @Transactional(readOnly = true)
    public Optional<BrandResponseDTO> findById(String id) {
        log.debug("Buscando marca por ID: {}", id);
        return repository.findById(id)
                .map(mapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandResponseDTO> findAll(Pageable pageable) {
        log.debug("Buscando todas las marcas, paginado: {}", pageable);
        Page<Brand> brandPage = repository.findAll(pageable);
        return mapper.toBrandResponseDTOPage(brandPage);
    }

    @Override
    @Transactional
    public void delete(String s) {
        log.info("Eliminando marca: {}", s);
        if(!repository.existsById(s)){
            throw new ResourceNotFoundException("Brand", "ID", s);
        }
        // ¿qué pasa con los productos de esta marca?
        repository.deleteById(s);
        log.info("Marca eliminada con ID {}", s);
    }

    // --- Métodos funcionales ---

    /**
     * Busca marcas con nombres que contengan la cadena especificada, ignorando mayúsculas y minúsculas,
     * y devuelve los resultados en un formato paginado.
     *
     * @param name la subcadena a buscar dentro de los nombres de marca
     * @param pageable la información de paginación
     * @return una lista paginada de marcas que coinciden con los criterios de búsqueda como BrandResponseDTO
     */
    @Transactional(readOnly = true)
    public Page<BrandResponseDTO> searchByName(String name, Pageable pageable) {
        log.debug("Buscando marcas por nombre que contengan '{}', paginado: {}", name, pageable);
        Page<Brand> brandPage = repository.findByNameValueContainingIgnoreCase(name, pageable);
        return mapper.toBrandResponseDTOPage(brandPage);
    }

    /**
     * Guarda una nueva entidad de marca tras validar la unicidad de su nombre
     * y devuelve los detalles de la marca creada como un DTO de respuesta.
     * @param request el objeto de transferencia de datos que contiene los detalles
     * de la marca a crear, incluyendo el nombre, descripción y URL del logotipo.
     * @return un objeto de transferencia de datos que contiene los detalles de la
     * marca guardada con éxito, incluyendo su identificador único.
     */
    @Transactional
    public BrandResponseDTO save(BrandRequestDTO request) {
        log.info("Creando marca con nombre: {}", request.name());
        // Valida unicidad del nombre antes de crear la entidad
        String normalizedName = new NonBlankString(request.name()).value();
        if(repository.existsByNameValueIgnoreCase(normalizedName)){
            throw new DataIntegrityViolationException("There is already a brand with name: " + normalizedName);
        }
        // Generar un identificador aleatorio
        String id = IdGenerator.generateId();
        log.debug("ID generado para nueva marca: {}", id);
        // Crear la entidad
        Brand brand = new Brand(
                id,
                request.name(),
                request.description(),
                request.logoUrl()
        );
        // Guardar la nueva instancia
        try {
            Brand savedBrand = repository.save(brand);
            log.info("Marca creada con ID {}", id);
            return mapper.toResponseDTO(savedBrand);
        } catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException(
                    "Branding conflict: (possible name repeated)" + request.name(), e);
        }
    }

    /**
     * Actualiza una entidad de Marca existente identificada por su ID con la información proporcionada.
     *
     * @param id el identificador único de la marca a actualizar
     * @param request el objeto BrandRequestDTO que contiene la información actualizada como nombre, descripción y logoUrl
     * @return Un Optional que contiene el BrandResponseDTO actualizado si la actualización se ha realizado correctamente, o un Optional vacío en caso contrario
     */
    public Optional<BrandResponseDTO> update(String id, BrandRequestDTO request){
        log.info("Actualizando marca ID: {}", id);
        Brand brand = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "ID", id));
        // Verificar si el nombre cambió y es único
        String originalName = brand.getName().value();
        String currentName = new NonBlankString(request.name()).value();
        if(!currentName.equalsIgnoreCase(originalName) &&
                repository.existsByNameValueIgnoreCase(currentName)){
            throw new DataIntegrityViolationException("There is already a brand with name " + currentName);
        }
        // Modificar la instancia
        brand.setName(request.name());
        brand.setDescription(request.description());
        brand.setLogoUrl(request.logoUrl());
        // Guardar la instancia modificada
        try {
            Brand savedBrand = repository.save(brand);
            log.info("Marca actualizada con ID {}", savedBrand.getId());
            return Optional.of(mapper.toResponseDTO(brand));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                    "Branding conflict: (possible name repeated)" + request.name(), e);
        }
    }
}
