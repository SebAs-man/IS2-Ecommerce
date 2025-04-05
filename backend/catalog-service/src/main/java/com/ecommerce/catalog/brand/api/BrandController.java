package com.ecommerce.catalog.brand.api;

import com.ecommerce.catalog.brand.application.BrandService;
import com.ecommerce.catalog.brand.application.dto.request.BrandRequestDTO;
import com.ecommerce.catalog.brand.application.dto.response.BrandResponseDTO;
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
 * Controlador REST que gestiona las operaciones relacionadas con las marcas
 * del catálogo, proporcionando los endpoints necesarios para crear, leer,
 * actualizar y eliminar marcas. Este controlador también implementa funcionalidades
 * específicas como la búsqueda de marcas por nombre.
 */
@RestController
@RequestMapping("/brands")
public class BrandController implements ReadController<BrandResponseDTO, String>, DeleteController<String> {
    // --- Dependencias ---
    private final BrandService service;

    /**
     * Construye un nuevo BrandController con el BrandService especificado.
     * @param service el servicio responsable de ejecutar la lógica de negocio
     * relacionada con las marcas dentro del catálogo. No debe ser null.
     */
    public BrandController(BrandService service) {
        this.service = service;
    }

    // --- EndPoints heredados ---

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping(params = "!name") // Se activa solo si NO existe el parámetro 'name'
    public ResponseEntity<Page<BrandResponseDTO>> getAll(Pageable pageable) {
        Page<BrandResponseDTO> brandPage = service.findAll(pageable);
        return ResponseEntity.ok(brandPage);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- EndPonts funcionales ---

    /**
     * Recupera una lista paginada de marcas que coinciden con el nombre dado.
     * Este método sólo se activa cuando se proporciona el parámetro «name».
     * @param name el nombre de la(s) marca(s) a buscar
     * @param pageable la información de paginación y ordenación
     * @return  una ResponseEntity que contiene una Page de instancias BrandResponseDTO
     * que coinciden con el nombre dado
     */
    @GetMapping(params = "name")// Se activa solo si existe el parámetro 'name'
    public ResponseEntity<Page<BrandResponseDTO>> searchByName(
            @RequestParam String name, Pageable pageable) {
        Page<BrandResponseDTO> response = service.searchByName(name, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Gestiona la creación de una nueva marca en el sistema procesando los datos de la solicitud.
     * La información de la marca creada se devuelve una vez creada con éxito, junto con un URI de ubicación.
     * @param brandRequestDTO los detalles de la marca a crear, incluyendo nombre, descripción,
     * y URL del logotipo. Debe ser un objeto de solicitud válido.
     * @return  una ResponseEntity que contiene los detalles de la marca creada envueltos en un objeto
     * BrandResponseDTO, junto con el URI de ubicación de la entidad creada.
     */
    @PostMapping
    public ResponseEntity<BrandResponseDTO> createBrand(
            @Valid @RequestBody BrandRequestDTO brandRequestDTO) {
        BrandResponseDTO response = service.save(brandRequestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.name()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Actualiza los detalles de una marca existente identificada por su ID.
     * Si la marca existe, se actualizará con los detalles proporcionados;
     * En caso contrario, se devolverá una respuesta 404 (No encontrado).
     * @param id el identificador único de la marca a actualizar
     * @param request los detalles de la marca a actualizar, encapsulados en un objeto BrandRequestDTO.
     * Debe ser una solicitud válida que contenga los campos necesarios.
     * @return a ResponseEntity que contiene los detalles de la marca actualizada encapsulados en un objeto BrandResponseDTO,
     * o una respuesta 404 (Not Found) si la marca no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BrandResponseDTO> updateBrand(
            @PathVariable String id, @Valid @RequestBody BrandRequestDTO request){
        return service.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
