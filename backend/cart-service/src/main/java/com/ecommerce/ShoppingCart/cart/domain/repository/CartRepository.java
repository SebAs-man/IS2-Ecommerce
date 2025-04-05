package com.ecommerce.ShoppingCart.cart.domain.repository;

import com.ecommerce.ShoppingCart.cart.domain.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para realizar operaciones CRUD relacionadas con el carrito de compras.
 * Proporciona métodos personalizados para consultas basadas en el ID del usuario.
 */
@Repository
public interface CartRepository extends CrudRepository<Cart, String> {
    /**
     * Recupera un carrito asociado a un ID de usuario específico.
     * @param userId el identificador único del usuario cuyo carrito se desea recuperar
     * @return un Optional que contiene el carrito si se encuentra, o un Optional vacío si no existe ningún carrito para el ID de usuario proporcionado
     */
    Optional<Cart> findByUserId(String userId);
}
