package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MenuPresenter implements Initializable {

    public ComboBox<String> mapType;
    public TextField mapWidth;
    public TextField mapHeight;
    public TextField grassEnergy;
    public TextField copulationEnergy;
    public TextField animalEnergy;
    public TextField energyCost;
    public TextField animalInitNumber;
    public TextField grassEachDay;
    public TextField coolDown;
    public Button commitButton;
    public TextField grassAtStart;
    public TextField EnergyTaken;
    @FXML
    private javafx.scene.control.Label waterMapLabel;
    @FXML
    private TextField waterMapTextField;

    // wszystkie parametry z menu + id przycisku i wybrany typ mapy (jeszcze nie dodane)

    // pomysł --> stworzyć klase arguments, tam zapisac je i w ten sposob przekazywac
    public void makeNewWindow() throws Exception {
        String mapa = mapType.getValue();
        if ("Normal map".equals(mapa)){
            waterMapTextField.setText("0");
        }
        int waterNumber = Integer.parseInt(waterMapLabel.getText());
        int mapW = Integer.parseInt(mapWidth.getText());
        int mapH = Integer.parseInt(mapHeight.getText());
        int grassE = Integer.parseInt(grassEnergy.getText());
        int copulationE = Integer.parseInt(copulationEnergy.getText());
        int animalE = Integer.parseInt(animalEnergy.getText());
        int energyC = Integer.parseInt(energyCost.getText());
        int animalInitN= Integer.parseInt(animalInitNumber.getText());
        int grassEachD = Integer.parseInt(grassEachDay.getText());
        int coolD = Integer.parseInt(coolDown.getText());

        SimulationApp.simulationWindow(mapW, mapH, grassE,
                                    copulationE, animalE,energyC,
                                    animalInitN, grassEachD, coolD);
    }
    // to co ponizej to 90% chatgpt, ale dziala
    public void initialize(URL location, ResourceBundle resources) {
        mapType.setItems(FXCollections.observableArrayList("Normal map", "Water map"));

        mapType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Water map".equals(newValue)) {
                waterMapLabel.setVisible(true);
                waterMapTextField.setVisible(true);
            } else {
                waterMapLabel.setVisible(false);
                waterMapTextField.setVisible(false);
            }
        });
        waterMapTextField.setTextFormatter(createIntegerTextFormatter(10,1000)); // powinny byc inne ograniczenia
        mapWidth.setTextFormatter(createIntegerTextFormatter(100, 1000));
        mapHeight.setTextFormatter(createIntegerTextFormatter(30, 1000));
        grassEnergy.setTextFormatter(createIntegerTextFormatter(10, 1000));
        copulationEnergy.setTextFormatter(createIntegerTextFormatter(30, 1000));
        animalEnergy.setTextFormatter(createIntegerTextFormatter(50, 1000));
        energyCost.setTextFormatter(createIntegerTextFormatter(10, 1000));
        animalInitNumber.setTextFormatter(createIntegerTextFormatter(10, 1000));
        grassEachDay.setTextFormatter(createIntegerTextFormatter(5, 1000));
        coolDown.setTextFormatter(createIntegerTextFormatter(20, 1000));
    }

    private TextFormatter<Integer> createIntegerTextFormatter(int initialValue, int upperLimit) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), initialValue, integerFilter);

        textFormatter.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > upperLimit) {
                textFormatter.setValue(upperLimit);
            }
        });

        return textFormatter;
    }
}
