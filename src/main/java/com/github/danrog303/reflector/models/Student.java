package com.github.danrog303.reflector.models;

import com.github.danrog303.reflector.validation.constraints.Length;
import com.github.danrog303.reflector.validation.constraints.RegexPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
  * Przechowuje dane personalne hipotetycznego studenta.
  * Jeden z dwóch przykładowych Java Beanów służących do testowania mechanizmu refleksji.
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class Student implements Serializable {
    @RegexPattern(regex="^[A-Za-z]*$", message="Niedozwolone znaki w imieniu.")
    @Length(minLength=1, message="Imię musi mieć minimum 1 znak.")
    private @NonNull String name;

    @RegexPattern(regex="^[A-Za-z\\']*$", message="Niedozwolone znaki w nazwisku.")
    @Length(minLength=2, message="Nazwisko musi mieć minimum 2 znaki.")
    private @NonNull String surname;

    @RegexPattern(regex="\\d{6}", message="Numer indeksu składa się z 6 cyfr.")
    private String indexNumber;

    private boolean passed = true;
}
