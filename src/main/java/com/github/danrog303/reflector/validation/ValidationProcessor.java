package com.github.danrog303.reflector.validation;

import com.github.danrog303.reflector.validation.constraints.*;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa sprawdzająca jakie adnotacje walidujące są umieszczone na wskazanym polu klasy.
 * Na podstawie adnotacji ustalana jest poprawność/niepoprawność wskazanego pola.
 */
public class ValidationProcessor {
    /**
     * Na podstawie adnotacji umieszczonych nad podanym polem pobiera walidatory, których
     * należy użyć do zwalidowania podanego pola.
     */
    @SneakyThrows
    private List<Validator> getValidatorsForField(Field field) {
        List<Validator> result = new ArrayList<>();

        Length lengthAnnotation = field.getDeclaredAnnotation(Length.class);
        if (lengthAnnotation != null) {
            result.add(new LengthValidator(lengthAnnotation));
        }

        RegexPattern regexPatternAnnotation = field.getDeclaredAnnotation(RegexPattern.class);
        if (regexPatternAnnotation != null) {
            result.add(new RegexPatternValidator(regexPatternAnnotation));
        }

        return result;
    }

    /**
     * Na podstawie adnotacji walidujących sprawdza, czy wskazane pole jest poprawne.
     * @param field Pole klasy do zwalidowania
     * @param classInstance Instancja klasy do zwalidowania
     * @throws ValidationConstraintsViolatedException Gdy jedna z adnotacji zgłosiła błąd walidacji.
     */
    @SneakyThrows(IllegalAccessException.class)
    public void validateField(Field field, Object classInstance) throws ValidationConstraintsViolatedException {
        for (Validator validator : getValidatorsForField(field)) {
            field.setAccessible(true);
            validator.validate(field.get(classInstance));

            if (!validator.isValid()) {
                throw new ValidationConstraintsViolatedException(validator.getMessage());
            }
        }
    }
}
