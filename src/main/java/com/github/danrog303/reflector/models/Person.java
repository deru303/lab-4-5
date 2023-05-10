package com.github.danrog303.reflector.models;

import com.github.danrog303.reflector.validation.constraints.Length;
import com.github.danrog303.reflector.validation.constraints.RegexPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Przechowuje dane personalne hipotetycznej osoby.
 * Jeden z dwóch przykładowych Java Beanów służących do testowania mechanizmu refleksji.
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class Person implements Serializable {
    @Length(minLength = 3, maxLength = 128, message = "Imię musi mieć długość od 3 do 128.")
    @RegexPattern(regex = "^[A-Za-z ]*$", message = "Imię zawiera niedozwolone znaki.")
    private String name = "Default name";

    private int age = 18;

    private char gender = 'M';

    @RegexPattern(regex = "\\d{2}-\\d{3}", message = "Kod pocztowy musi być w formacie 00-000.")
    private String postalCode;
}
