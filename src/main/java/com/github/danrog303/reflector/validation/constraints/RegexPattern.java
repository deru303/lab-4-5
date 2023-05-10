package com.github.danrog303.reflector.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja pozwalająca wymusić, aby zawartość pola tekstowego pasowała do wyrażenia regularnego.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegexPattern {
    String regex();
    String message() default "Pole nie spełnia wymagań określonych przez wyrażenie regularne.";
}