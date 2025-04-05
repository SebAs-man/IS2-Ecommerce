package com.ecommerce.ShoppingCart.cart.domain.exception;

public class ItemNotFoundInCartException extends RuntimeException{
    public ItemNotFoundInCartException(String itemId, String cartId) {
        super("Item: " + itemId + " not found in cart: " + cartId + ".");
    }
}
