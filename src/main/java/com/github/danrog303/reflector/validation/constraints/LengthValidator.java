package com.github.danrog303.reflector.validation.constraints;

import com.github.danrog303.reflector.validation.Validator;
import lombok.RequiredArgsConstructor;

/**
 * Klasa świadcząca usługę walidacji dla adnotacji {@link Length}.
 */
@RequiredArgsConstructor
public class LengthValidator implements Validator {
    private final Length validatedAnnotation;
    private boolean isValid = false;

    @Override
    public void validate(Object value) {
        if (!String.class.isAssignableFrom(value.getClass())) {
            throw new IllegalStateException("@Length annotation is only applicable to String fields!");
        }

        int length = value.toString().length();
        isValid = length >= validatedAnnotation.minLength() && length <= validatedAnnotation.maxLength();
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
