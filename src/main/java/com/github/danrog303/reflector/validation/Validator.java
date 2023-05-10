package com.github.danrog303.reflector.validation;

/**
 * Klasy implementujące interfejs Validator świadczą usługę walidacji dla pojedynczej
 * adnotacji walidującej.
 */
public interface Validator {
    /**
     * Waliduje wskazaną wartość. Efekt walidacji jest zwracany przez metodę {@link #isValid()}.
     */
    void validate(Object value);

    /**
     * Zwraca efekt ostatniej walidacji (logiczna prawda, gdy walidacja przebiegła pomyślnie).
     */
    boolean isValid();

    /**
     * Zwraca wiadomość wygenerowaną przez usługę walidacji (np. komunikat o błędzie).
     */
    String getMessage();
}
