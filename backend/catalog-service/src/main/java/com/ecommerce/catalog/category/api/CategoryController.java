package com.ecommerce.catalog.category.api;

import com.ecommerce.catalog.category.application.CategoryService;
import com.ecommerce.catalog.category.application.dto.request.CreateCategoryRequestDTO;
import com.ecommerce.catalog.category.application.dto.request.UpdateCategoryRequest;
import com.ecommerce.catalog.category.application.dto.response.CategoryResponseDTO;
import com.ecommerce.catalog.sharedkernel.api.DeleteController;
import com.ecommerce.catalog.sharedkernel.api.ReadController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controlador REST para gestionar operaciones relacionadas con las categorías.
 * Proporciona endpoints para la lectura y eliminación de categorías mediante
 * la implementación de las interfaces ReadController y DeleteController.
 */
@RestController
@RequestMapping("/categories")
public class CategoryController implements ReadController<CategoryResponseDTO, String>, DeleteController<String> {
    // --- Dependencias ---
    private final CategoryService service;

    /**
     * Construye un nuevo CategoryController con el CategoryService especificado.
     * @param service la instancia de CategoryService que proporciona la lógica de negocio para las operaciones de categorías.
     */
    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // --- EndPoints heredados ---

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping(params = "!name")
    public ResponseEntity<Page<CategoryResponseDTO>> getAll(Pageable pageable) {
        Page<CategoryResponseDTO> categoryPage = service.findAll(pageable);
        return ResponseEntity.ok(categoryPage);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- EndPoint funcionales ---

    /**
     * Busca categorías por nombre y devuelve una lista paginada de resultados.
     *
     * @param name el nombre o parte del nombre de la categoría para filtrar los resultados.
     * @param pageable los parámetros de paginación y ordenación.
     * @return una respuesta HTTP que contiene una página de categorías que coinciden con el criterio de búsqueda, encapsulada en objetos CategoryResponseDTO.
     */
    @GetMapping(params = "name")
    public ResponseEntity<Page<CategoryResponseDTO>> searchByName(
            @RequestParam String name, Pageable pageable) {
        Page<CategoryResponseDTO> response = service.searchByName(name, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Recupera una lista paginada de subcategorías directas para una categoría específica.
     * @param id el identificador de la categoría cuyas subcategorías directas se recuperarán.
     * @param pageable los parámetros de paginación y ordenación.
     * @return una ResponseEntity que contiene una página de objetos CategoryResponseDTO que representan las subcategorías directas.
     */
    @GetMapping("/{id}/categories")
    public ResponseEntity<Page<CategoryResponseDTO>> getDirectById(
            @PathVariable String id, Pageable pageable) {
        Page<CategoryResponseDTO> categoryPage = service.findDirectSubcategories(id, pageable);
        return ResponseEntity.ok(categoryPage);
    }

    /**
     * Crea una nueva categoría basándose en los datos de la solicitud.
     * @param request una instancia de CreateCategoryRequestDTO que contiene los detalles de la categoría que se creará.
     * @return una ResponseEntity que contiene el objeto CategoryResponseDTO creado y un encabezado de ubicación que apunta al URI de la categoría recién creada.
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CreateCategoryRequestDTO request) {
        CategoryResponseDTO response = service.save(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.name()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Actualiza una categoría existente con la información proporcionada.
     * @param id el identificador único de la categoría a actualizar.
     * @param request el objeto que contiene los detalles actualizados de la categoría.
     * @return una ResponseEntity que contiene los datos actualizados de la categoría si la operación es correcta.
     * O una respuesta 404 No encontrado si la categoría no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable String id, @Valid @RequestBody UpdateCategoryRequest request){
        return service.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
