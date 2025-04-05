package com.ecommerce.catalog.category.domain.exception;

/**
 * Excepción lanzada para indicar que una categoría no puede ser eliminada porque tiene subcategorías asociadas.
 * Esta excepción extiende {@link RuntimeException} y se usa específicamente para señalar situaciones
 * cuando una operación falla debido a la existencia de subcategorías bajo una categoría que está siendo
 * para su eliminación.
 */
public class CategoryNotEmptyException extends RuntimeException {
    public CategoryNotEmptyException(String categoryId) {
        super("No es posible eliminar la categoría " + categoryId + " porque tiene subcategorías asociadas.");
    }
}
