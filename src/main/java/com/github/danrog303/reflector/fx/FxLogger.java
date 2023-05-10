package com.github.danrog303.reflector.fx;

import javafx.scene.control.TextArea;

/**
 * Pozwala klasom spoza kontrolera na dopisywanie nowych logów do obiektu {@link TextArea}
 * zdefiniowanego w głównym widoku aplikacji.
 */
public interface FxLogger {
    /**
     * Dodaje wskazaną wiadomość do logów wyświetlanych w głównym widoku aplikacji.
     */
    void log(String msg);
}
