package com.ecommerce.catalog.product.application;

import com.ecommerce.catalog.brand.domain.repository.BrandRepository;
import com.ecommerce.catalog.category.domain.model.Category;
import com.ecommerce.catalog.category.domain.repository.CategoryRepository;
import com.ecommerce.catalog.product.application.dto.request.*;
import com.ecommerce.catalog.product.application.dto.request.AttributeRequestDTO;
import com.ecommerce.catalog.product.application.dto.response.ProductResponseDTO;
import com.ecommerce.catalog.product.application.dto.response.VariantResponseDTO;
import com.ecommerce.catalog.product.application.mapper.ProductMapper;
import com.ecommerce.catalog.product.application.mapper.VariantMapper;
import com.ecommerce.catalog.product.application.exception.InvalidVariantAttributesException;
import com.ecommerce.catalog.product.domain.constant.AttributeType;
import com.ecommerce.catalog.product.domain.model.Product;
import com.ecommerce.catalog.product.domain.model.Variant;
import com.ecommerce.catalog.product.domain.model.vo.Attribute;
import com.ecommerce.catalog.product.domain.repository.ProductRepository;
import com.ecommerce.catalog.product.domain.repository.VariantRepository;
import com.ecommerce.catalog.sharedkernel.application.DeleteService;
import com.ecommerce.catalog.sharedkernel.application.ReadService;
import com.ecommerce.libs.application.exception.ResourceNotFoundException;
import com.ecommerce.libs.application.mapper.ValueObjectMapper;
import com.ecommerce.libs.application.util.IdGenerator;
import com.ecommerce.libs.domain.vo.Money;
import com.ecommerce.libs.domain.vo.NonBlankString;
import com.ecommerce.libs.domain.vo.NonNegativeInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Clase de servicio para gestionar operaciones relacionadas con productos.
 * Esta clase funciona como una capa de servicio de aplicación para gestionar
 * operaciones relacionadas con productos, como la obtención de detalles y la eliminación de productos.
 * Implementa las interfaces ReadService y DeleteService para proporcionar métodos genéricos de recuperación y eliminación.
 */
@Service
public class ProductService implements ReadService<ProductResponseDTO, String>, DeleteService<String> {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    // --- Dependencias ---
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final VariantMapper variantMapper;
    private final ValueObjectMapper valueObjectMapper;

    /**
     * Constructor con todas las dependencias del servicio.
     * @param productRepository la interfaz del repositorio para acceder a los datos de los productos.
     * @param variantRepository la interfaz del repositorio para acceder a los datos de las variantes.
     * @param brandRepository la interfaz del repositorio para acceder a los datos de las marcas.
     * @param categoryRepository la interfaz del repositorio para acceder a los datos de las categorías.
     * @param productMapper el asignador utilizado para convertir entre objetos de producto y DTO.
     * @param variantMapper el asignador utilizado para convertir entre objetos de variante y DTO.
     */
    public ProductService(ProductRepository productRepository, VariantRepository variantRepository,
                          BrandRepository brandRepository, CategoryRepository categoryRepository,
                          ProductMapper productMapper, VariantMapper variantMapper, ValueObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.variantMapper = variantMapper;
        this.valueObjectMapper = objectMapper;
    }

    // --- Métodos heredados ---

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> findById(String id) {
        log.debug("Buscando producto por ID: {}", id);
        return productRepository.findById(id)
                .map(productMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        log.debug("Buscando productos paginado: {}", pageable);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productMapper.toProductResponseDTOPage(productPage);
    }

    @Override
    @Transactional
    public void delete(String id) {
        log.info("Intentando eliminar producto ID: {}", id);
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product", "ID", id);
        }
        // Eliminar todas sus variantes existentes.
        Long exist = variantRepository.deleteByProductIdValue(id);
        // Eliminar el producto en sí.
        productRepository.deleteById(id);
        log.info("Producto eliminado ID: {}. Tenía {} variantes.", id, exist);
    }

    // --- Métodos funcionales ---

