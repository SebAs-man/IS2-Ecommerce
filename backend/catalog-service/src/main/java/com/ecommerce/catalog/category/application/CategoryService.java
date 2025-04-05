package com.ecommerce.catalog.category.application;

import com.ecommerce.catalog.category.application.dto.request.CreateCategoryRequestDTO;
import com.ecommerce.catalog.category.application.dto.request.UpdateCategoryRequest;
import com.ecommerce.catalog.category.application.dto.response.CategoryResponseDTO;
import com.ecommerce.catalog.category.application.mapper.CategoryMapper;
import com.ecommerce.catalog.category.domain.exception.CategoryNotEmptyException;
import com.ecommerce.catalog.category.domain.model.Category;
import com.ecommerce.catalog.category.domain.repository.CategoryRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase de servicio para gestionar operaciones relacionadas con categorías.
 * Esta clase funciona como una capa de servicio de aplicación para gestionar
 * operaciones relacionadas con categorías, como la obtención de detalles y la eliminación de categorías.
 * Implementa las interfaces ReadService y DeleteService para proporcionar métodos genéricos de recuperación y eliminación.
 */
@Service
public class CategoryService implements ReadService<CategoryResponseDTO, String>, DeleteService<String> {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    // --- Dependencias ---
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    /**
     * Clase de servicio para gestionar operaciones relacionadas con categorías.
     * @param repository la interfaz del repositorio para acceder a los datos de categorías.
     * @param categoryMapper el asignador utilizado para convertir entre objetos de dominio y DTO.
     */
    public CategoryService(CategoryRepository repository, CategoryMapper categoryMapper) {
        this.repository = repository;
        this.mapper = categoryMapper;
    }

    // --- Métodos heredados ---

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryResponseDTO> findById(String id) {
        log.debug("Buscando categoría por ID: {}", id);
        return repository.findById(id)
                .map(mapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findAll(Pageable pageable) {
        log.debug("Buscando categorías raíz, paginado: {}", pageable);
        Page<Category> categoryPage = repository.findByParentIdIsNull(pageable);
        return mapper.toCategoryResponseDTOPage(categoryPage);
    }

    @Override
    @Transactional
    public void delete(String id) {
        log.info("Intentando eliminar categoría ID: {}", id);
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Category", "ID", id);
        }
        // Verificar que no tenga hijos
        if(repository.countByParentId(id) > 0){
            throw new CategoryNotEmptyException(id);
        }
        // ¿Qué pasa con los productos de esta categoría?
        repository.deleteById(id);
        log.info("Categoría eliminada ID: {}", id);
    }

    // --- Métodos funcionales ---

    /**
     * Busca categorías por nombre y devuelve un resultado paginado.
     *
     * @param name el nombre de la categoría o parte del nombre para filtrar los resultados.
     * @param pageable la información de paginación y ordenación.
     * @return una página de categorías que coinciden con el nombre especificado, encapsulada en objetos CategoryResponseDTO.
     */
    public Page<CategoryResponseDTO> searchByName(String name, Pageable pageable) {
        log.debug("Buscando categorías por nombre que contengan '{}', paginado: {}", name, pageable);
        Page<Category> categoryPage = repository.findByNameValueContainingIgnoreCase(name, pageable);
        return mapper.toCategoryResponseDTOPage(categoryPage);
    }

    /**
     * Recupera una lista paginada de subcategorías directas para la categoría principal especificada.
     * @param parentId el ID de la categoría principal cuyas subcategorías se recuperarán.
     * @param pageable la información de paginación y ordenación del resultado.
     * @return una lista paginada de subcategorías, representadas por objetos CategoryResponseDTO.
     */
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findDirectSubcategories(String parentId, Pageable pageable){
        log.debug("Buscando subcategorías de {} paginado: {}", parentId, pageable);
        if(!repository.existsById(parentId)) {
            throw new ResourceNotFoundException("Category", "ID", parentId);
        }
        Page<Category> categoryPage = repository.findByParentId(parentId, pageable);
        return mapper.toCategoryResponseDTOPage(categoryPage);
    }

    /**
     * Guarda una nueva categoría basándose en los datos de la solicitud.
     * Este método valida la unicidad del nombre de la categoría,
     * determina los ancestros si se proporciona un ID de categoría principal,
     * genera un identificador único para la categoría y almacena la nueva categoría
     * de forma persistente en el repositorio.
     * @param request el objeto de transferencia de datos que contiene la información necesaria para crear una nueva categoría, incluyendo nombre, descripción e ID de categoría principal opcional.
     * @return un {@code CategoryResponseDTO} con los detalles de la categoría recién guardada.
     */
    @Transactional
    public CategoryResponseDTO save(CreateCategoryRequestDTO request) {
        log.info("Creando categoría con nombre: '{}', padre ID: {}", request.name(), request.parentId());
        // Validar unicidad del nombre
        String normalizedName = new NonBlankString(request.name()).value();
        if(repository.existsByNameValueIgnoreCase(normalizedName)){
            throw new DataIntegrityViolationException("There is ready a category with name: " + normalizedName);
        }
        // Validar ancestros
        List<String> ancestors = new ArrayList<>();
        String parentId = request.parentId();
        if(parentId != null && !parentId.isBlank()){
           Category parent = repository.findAncestorsById(parentId)
                   .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", parentId));
           ancestors.addAll(parent.getAncestors());
        }
        // Generar un identificador aleatorio
        String id = IdGenerator.generateId();
        log.debug("ID generado para nueva categoría: {}", id);
        // Crear la entidad
        Category category = new Category(
                id,
                request.name(),
                request.description(),
                parentId,
                ancestors
        );
        // Guardar la nueva instancia
        try{
            Category savedCategory = repository.save(category);
            log.info("Categoría creada ID {}", savedCategory.getId());
            return mapper.toResponseDTO(savedCategory);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException(
                    "Category conflict: (possible name repeated)" + request.name(), e
            );
        }
    }

    /**
     * Actualiza los detalles de una categoría existente utilizando el ID proporcionado y los datos de la solicitud de actualización.
     * @param id el identificador único de la categoría que se actualizará.
     * @param request los datos que contienen los valores actualizados de la categoría.
     * @return un objeto de transferencia de datos (DTO) que representa los detalles actualizados de la categoría.
     */
    public Optional<CategoryResponseDTO> update(String id, UpdateCategoryRequest request){
        log.info("Actualizando detalles de categoría ID: {}", id);
        Category category = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", id));
        // Verificar si el nombre cambió y es único
        String originalName = category.getName().value();
        String currentName = new NonBlankString(request.name()).value();
        if(currentName.equalsIgnoreCase(originalName)){
            throw new DataIntegrityViolationException("There is ready a category with name " + currentName);
        }
        // Modificar la instancia
        category.setName(request.name());
        category.setDescription(request.description());
        // Guardar la instancia modificada
        try{
            Category savedCategory = repository.save(category);
            log.info("Categoría actualizada ID {}", savedCategory.getId());
            return Optional.of(mapper.toResponseDTO(savedCategory));
        } catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException(
                    "Category conflict: (possible name repeated)" + request.name(), e
            );
        }
    }
}
