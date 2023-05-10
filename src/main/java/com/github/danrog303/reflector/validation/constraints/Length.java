package com.github.danrog303.reflector.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja pozwalająca określić minimalną i maksymalną długość dla pola tekstowego.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;
    String message() default "Pole nie spełnia wymagań dotyczących długości.";
}