    /**
     * Busca productos por nombre y devuelve un resultado paginado.
     * @param name el nombre del producto o parte del nombre para filtrar los resultados.
     * @param pageable la información de paginación y ordenación.
     * @return una página de productos que coinciden con el nombre especificado, encapsulada en objetos {@code ProductResponseDTO}.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchByName(String name, Pageable pageable) {
        log.debug("Buscando productos por nombre que contengan '{}', paginado: {}", name, pageable);
        Page<Product> productPage = productRepository.findByNameValueContainingIgnoreCase(name, pageable);
        return productMapper.toProductResponseDTOPage(productPage);
    }

    /**
     * Busca productos por marcas y devuelve un resultado paginado.
     * @param id el identificador de la marca a filtrar.
     * @param pageable la información de paginación y ordenamiento.
     * @return una página de productos que coinciden con la marca especificada.
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchByBrand(String id, Pageable pageable) {
        log.debug("Buscando productos por marca: {}", id);
        if(!brandRepository.existsById(id)){
            throw new ResourceNotFoundException("Brand", "ID", id);
        }
        Page<Product> productPage = productRepository.findByBrandIdValue(id, pageable);
        return productMapper.toProductResponseDTOPage(productPage);
    }

    /**
     * Busca productos por categorías y devuelve un resultado paginado.
     * @param id el identificador de la categoría a filtrar.
     * @param pageable la información de paginación y ordenamiento.
     * @return una página de productos que coinciden con la categoría especificada
     */
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchByCategory(String id, Pageable pageable) {
        log.debug("Buscando productos por categoría: {}, paginado {}", id, pageable);
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Category", "ID", id);
        }
        List<String> categoriesId = categoryRepository.findByAncestors(id).stream()
                .map(Category::getId)
                .toList();
        log.debug("IDs de categoría encontrados: [{}]", categoriesId);
        Page<Product> productPage = productRepository.findByCategoriesIdIn(categoriesId, pageable);
        return productMapper.toProductResponseDTOPage(productPage);
    }

    /**
     * Busca las variantes de un producto en específico.
     * @param id el identificador del producto a filtrar.
     * @param pageable a información de paginación y ordenamiento.
     * @return una página de variantes que pertenecen a un producto.
     */
    @Transactional(readOnly = true)
    public Page<VariantResponseDTO> findVariantsByProductId(String id, Pageable pageable) {
        log.debug("Buscando variantes para producto ID {} paginado: {}", id, pageable);
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Product", "ID", id);
        }
        Page<Variant> variantPage = variantRepository.findByProductIdValue(id, pageable);
        return variantMapper.toProductResponseDTOPage(variantPage);
    }

    /**
     * Busca una variante en específico.
     * @param id el identificador de la variante.
     * @return La variante encontrada o un Optional.empty() si no se encontró la variante.
     */
    @Transactional(readOnly = true)
    public Optional<VariantResponseDTO> findVariantById(String id) {
        log.debug("Buscando variante por ID: {}", id);
        return variantRepository.findById(id)
                .map(variantMapper::toResponseDTO);
    }

    /**
     * Guarda un nuevo producto basándose en los datos de la solicitud.
     * @param request el objeto de transferencia de datos que contiene la información necesaria para crear un nuevo producto.
     * @return un {@code ProductResponseDTO} con los detalles del producto recién guardado.
     */
    @Transactional
    public ProductResponseDTO saveProduct(CreateProductRequestDTO request) {
        log.info("Creando producto: {}", request.name());
        // Validar las entidades obtenidas
        validateBrand(request.brandId());
        validateCategory(request.categoriesId());
        // Generar un identificador aleatorio para el producto
        String id = IdGenerator.generateId();
        log.debug("ID generado para el nuevo producto: {}", id);
        // Crear la entidad
        Product product = new Product(
                id,
                request.name(),
                request.description(),
                request.brandId(),
                request.categoriesId(),
                validateAttributes(request.attributeDefinitions())
        );
        // Guardar la nueva instancia
        Product savedProduct = productRepository.save(product);
        log.info("Producto base creado con ID {}", savedProduct.getId());
        // Crear la variante inicial
        try{
            Variant variant = createVariantInternal(savedProduct, request.initialVariant());
        } catch (DataIntegrityViolationException | InvalidVariantAttributesException | IllegalArgumentException | NullPointerException e){
            throw new RuntimeException("Error al crear la variante inicial requerida: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al crear la variante inicial.", e);
        }
        return productMapper.toResponseDTO(savedProduct);
    }

    /**
     * Actualiza un producto existente utilizando los datos de la solicitud.
     * @param id El identificador del producto a modificar.
     * @param request Los datos a modificar del producto.
     * @return Un objeto de transferencia de datos de tipo response que tiene los datos actualizados del producto.
     */
    @Transactional
    public Optional<ProductResponseDTO> updateProduct(String id, UpdateProductRequestDTO request) {
        log.info("Actualizando datos base del producto ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));
        // Validar las entidades obtenidas
        validateBrand(request.brandId());
        validateCategory(request.categoriesId());
        // Modificar la instancia
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBrandId(request.brandId());
        product.setCategoriesId(request.categoriesId());
        // Guardar la instancia modificada
        Product savedProduct = productRepository.save(product);
        log.info("Producto base actualizado ID {}", savedProduct.getId());
        return Optional.of(productMapper.toResponseDTO(savedProduct));
    }

    /**
     * Guarda una nueva variante basándose en los datos de la solicitud.
     * @param id el identificador único del producto al que pertenecerá la variante.
     * @param request el objeto de transferencia de datos que contiene la información necesaria para crear una nueva variante.
     * @return un {@code VariantResponseDTO} con los detalles de la variante recién guardada.
     */
    @Transactional
    public VariantResponseDTO saveVariant(String id, CreateVariantRequestDTO request) {
        log.info("Creando nueva variante para producto ID: {}",  id);
        String productId = new NonBlankString(id).value();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));
        Variant savedVariant = createVariantInternal(product, request);
        log.info("Variante creada con ID {}", savedVariant.getId());
        return variantMapper.toResponseDTO(savedVariant);
    }

    /**
     * Actualiza los detalles de una variante existente utilizando el ID proporcionado y los datos de la solicitud de actualización.
     * @param variantId el identificador único de la variante que se actualizará.
     * @param request los datos que contienen los valores actualizados de la variante.
     * @return un objeto de transferencia de datos (DTO) que representa los detalles actualizados de la variante.
     */
    public Optional<VariantResponseDTO> updateVariant(String variantId, UpdateVariantRequestDTO request) {
        log.info("Actualizando variante ID: {}", variantId);
        String id = new NonBlankString(variantId).value();
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variant", "ID", id));
        Money price = valueObjectMapper.moneyDTOToMoney(request.price());
        Integer stock = new NonNegativeInteger(request.stock()).value();
        // Modificar la instancia
        variant.setPrice(price);
        variant.setStock(stock);
        variant.setImages(request.images());
        variant.setAvailable(request.available());
        // Guardar la instancia modificada
        Variant savedVariant = variantRepository.save(variant);
        log.info("Variante actualizada ID {}", id);
        return Optional.of(variantMapper.toResponseDTO(savedVariant));
    }

    @Transactional
    public void deleteVariant(String id) {
        log.info("Eliminando variante ID: {}", id);
        if(!variantRepository.existsById(id)){
            throw new ResourceNotFoundException("Variant", "ID", id);
        }
        variantRepository.deleteById(id);
        log.info("Variante eliminada ID: {}", id);
    }

    // --- Métodos auxiliares ---

    /**
     * Valida y mapea la lista de definiciones de atributos de DTO a objetos de Dominio.
     * Asegura que las claves sean únicas y que los defaults sean válidos para el tipo.
     * @param attributes Lista de DTOs de definición desde la petición.
     * @return Lista de objetos ProductAttributeDefinition de dominio.
     */
    private List<Attribute> validateAttributes(List<AttributeRequestDTO> attributes) {
        Set<String> keys = new HashSet<>();
        List<Attribute> definitions = new ArrayList<>();
        for (AttributeRequestDTO attribute : attributes) {
            // Validar que no estén repetidas
            String key = new NonBlankString(attribute.key()).value();
            if(!keys.add(key.toLowerCase(Locale.ROOT))){
                throw new IllegalArgumentException("Duplicate key: " + attribute.key());
            }
            Attribute definition = new Attribute(
                    new NonBlankString(attribute.key()),
                    new NonBlankString(attribute.label()),
                    attribute.type(),
                    attribute.isVariantOption(),
                    attribute.isRequired(),
                    attribute.defaultValue()
            );
            definitions.add(definition);
        }
        // La validación del tipo de dato se hace en el dominio.
        return definitions;
    }

    /**
     * Valida los atributos definitorios de una variante contra el schema del producto y crea la variante.
     * @param product representa una instancia existente de un producto
     * @param dto representa un DTO de tipo request con los datos para crear una nueva variante.
     */
    private Variant createVariantInternal(Product product, CreateVariantRequestDTO dto) {
        // Normalizar las claves de los atributos de la variante
        Map<String, Object> normalizedAttributes = dto.attributes() == null ? Collections.emptyMap() :
        dto.attributes().entrySet().stream()
                .filter(e -> e.getKey() != null && !e.getKey().isBlank())
                .collect(Collectors.toMap(
                        e -> e.getKey().trim().toLowerCase(Locale.ROOT),
                        Map.Entry::getValue,
                        (v1, v2) -> v1 // En caso de claves duplicadas normalizadas
                ));
        // Verificar que los atributos de la variante su cumplan con el esquema del producto.
        Map<String, Object> attributes = validateVariantDefiningAttributes(
                normalizedAttributes, product.getAttributeDefinitions()
        );
        // Obtener los campos faltantes
        Money price = valueObjectMapper.moneyDTOToMoney(dto.price());
        Integer stock = new NonNegativeInteger(dto.stock()).value();
        String id = IdGenerator.generateId();
        // Crear la instancia
        Variant variant = new Variant(
                id,
                product.getId(),
                price,
                stock,
                dto.images(),
                attributes
        );
        // Guardar la instancia
        return variantRepository.save(variant);
    }

    /**
     * Valida los atributos definitorios de una variante contra el schema del producto.
     * @param proposedAttributes Propuesta de mapa de pares (clave-valor) definidos por la variante.
     * @param schemaDefinitions Esquema de atributos definidos en el producto.
     * @return Un mapa de pares (clave-valor) con los atributos válidos del esquema.
     */
    private Map<String, Object> validateVariantDefiningAttributes(
            Map<String, Object> proposedAttributes, List<Attribute> schemaDefinitions) {
        List<Attribute> schema = schemaDefinitions == null ? Collections.emptyList() : schemaDefinitions;
        Map<String, Object> inputAttrs = proposedAttributes == null ? Collections.emptyMap() : proposedAttributes;
        // Convertir schema a mapa para búsqueda rápida por clave normalizada
        final Map<String, Attribute> schemaMap = schema.stream()
                .collect(Collectors.toMap(
                        def -> def.getKey().value(), // Clave ya normalizada en la definición
                        Function.identity()
                ));
        Map<String, Object> attributesToStore = new HashMap<>();
        // Validar cada atributo propuesto por el cliente
        for(Map.Entry<String, Object> entry : inputAttrs.entrySet()) {
            String key = entry.getKey().toLowerCase(Locale.ROOT);
            Object value = Objects.requireNonNull(entry.getValue(), "Value for attribute key: {" + key + "} cannot be null.");
            // ¿Clave definida en el schema?
            Attribute attribute = schemaMap.get(key);
            if(attribute == null) {
                throw new InvalidVariantAttributesException("Key {" + key + "} not defined in schema definitions");
            }
            // ¿Tipo de valor correcto?
            AttributeType attributeType = attribute.getType();
            try{
                attributeType.validate(value);
            } catch (Exception e){
                throw new InvalidVariantAttributesException("Value {" + key + "} not defined in schema definitions");
            }
            // Decidir si guardar ese atributo en la variante
            if(attribute.getIsVariantOption()){
                // Siempre se guarda si es una opción que define la variante
                attributesToStore.put(key, value);
            } else {
                Object defaultValue = attribute.getDefaultValue();
                if(!Objects.equals(defaultValue, value)){
                    // Es un override del valor, por lo tanto se guarda
                    attributesToStore.put(key, value);
                }
            }
        }
        // Verificar que todos los atributos requeridos tengan un valor
        for(Attribute attribute : schema) {
            if(attribute.getIsRequired()){
                String requiredKey = attribute.getKey().value();
                // ¿Tiene un valor específico guardado O tiene un valor por defecto definido?
                boolean hasValue = attributesToStore.containsKey(requiredKey) || attribute.getDefaultValue() != null;
                if(!hasValue){
                    throw new InvalidVariantAttributesException(
                            "Attribute key {" + requiredKey + "} is required and not defined in schema definitions");
                }
            }
        }
        log.debug("Atributos validados para guardar en variante: {}", attributesToStore);
        return attributesToStore;
    }

    /**
     * Valida que la marca obtenida por el request exista.
     * @param brandId una cadena de caracteres con el identificador de la marca.
     */
    private void validateBrand(String brandId) {
        String id = new NonBlankString(brandId).value();
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand", "ID", id);
        }
    }

    /**
     * Valida que las categorías obtenidas por el request existan.
     * @param categoriesId lista de identificadores de categorías.
     */
    private void validateCategory(List<String> categoriesId) {
        if (categoriesId == null || categoriesId.isEmpty()) {
            throw new IllegalArgumentException("Is required at least one category");
        }
        for (String categoryId : categoriesId) {
            String id = new NonBlankString(categoryId).value();
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("Category", "ID", id);
            }
        }
    }
}
