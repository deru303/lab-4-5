package com.github.danrog303.reflector.fx;

import com.github.danrog303.reflector.validation.ValidationProcessor;
import com.github.danrog303.reflector.validation.constraints.ValidationConstraintsViolatedException;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Klasa realizująca obustronną konwersję pomiędzy obiektami Java Bean
 * a formularzami składającymi się z kontrolek JavaFX.
 */
public class ReflectionFxMapper {
    /**
     * Instancja klasy, dla której generowany jest formularz JavaFX.
     */
    private Object instance;

    /**
     * Obiekt pozwalający na dopisywanie nowych logów do obiektu TextArea zdefiniowanego w głównym widoku aplikacji.
     */
    private final FxLogger fxLogger;

    /**
     * Obiekt odpowiedzialny za przetworzenie adnotacji walidujących obiekty Java Bean.
     */
    private final ValidationProcessor validationProcessor = new ValidationProcessor();

    public ReflectionFxMapper(FxLogger fxLogger) {
        this.fxLogger = fxLogger;
    }

    /**
     * Przetwarza nazwę klasy na formularz JavaFX.
     * @param classCanonicalName Pełna nazwa klasy, na przykład com.github.danrog303.reflector.models.Person
     * @return Lista kontrolek JavaFX umożliwiających edytowanie pól klasy.
     */
    @SneakyThrows
    public List<VInputText> mapClassNameToFxControls(String classCanonicalName) throws ClassNotFoundException {
        List<VInputText> result = new ArrayList<>();
        Class<?> classObject = Class.forName(classCanonicalName);
        instance = classObject.getConstructor().newInstance();

        for (Field classField : classObject.getDeclaredFields()) {
            VInputText inputBox = new VInputText();
            Optional<Node> fieldEditControl = getFieldEditControl(classField);

            fieldEditControl.ifPresent(value -> {
                inputBox.setUserInputControl(value);
                inputBox.setFieldName(classField.getName());
                result.add(inputBox);
            });
        }

        return result;
    }

    /**
     * Przetwarza obiekt Field (pochodzący z Java Reflection API) na obiekt
     * Control (obiekt JavaFX umożliwiający edycję danego pola).
     */
    @SneakyThrows
    private Optional<Node> getFieldEditControl(Field field) {
        String getterName;
        if (boolean.class.isAssignableFrom(field.getType())) {
            getterName = "is" + StringUtils.capitalize(field.getName());
        } else {
            getterName = "get" + StringUtils.capitalize(field.getName());
        }

        Method getter = instance.getClass().getMethod(getterName);
        try {
            if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
                CheckBox checkBox = new CheckBox();
                Boolean selected = (Boolean) getter.invoke(instance);
                if (selected != null && selected) {
                    checkBox.setSelected(true);
                }
                return Optional.of(checkBox);
            }
            else {
                return Optional.of(new TextField(Objects.toString(getter.invoke(instance), "")));
            }
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Przetwarza podane kontrolki JavaFX i na podstawie ich zawartości konstruuje obiekt wskazanej nazwy.
     * @param userInput
     * @param classCanonicalName
     */
    @SneakyThrows
    public Object mapFxControlsToClassInstance(List<VInputText> userInput, String classCanonicalName) {
        Class<?> classObject = Class.forName(classCanonicalName);

        for (VInputText userInputBox : userInput) {
            Pane userInputContainer = (Pane) userInputBox.getChildren().get(1);
            Node fieldEditControl = userInputContainer.getChildren().get(0);

            Label fieldNameLabel = (Label) userInputBox.getChildren().get(2);
            Field classField = classObject.getDeclaredField(fieldNameLabel.getText());

            try {
                applyUserInput(instance, classField, fieldEditControl);
                validationProcessor.validateField(classField, instance);
                userInputBox.setValidationIcon(true, "Pole jest poprawne");
            } catch(ValidationConstraintsViolatedException e) {
                fxLogger.log("Pole: '%s', błąd walidacji adnotacji: %s".formatted(classField.getName(), e.getMessage()));
                userInputBox.setValidationIcon(false, e.getMessage());
            } catch(Exception e) {
                fxLogger.log("Pole '%s', wyjątek: %s".formatted(classField.getName(), e.getClass().getSimpleName()));
                userInputBox.setValidationIcon(false, e.getMessage());
            }
        }

        return instance;
    }

    /**
     * Na podstawie danych wpisanych przez użytkownika do kontrolki JavaFX, wywołuje odpowiedni
     * setter na instancji klasy.
     */
    private void applyUserInput(Object instance, Field classField, Node fieldEditControl) throws Exception {
        String setterName = "set" + StringUtils.capitalize(classField.getName());
        Method setter = instance.getClass().getMethod(setterName, classField.getType());

        if (Boolean.class.isAssignableFrom(classField.getType()) || boolean.class.isAssignableFrom(classField.getType())) {
            CheckBox booleanInput = (CheckBox) fieldEditControl;
            setter.invoke(instance, booleanInput.isSelected());
        }
        else if (Character.class.isAssignableFrom(classField.getType()) || char.class.isAssignableFrom(classField.getType())) {
            TextField textInput = (TextField) fieldEditControl;
            setter.invoke(instance, textInput.getText().charAt(0));
        }
        else if (String.class.isAssignableFrom(classField.getType())) {
            TextField textInput = (TextField) fieldEditControl;
            setter.invoke(instance, textInput.getText());
        }
        else {
            TextField textInput = (TextField) fieldEditControl;
            Class<?> fieldType = classField.getType();
            if (fieldType.isPrimitive()) {
                fieldType = ClassUtils.primitiveToWrapper(fieldType);
            }

            Method valueOfMethod = fieldType.getMethod("valueOf", String.class);
            setter.invoke(instance, valueOfMethod.invoke(null, textInput.getText()));
        }
    }
}
