package com.ecommerce.catalog.product.api;

import com.ecommerce.catalog.product.application.ProductService;
import com.ecommerce.catalog.product.application.dto.request.CreateProductRequestDTO;
import com.ecommerce.catalog.product.application.dto.request.CreateVariantRequestDTO;
import com.ecommerce.catalog.product.application.dto.request.UpdateProductRequestDTO;
import com.ecommerce.catalog.product.application.dto.request.UpdateVariantRequestDTO;
import com.ecommerce.catalog.product.application.dto.response.ProductResponseDTO;
import com.ecommerce.catalog.product.application.dto.response.VariantResponseDTO;
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
 * Controlador REST para gestionar operaciones relacionadas con los productos.
 * Proporciona endpoints para la lectura y eliminación de categorías mediante
 * la implementación de las interfaces ReadController y DeleteController.
 */
@RestController
@RequestMapping("/products")
public class ProductController implements ReadController<ProductResponseDTO, String>, DeleteController<String> {
    // --- Dependencias ---
    private final ProductService service;

    /**
     * Construye un nuevo ProductController con el ProductService especificado.
     * @param service la instancia de ProductService que proporciona la lógica de negocio para las operaciones de productos.
     */
    public ProductController(ProductService service) {
        this.service = service;
    }

    // --- EndPoints heredados ---

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping(params = "!name")
    public ResponseEntity<Page<ProductResponseDTO>> getAll(Pageable pageable) {
        Page<ProductResponseDTO> products = service.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- EndPoints funcionales ---

    /**
     * Busca productos por nombre y devuelve una lista paginada de resultados.
     * @param name el nombre o parte del nombre del producto para filtrar los resultados.
     * @param pageable los parámetros de paginación y ordenación.
     * @return una respuesta HTTP que contiene una página de productos que coinciden con el criterio de búsqueda, encapsulada en objetos ProductResponseDTO.
     */
    @GetMapping(params = "name")
    public ResponseEntity<Page<ProductResponseDTO>> searchByName(
            @RequestParam String name, Pageable pageable) {
        Page<ProductResponseDTO> response = service.searchByName(name, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca productos por un identificador de una marca y devuelve una lista paginada de resultados.
     * @param id el identificador de la marca a filtrar.
     * @param pageable los parámetros de paginación y ordenación.
     * @return una respuesta HTTP que contiene una página de productos que coinciden con la marca especificada.
     */
    @GetMapping(value = "/brand", params = "id")
    public ResponseEntity<Page<ProductResponseDTO>> searchByBrand(
            @RequestParam String id, Pageable pageable){
        Page<ProductResponseDTO> response = service.searchByBrand(id, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca productos por el identificador de una categoría y devuelve una lista paginada de resultados.
     * @param id el identificador de la categoría
     * @param pageable los parámetros de paginación y ordenación.
     * @return una respuesta HTTP que contiene una página de productos que coinciden con la categoría y sus predecesoras.
     */
    @GetMapping(value = "/categories", params = "id")
    public ResponseEntity<Page<ProductResponseDTO>> searchByCategory(
            @RequestParam String id, Pageable pageable){
        Page<ProductResponseDTO> response = service.searchByCategory(id, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene las variantes de un producto específico, paginado.
     * @param id El ID del producto padre.
     * @param pageable Objeto de paginación y ordenación inyectado por Spring.
     * @return ResponseEntity con 200 OK y un objeto Page de VariantResponseDTO.
     */
    @GetMapping("/{id}/variants")
    public ResponseEntity<Page<VariantResponseDTO>> getProductVariants(
            @PathVariable String id, Pageable pageable) {
        Page<VariantResponseDTO> variantPage = service.findVariantsByProductId(id, pageable);
        return ResponseEntity.ok(variantPage);
    }

    /**
     * Obtiene una variante específica por su ID.
     * @param id El ID único de la variante.
     * @return ResponseEntity con 200 OK y VariantResponseDTO si se encuentra, o 404 Not Found.
     */
    @GetMapping("/variants/{id}")
    public ResponseEntity<VariantResponseDTO> getVariantById(@PathVariable String id) {
        return service.findVariantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo producto base junto con su variante inicial.
     * @param request DTO con la información del producto y la variante inicial.
     * @return ResponseEntity con status 201 Created, cabecera Location y el DTO del producto creado.
     */
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody CreateProductRequestDTO request) {
        ProductResponseDTO product = service.saveProduct(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/products/{id}")
                .buildAndExpand(product.id()).toUri();
        return ResponseEntity.created(location).body(product);
    }

    /**
     * PActualiza la información base de un producto existente.
     * No actualiza el schema de variantes (attributeDefinitions) ni las variantes en sí.
     * @param id El ID del producto a actualizar.
     * @param requestDto DTO con los nuevos datos para los campos base del producto.
     * @return ResponseEntity con 200 OK y el ProductResponseDTO actualizado sí se encuentra, o 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable String id, @Valid @RequestBody UpdateProductRequestDTO requestDto) {
        return service.updateProduct(id, requestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Por si el servicio devolviera Optional vacío
    }

    /**
     * Crea una nueva variante para un producto existente.
     * @param requestDto DTO con los datos de la nueva variante.
     * @return ResponseEntity con 201 Created, cabecera Location y el DTO de la variante creada.
     */
    @PostMapping("/{id}/variants")
    public ResponseEntity<VariantResponseDTO> createProductVariant(
            @PathVariable String id, @Valid @RequestBody CreateVariantRequestDTO requestDto) {
        VariantResponseDTO createdVariant = service.saveVariant(id, requestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/products/variants/{id}")
                .buildAndExpand(createdVariant.id()).toUri();
        return ResponseEntity.created(location).body(createdVariant);
    }

    /**
     * Actualiza los datos de una variante existente (precio, stock, imágenes, etc.).
     * No permite cambiar los atributos definitorios ni el SKU.
     * @param id El ID de la variante a actualizar.
     * @param requestDto DTO con los datos a actualizar.
     * @return ResponseEntity con 200 OK y VariantResponseDTO actualizado, 404 si no se encuentra, o 409 en caso de conflicto de versión.
     */
    @PutMapping("/variants/{id}")
    public ResponseEntity<VariantResponseDTO> updateVariant(
            @PathVariable String id, @Valid @RequestBody UpdateVariantRequestDTO requestDto) {
        return service.updateVariant(id, requestDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una variante específica por su SKU.
     * @param id El ID de la variante a eliminar.
     * @return ResponseEntity 204 No Content si se elimina, o 404 Not Found.
     */
    @DeleteMapping("/variants/{id}")
    public ResponseEntity<Void> deleteVariant(@PathVariable String id) {
        service.deleteVariant(id);
        return ResponseEntity.noContent().build();
    }

}
