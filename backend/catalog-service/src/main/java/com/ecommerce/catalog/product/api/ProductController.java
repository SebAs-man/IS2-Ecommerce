package com.ecommerce.catalog.product.api;

import com.ecommerce.catalog.product.application.dto.request.ProductRequestDTO;
import com.ecommerce.catalog.product.application.dto.response.ProductResponseDTO;
import com.ecommerce.catalog.sharedkernel.api.GenericController;
import com.ecommerce.catalog.product.application.ProductApplicationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Clase controladora de las solicitudes HTTP que se pidan al microservicio.
 */
@RestController
@RequestMapping("/products")//ruta base para la API de productos
public class ProductController implements GenericController<ProductRequestDTO, ProductResponseDTO, String> {
    //Permite acceder a los eventos que ocurran en sistema para obtener mayor información de los procesos.
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    //Instancias de dependencia del controlador.
    private final ProductApplicationService service;

    /**
     * Constructor del controlador que permite la inyección de dependencias de la aplicación
     * @param service servicio utilizado para implementar la lógica del negocio
     */
    public ProductController(ProductApplicationService service) {
        this.service = service;
    }

    // --- Implementación de métodos ---

    @Override
    @PostMapping
    public ResponseEntity<ProductResponseDTO> save(@Valid @RequestBody ProductRequestDTO dto) {
        log.info("Recibida petición para crear producto: {}", dto.name());
        ProductResponseDTO response = service.save(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Empieza desde la URL actual (/api/v1/products)
                .path("/{id}") // Añade el path variable del ID
                .buildAndExpand(response.id()) // Reemplaza {id} con el ID real
                .toUri();
        log.info("Producto creado con ID: {}. Respondiendo con 201 Created.", response.id());

        return ResponseEntity.created(location).body(response);// response 201
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable String id) {
        log.debug("Recibida petición para obtener producto por ID: {}", id);
        return service.findById(id)
                .map(dto -> {
                    log.debug("Producto encontrado con ID: {}", id);
                    return ResponseEntity.ok(dto); //response 200
                })
                .orElseGet(() -> {
                    log.warn("Producto NO encontrado con ID: {}", id);
                    return ResponseEntity.notFound().build();// response 404
                });
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        log.debug("Recibida petición para obtener todos los productos");
        List<ProductResponseDTO> response = service.findAll();
        log.debug("Encontrados {} productos.", response.size());
        return ResponseEntity.ok(response);// reponse 200
    }
}
