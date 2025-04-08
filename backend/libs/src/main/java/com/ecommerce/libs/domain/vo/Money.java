package com.ecommerce.libs.domain.vo;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Representa una cantidad monetaria emparejada con una moneda específica.
 * Esta clase asegura la inmutabilidad y maneja operaciones monetarias comunes.
 */
public record Money(BigDecimal amount, Currency currency) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // --- Campos constantes ---
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;

    /**
     * Constructor canónico. Valida que los argumentos no sean nulos.
     */
    public Money {
        Objects.requireNonNull(amount, "Value cannot be null.");
        Objects.requireNonNull(currency, "Currency cannot be null.");
        // Redondea la cantidad a la escala por defecto de la moneda para consistencia interna
        amount = amount.setScale(currency.getDefaultFractionDigits(), DEFAULT_ROUNDING);
    }

    /**
     * Crea una instancia de Money con cantidad cero para una moneda dada.
     * @param currency La moneda.
     * @return Money con valor cero.
     */
    public static Money Zero(Currency currency){
        return new Money(BigDecimal.ZERO, currency);
    }

    /**
     * Crea una instancia de Money a partir de un código de moneda ISO 4217.
     * @param amount La cantidad.
     * @param currencyCode El código ISO (ej: "COP", "USD").
     * @return Nueva instancia de Money.
     * @throws IllegalArgumentException si el código de moneda no es válido.
     */
    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }

    // --- Métodos aritméticos ---

    /**
     * Suma otro objeto Money a este. Lanza excepción si las monedas no coinciden.
     * @param other El otro Money a sumar.
     * @return Un nuevo objeto Money con el resultado de la suma.
     * @throws IllegalArgumentException si las monedas son diferentes.
     */
    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Resta otro objeto Money de este. Lanza excepción si las monedas no coinciden.
     * @param other El otro Money a restar.
     * @return Un nuevo objeto Money con el resultado de la resta.
     * @throws IllegalArgumentException si las monedas son diferentes.
     */
    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Multiplica la cantidad monetaria por un multiplicador entero especificado.
     * @param multiplier: el valor entero por el que se multiplicará la cantidad monetaria.
     * @return una nueva instancia de {@code Money} que contiene el resultado de la multiplicación,
     * con la misma moneda que la instancia original.
     */
    public Money multiply(int multiplier) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }

    /**
     * Multiplica la cantidad monetaria con un multiplicador {@code BigDecimal} especificado.
     * @param multiplier el valor {@code BigDecimal} por el que se multiplicará la cantidad monetaria.
     * @return una nueva instancia de {@code Money} que contiene el resultado de la multiplicación,
     * con la misma moneda que la instancia original.
     */
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier), this.currency);
    }

    // --- Métodos comparables ---

    /**
     * Determina si la cantidad monetaria es cero.
     *
     * @return {@code true} si el importe monetario es igual a cero; {@code false} en caso contrario.
     */
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Determina si la cantidad monetaria es positiva.
     *
     * @return true si el importe es mayor que cero, false en caso contrario.
     */
    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Compara la instancia actual de Money con otra instancia de Money para determinar si la cantidad
     * del Dinero actual es mayor que la cantidad del Dinero proporcionado.
     * Se asegura de que ambas instancias de dinero tengan la misma moneda antes de realizar la comparación.
     * @param other La otra instancia de dinero con la que comparar.
     * @return  {@code true} si el importe del Dinero actual es mayor que el importe del Dinero dado;
     * {@code false} en caso contrario.
     */
    public boolean isGreaterThan(Money other) {
        requireSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    // --- Métodos funcionales ---

    /**
     * Asegura que la instancia {@code Money} dada tiene la misma moneda que la instancia actual.
     * Lanza una excepción si las monedas difieren.
     * @param other La otra instancia {@code Money} para comprobar la igualdad de divisas.
     * @throws NullPointerException si la instancia {@code Money} proporcionada es nula.
     * @throws IllegalArgumentException si las monedas de las dos instancias {@code Money} no coinciden.
     */
    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other);
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Cannot perform operation on Money with different currencies: "
                            + this.currency + " and " + other.currency);
        }
    }



    // --- Métodos heredados ---

    @Override
    public String toString() {
        // Considera usar NumberFormat para formateo específico de moneda local si es necesario
        return amount.toString() + " " + currency.getCurrencyCode();
    }
}
