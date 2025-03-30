package com.ecommerce.catalog.config;

import com.ecommerce.catalog.sharedkernel.application.dto.ErrorResponse;
import com.ecommerce.catalog.sharedkernel.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // --- Manejadores Específicos ---

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, List<String>> validationErrors = new HashMap<>();
        // Itera sobre todos los errores de campo encontrados por @Valid
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error", // Error genérico de validación
                "La validación de la petición falló. Revisa los errores por campo.", // Mensaje genérico
                request.getDescription(false).replace("uri=", ""),
                validationErrors // Mapa con los detalles por campo
        );
        log.warn("Error de validación de DTO: {}", validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Devuelve 400
    }

    /**
     * Manejo de la excepción de IllegalArgumentException.
     * @param ex la excepción lanzada.
     * @return una respuesta http 400.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentAndState(
            RuntimeException ex, WebRequest request) {
        // Usado para validaciones de dominio fallidas (ej: createValidatedAttribute)
        // o estados incorrectos. Devuelve 400 Bad Request.
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(), // Mensaje de la excepción (ej: "Key inválida", "Precio debe ser positivo")
                request.getDescription(false).replace("uri=", "")
        );
        log.warn("Argumento/Estado ilegal: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Devuelve 400
    }

    /**
     * Manejo de la excepción de ResourceNotFoundException.
     * @param ex la excepción lanzada.
     * @return una respuesta http 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(), // Mensaje de nuestra excepción personalizada
                request.getDescription(false).replace("uri=", "") // Obtiene la URI de la petición
        );
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // Devuelve 404
    }

    /**
     * Maneja cualquier otra excepción no manejada antes.
     * @param ex excepción lanzada
     * @param request solicitud de request
     * @return 500 internal server error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        log.error("Error inesperado procesando la petición: {}", request.getDescription(false), ex);

        // Devuelve una respuesta genérica 500 Internal Server Error
        // NUNCA expongas detalles internos o stack traces al cliente en producción.
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error interno inesperado en el servidor.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
