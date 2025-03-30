package com.ecommerce.catalog.product.Application;

import com.ecommerce.catalog.product.Application.dto.AttributeDTO;
import com.ecommerce.catalog.product.Application.dto.request.ProductRequestDTO;
import com.ecommerce.catalog.product.Application.dto.response.ProductResponseDTO;
import com.ecommerce.catalog.product.Application.mapper.ProductMapper;
import com.ecommerce.catalog.product.Domain.constant.AttributeKey;
import com.ecommerce.catalog.product.Domain.model.Attribute;
import com.ecommerce.catalog.product.Domain.model.Product;
import com.ecommerce.catalog.product.Domain.repository.ProductRepository;
import com.ecommerce.catalog.sharedkernel.application.GenericService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio concreto para manejar la lógica del negocio de los objetos producto.
 */
@Service
public class ProductApplicationService implements GenericService<ProductRequestDTO, ProductResponseDTO, String> {
    //Permite acceder a los eventos que ocurran en sistema para obtener mayor información de los procesos.
    private static final Logger log = LoggerFactory.getLogger(ProductApplicationService.class);

    // Instancias de las dependencias del servicio.
    private final ProductRepository repository;
    private final ProductMapper mapper;

    /**
     * Constructor del servicio que permite la inyección de dependencias de la aplicación
     * @param repository repositorio utilizado para recoger/mandar datos a la base de datos.
     * @param mapper mapper de las entidades a sus respectivos DTOs.
     */
    public ProductApplicationService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // --- Métodos Públicos (Casos de Uso) ---

    @Override
    //@Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        log.info("Solicitud para crear producto: '{}'", request.name());
        //Validar todos los atributos y crear la instancia
        List<Attribute> validateAttributes = processAndValidateAttributes(request.attributes());
        Product product = new Product(
                request.name(),
                request.price(),
                request.quantity(),
                true,
                request.description(),
                validateAttributes
        );
        //Guardar la nueva entidad Product en la base de datos.
        Product temp = repository.save(product);
        log.info("Producto creado con ID: {}", temp.getId());
        //Mapea la entidad en el DTO de respuesta
        return mapper.toProductResponseDTO(temp);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findById(String id) {
        log.debug("Buscando producto por ID: {}", id);
        if(!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("El ID proporcionado no es válido. Debe tener 24 dígitos alfanuméricos.");
        }
        return repository.findById(new ObjectId(id))
                .map(mapper::toProductResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        log.info("Solicitud para consultar productos");
        return repository.findAll().stream()
                .map(mapper::toProductResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Procesa una lista de AttributeDTO, validando cada uno y convirtiéndolos
     * a Value Objects de dominio Attribute usando el método factoría.
     * Lanza IllegalArgumentException si alguna validación falla.
     * @param attributes La lista de DTOs de atributos (puede ser nula o vacía).
     * @return Una lista (posiblemente vacía) de objetos Attribute validados.
     */
    private List<Attribute> processAndValidateAttributes(List<AttributeDTO> attributes) {
        if(attributes == null || attributes.isEmpty()) {
            return Collections.emptyList();
        }
        // Itera, valida y mapea usando el método factoría estático de Attribute
        List<Attribute> validatedAttributes = new ArrayList<>();
        for(AttributeDTO dto : attributes) {
            AttributeKey key;
            String inputKey = dto.key();
            try{ //Valida que la clave exista
                key = AttributeKey.valueOf(inputKey.toUpperCase(Locale.ROOT).trim());
            } catch (IllegalArgumentException e) {
                log.error("Clave de atributo inválida recibida: '{}'", inputKey, e);
                throw new IllegalArgumentException("Key '" + inputKey + "' isn't valid");
            }

            try { //
                Attribute attribute = Attribute.create(key, dto.value());
                validatedAttributes.add(attribute);
            } catch (IllegalArgumentException e) {
                log.error("Error de validación de valor para el atributo '{}': {}", key, e.getMessage());
                throw new IllegalArgumentException("Attribute error '" + key + "': " + e.getMessage());
            }
        }
        return validatedAttributes;
    }
}
