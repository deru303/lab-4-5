package com.github.danrog303.reflector.fx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Kontrolka odpowiedzialna za wyświetlenie pojedynczego pola klasy w formularzu edycji obiektu.
 * Kontrolka wyświetla nazwę pola, ikonę efektu walidacji oraz pole input dla użytkownika.
 */
public class VInputText extends HBox {
    @FXML private ImageView validationIcon;
    @FXML private Label fieldNameLabel;
    @FXML private VBox userInputContainer;

    public void setValidationIcon(boolean isFieldValid, String iconTooltip) {
        String iconName = isFieldValid ? "icon-valid.png" : "icon-invalid.png";
        InputStream iconStream = Objects.requireNonNull(getClass().getResourceAsStream(iconName));
        validationIcon.setImage(new Image(iconStream));
        Tooltip.install(validationIcon, new Tooltip(iconTooltip));
    }

    public void setUserInputControl(Node node) {
        this.userInputContainer.getChildren().clear();
        this.userInputContainer.getChildren().add(node);
    }

    public void setFieldName(String fieldName) {
        this.fieldNameLabel.setText(fieldName);
    }

    public VInputText() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("v-input-text.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}