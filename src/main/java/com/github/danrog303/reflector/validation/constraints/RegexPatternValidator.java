package com.github.danrog303.reflector.validation.constraints;

import com.github.danrog303.reflector.validation.Validator;
import lombok.RequiredArgsConstructor;

/**
 * Klasa świadcząca usługę walidacji dla adnotacji {@link RegexPattern}.
 */
@RequiredArgsConstructor
public class RegexPatternValidator implements Validator {
    private final RegexPattern validatedAnnotation;

    private boolean isValid;

    @Override
    public void validate(Object value) {
        if (!String.class.isAssignableFrom(value.getClass())) {
            throw new IllegalStateException("@RegexPattern annotation is only applicable to String fields!");
        }

        this.isValid = value.toString().matches(validatedAnnotation.regex());
    }

    @Override
    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public String getMessage() {
        return validatedAnnotation.message();
    }
}
