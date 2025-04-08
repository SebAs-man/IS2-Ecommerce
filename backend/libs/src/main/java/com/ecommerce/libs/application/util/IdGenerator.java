package com.ecommerce.libs.application.util;

import com.github.f4b6a3.ulid.Ulid;

/*
 * Generador de ID únicos basado en la librería NanoID.
 * Proporciona ID cortos, seguros para URL y con baja probabilidad de colisión.
 * Es un componente de Spring para poder ser inyectado en los servicios.
 */
public final class IdGenerator {
    /**
     * Constructor privado para evitar la instanciación de la clase IdGenerator.
     * Esta clase está diseñada para ser utilizada como una utilidad y no debe ser instanciada.
     */
    private IdGenerator() {}

    /**
     * Genera un identificador único basado en la especificación ULID.
     * El ID generado está en minúsculas.
     * @return  un identificador único, cadena en minúsculas.
     */
    public static String generateId() {
        return Ulid.fast().toLowerCase();
    }
}
