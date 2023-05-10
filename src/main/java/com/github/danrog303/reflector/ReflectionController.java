package com.github.danrog303.reflector;

import com.github.danrog303.reflector.fx.FxLogger;
import com.github.danrog303.reflector.fx.ReflectionFxMapper;
import com.github.danrog303.reflector.fx.VInputText;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class ReflectionController {
    public @FXML VBox objectEditBox;
    public @FXML TextArea logTextArea;
    public @FXML TextField classNameInput;

    private String currentlyChosenClassName = null;
    private final FxLogger fxLogger = (msg) -> logTextArea.appendText(msg + "\n");
    private final ReflectionFxMapper reflectionMapper = new ReflectionFxMapper(fxLogger);

    /**
     * Metoda wywoływana, gdy użytkownik wciśnie przycisk wygenerowania formularza dla podanej klasy.
     */
    public void onCreateObject(ActionEvent actionEvent) {
        this.currentlyChosenClassName = classNameInput.getText();
        this.logTextArea.clear();

        try {
            List<VInputText> objectEditFields = reflectionMapper.mapClassNameToFxControls(this.currentlyChosenClassName);
            objectEditBox.getChildren().clear();
            objectEditBox.getChildren().addAll(objectEditFields);
        } catch(ClassNotFoundException e) {
            this.fxLogger.log("Class not found: %s\n".formatted(this.currentlyChosenClassName));
        }
    }

    /**
     * Metoda wywoływana, gdy użytkownik wciśnie przycisk zapisywania obiektu.
     */
    public void onSaveObject(ActionEvent actionEvent) {
        this.logTextArea.clear();

        List<VInputText> userInput = objectEditBox.getChildren()
                .stream()
                .filter(VInputText.class::isInstance)
                .map(fxNode -> (VInputText) fxNode)
                .collect(Collectors.toList());

        Object instance = reflectionMapper.mapFxControlsToClassInstance(userInput, this.currentlyChosenClassName);
        this.fxLogger.log(instance.toString());
    }
}
