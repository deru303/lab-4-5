package com.github.danrog303.reflector.validation.constraints;

import lombok.experimental.StandardException;

/**
 * Wyjątek rzucany przez {@link com.github.danrog303.reflector.validation.ValidationProcessor},
 * gdy pole klasy nie spełnia wszystkich założeń określanych przez adnotacje walidujące.
 */
@StandardException
public class ValidationConstraintsViolatedException extends Exception {
}
