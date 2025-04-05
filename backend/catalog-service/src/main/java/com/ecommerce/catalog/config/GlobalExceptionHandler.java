package com.ecommerce.catalog.config;

import com.ecommerce.catalog.sharedkernel.application.dto.ErrorResponse;
import com.ecommerce.catalog.sharedkernel.application.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Administrador global de excepciones para la API REST del microservicio de Catálogo.
 * Captura excepciones específicas y genéricas, devolviendo respuestas de error estandarizadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // --- Constantes ---
    private static final String INTERNAL_ERROR_MSG = "Ocurrió un error interno inesperado en el servidor.";
    private static final String CONFLICT_MSG = "La operación no pudo completarse debido a un conflicto de datos.";
    private static final String OPTIMISTIC_LOCK_MSG = "El recurso fue modificado concurrentemente. Intente la operación de nuevo.";
    private static final String VALIDATION_ERROR_MSG = "La validación de la petición falló.";

    // --- Administradores Específicos ---

    /**
     * Maneja {@code ResourceNotFoundException} y devuelve una respuesta JSON estandarizada
     * con detalles del error y un estado HTTP 404 Not Found.
     * @param ex la excepción lanzada cuando no se encuentra un recurso solicitado.
     * @param request la petición web actual durante la cual se produjo la excepción.
     * @return  una {@code ResponseEntity} que contiene una {@code ErrorResponse} con los detalles del error.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(), // Mensaje específico de la excepción
                getRequestPath(request)
        );
        log.warn("Recurso no encontrado [{}]: {}", getRequestPath(request), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones {@code IllegalArgumentException} y {@code IllegalStateException}.
     * Devolviendo una respuesta JSON estandarizada con detalles del error y un estado HTTP 400 Bad Request.
     * @param ex la excepción lanzada debido a un problema relacionado con un argumento o con un estado.
     * @param request la petición web actual durante la cual se produjo la excepción.
     * @return  una {@code ResponseEntity} que contiene una {@code ErrorResponse} con los detalles del error.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentAndState(
            RuntimeException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(), // Expone el mensaje de validación/error de negocio
                getRequestPath(request)
        );
        // Loguear como WARN porque usualmente es un error del cliente o de lógica previsible
        log.warn("Argumento/Estado ilegal [{}]: {}", getRequestPath(request), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja {@link DataIntegrityViolationException}, típicamente causadas por violaciones de las restricciones de la base de datos.
     * Devuelve una respuesta HTTP 409 Conflict.
     * @param ex la excepción lanzada debido a una violación de integridad de datos.
     * @param request la petición web actual en la que se produjo la excepción.
     * @return  una {@code ResponseEntity} que contiene una {@code ErrorResponse} con los detalles del error.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {

        // Podrías inspeccionar ex.getCause() para ver si es específicamente una DuplicateKeyException
        // y dar un mensaje más preciso, pero un 409 genérico suele ser suficiente.
        String specificMessage = CONFLICT_MSG + " Es posible que un identificador único ya exista.";
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                specificMessage,
                getRequestPath(request)
        );
        log.warn("Conflicto de integridad de datos [{}]: {}", getRequestPath(request), ex.getMessage()); // Loguea el mensaje original de la BD
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja {@link OptimisticLockingFailureException} y devuelve una respuesta JSON estandarizada
     * con detalles del error y un estado de Conflicto HTTP 409.
     * @param ex la excepción lanzada cuando se detecta un conflicto de bloqueo optimista.
     * @param request la petición web actual durante la cual se lanzó la excepción.
     * @return  una {@code ResponseEntity} que contiene una {@code ErrorResponse} con los detalles del error.
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockingFailure(
            OptimisticLockingFailureException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                OPTIMISTIC_LOCK_MSG, // Mensaje específico para el usuario
                getRequestPath(request)
        );
        log.warn("Fallo de bloqueo optimista [{}]: {}", getRequestPath(request), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja errores de validación de argumentos de método construyendo un {@code ErrorResponse} estructurado
     * y devolviendo una {@code ResponseEntity} con un estado HTTP 400 Bad Request.
     * Recoge y procesa los errores de validación para cada campo inválido de los detalles de la excepción.
     * @param ex la {@code MethodArgumentNotValidException} lanzada cuando ocurre un error de validación.
     * @param headers las cabeceras HTTP a incluir en la respuesta.
     * @param status el estado HTTP que se utilizará en la respuesta.
     * @param request la petición web actual durante la cual se produjo la excepción.
     * @return  una {@code ResponseEntity} que contiene la {@code ErrorResponse} con detalles sobre los errores de validación.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, List<String>> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                VALIDATION_ERROR_MSG,
                getRequestPath(request),
                validationErrors // Incluye los detalles por campo
        );
        log.warn("Error de validación de DTO [{}]: {}", getRequestPath(request), validationErrors);
        // Usamos handleExceptionInternal para encajar con la firma del método sobrescrito
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Maneja cualquier excepción inesperada durante la ejecución de la aplicación y proporciona
     * una respuesta de error JSON estandarizada con un estado HTTP 500 Internal Server Error.
     * @param ex la excepción que ocurrió durante el procesamiento de la solicitud.
     * @param request la petición web actual durante la cual se lanzó la excepción.
     * @return  una {@code ResponseEntity} que contiene una {@code ErrorResponse} con detalles del error
     * incluyendo el código de estado, el mensaje de error y la ruta de la petición.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Error inesperado [{}]: {}", getRequestPath(request), ex.getMessage(), ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                INTERNAL_ERROR_MSG, // Mensaje genérico para el cliente
                getRequestPath(request)
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Recupera la ruta de la petición de un {@code WebRequest} analizando la descripción de la petición.
     * Si la ruta no puede ser determinada, se pone por defecto «unknown».
     * @param request el {@code WebRequest} del que se extraerá la ruta.
     * @return  la ruta extraída como {@code String}, o «unknown» si se produce un error.
     */
    private String getRequestPath(WebRequest request) {
        try {
            // request.getDescription(false) suele devolver "uri=/path/to/resource"
            return request.getDescription(false).replace("uri=", "");
        } catch (Exception e) {
            return "unknown"; // Fallback
        }
    }
}
