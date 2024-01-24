package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.simulation.Arguments;
import agh.ics.oop.model.map.RectangularMap;
import agh.ics.oop.model.map.IMap;
import agh.ics.oop.model.simulation.Simulation;
import agh.ics.oop.model.map.WaterMap;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class MenuPresenter implements Initializable {

    public TextField mapWidth;
    public TextField mapHeight;
    public ComboBox<String> mapType;
    public TextField grassAtStart;
    public TextField grassEnergy;
    public TextField grassEachDay;
    public TextField animalInitNumber;
    public TextField animalEnergy;
    public TextField energyCost;
    public TextField copulationEnergy;
    public TextField energyTaken;
    public TextField minMut;
    public TextField maxMut;
    public TextField genomLen;
    public ComboBox<String> variant;
    public TextField coolDown;
    public Button commitButton;
    public TextField waterMapDaysTextField;
    public Label waterMapDaysLabel;
    public Label waterMapPercentageLabel;
    public TextField waterMapPercentageTextField;
    public ComboBox<String> preset;
    public TextField presetName;
    public Button editSet;
    public Button saveSet;
    public Button deleteButton;
    public CheckBox dataCheckBox;
    @FXML
    private TextField waterMapTextField;
    private boolean state;

    public void startSimulation() throws Exception {
        Arguments args = gatherArguments();

        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SimulationApp.class.getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.getController();
        presenter.setPrimaryStage(primaryStage);

        IMap map;
        if ("Normal map".equals(args.mapType()))
            map = new RectangularMap(args, presenter);
        else
            map = new WaterMap(args, presenter);
        dataCheckBox.setOnAction(e -> {
            state = dataCheckBox.isSelected();
        });
        presenter.setWorldMap(map);
        configureStage(primaryStage, viewRoot);
        Simulation simulation = new Simulation(args.coolDown(), args.grassEachDay(), map,state);
        presenter.setSimulation(simulation);
        primaryStage.show();
    }

    private Arguments gatherArguments() {
        String mapa = mapType.getValue();
        int waterNumber = 0;
        int waterPercentage = 0;
        int waterDays = 0;

        if ("Water map".equals(mapa)) {
            waterNumber = Integer.parseInt(waterMapTextField.getText());
            waterPercentage = Integer.parseInt(waterMapPercentageTextField.getText());
            waterDays = Integer.parseInt(waterMapDaysTextField.getText());
        }

        int mapW = Integer.parseInt(mapWidth.getText());
        int mapH = Integer.parseInt(mapHeight.getText());
        int grassE = Integer.parseInt(grassEnergy.getText());
        int copulationE = Integer.parseInt(copulationEnergy.getText());
        int animalE = Integer.parseInt(animalEnergy.getText());
        int energyC = Integer.parseInt(energyCost.getText());
        int animalInitN= Integer.parseInt(animalInitNumber.getText());
        int grassEachD = Integer.parseInt(grassEachDay.getText());
        int coolD = Integer.parseInt(coolDown.getText());
        int grassStart = Integer.parseInt(grassAtStart.getText());
        int energyT = Integer.parseInt(energyTaken.getText());
        int minM = Integer.parseInt(minMut.getText());
        int maxM = Integer.parseInt(maxMut.getText());
        int genomL = Integer.parseInt(genomLen.getText());
        String var = variant.getValue();

        return new Arguments(mapa, mapW, mapH, grassE, copulationE,
                animalE, energyC, animalInitN, grassEachD, coolD, grassStart,
                energyT, minM, maxM, genomL, var, waterNumber, waterPercentage, waterDays);
    }


    private static void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(SimulationPresenter.class.getResource("/images/paw.png")).toExternalForm()));
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public void initialize(URL location, ResourceBundle resources) {
        mapType.setItems(FXCollections.observableArrayList("Normal map", "Water map"));
        presetName.textProperty().addListener((observable, oldValue, newValue) -> {
            saveSet.setDisable(!isPresetNameValid(newValue));
        });
        mapType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Water map".equals(newValue)) {
                waterMapTextField.setEditable(true);
                waterMapTextField.setDisable(false);
                waterMapDaysTextField.setEditable(true);
                waterMapDaysTextField.setDisable(false);
                waterMapPercentageTextField.setEditable(true);
                waterMapPercentageTextField.setDisable(false);
            } else {
                waterMapTextField.setEditable(false);
                waterMapTextField.setDisable(true);
                waterMapDaysTextField.setEditable(false);
                waterMapDaysTextField.setDisable(true);
                waterMapPercentageTextField.setEditable(false);
                waterMapPercentageTextField.setDisable(true);
            }
        });
        waterMapTextField.setTextFormatter(createIntegerTextFormatter(10,1000)); // powinny byc inne ograniczenia
        waterMapDaysTextField.setTextFormatter(createIntegerTextFormatter(7,1000));
        waterMapPercentageTextField.setTextFormatter(createIntegerTextFormatter(50,1000));
        mapWidth.setTextFormatter(createIntegerTextFormatter(30, 1000));
        mapHeight.setTextFormatter(createIntegerTextFormatter(30, 1000));
        grassEnergy.setTextFormatter(createIntegerTextFormatter(10, 1000));
        copulationEnergy.setTextFormatter(createIntegerTextFormatter(30, 1000));
        animalEnergy.setTextFormatter(createIntegerTextFormatter(50, 1000));
        energyCost.setTextFormatter(createIntegerTextFormatter(1, 1000));
        animalInitNumber.setTextFormatter(createIntegerTextFormatter(10, 1000));
        grassEachDay.setTextFormatter(createIntegerTextFormatter(5, 1000));
        coolDown.setTextFormatter(createIntegerTextFormatter(20, 1000));
        grassAtStart.setTextFormatter(createIntegerTextFormatter(20, 1000));
        energyTaken.setTextFormatter(createIntegerTextFormatter(5, 1000));
        minMut.setTextFormatter(createIntegerTextFormatter(5, 1000));
        maxMut.setTextFormatter(createIntegerTextFormatter(5, 1000));
        genomLen.setTextFormatter(createIntegerTextFormatter(5, 1000));
        initializePresetComboBox();

        editSet.setCursor(Cursor.HAND);
        saveSet.setCursor(Cursor.HAND);
        deleteButton.setCursor(Cursor.HAND);
        commitButton.setCursor(Cursor.HAND);
        variant.setCursor(Cursor.HAND);
        mapType.setCursor(Cursor.HAND);
        preset.setCursor(Cursor.HAND);
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

    public void initializePresetComboBox() {
        URL resourcesUrl = getClass().getResource("/presets");

        if (resourcesUrl != null) {
            File resourcesDir = new File(resourcesUrl.getFile());

            if (resourcesDir.exists() && resourcesDir.isDirectory()) {
                File[] jsonFiles = resourcesDir.listFiles((dir, name) -> name.endsWith(".json"));

                if (jsonFiles != null && jsonFiles.length > 0) {
                    preset.setItems(FXCollections.observableArrayList(
                            Arrays.stream(jsonFiles)
                                    .filter(file -> file.isFile() && file.getName().endsWith(".json"))
                                    .map(file -> file.getName().replace(".json", ""))
                                    .collect(Collectors.toList())
                    ));

                    preset.valueProperty().addListener((observable, oldValue, newValue) -> {
                        Arguments args = loadPreset(newValue);
                        if (args != null) {
                            mapType.setValue(args.mapType());
                            mapWidth.setText(String.valueOf(args.mapWidth()));
                            mapHeight.setText(String.valueOf(args.mapHeight()));
                            grassEnergy.setText(String.valueOf(args.grassEnergy()));
                            copulationEnergy.setText(String.valueOf(args.copulationEnergy()));
                            grassEachDay.setText(String.valueOf(args.grassEachDay()));
                            animalInitNumber.setText(String.valueOf(args.animalInitNumber()));
                            animalEnergy.setText(String.valueOf(args.animalEnergy()));
                            energyCost.setText(String.valueOf(args.energyCost()));
                            copulationEnergy.setText(String.valueOf(args.copulationEnergy()));
                            energyTaken.setText(String.valueOf(args.energyTaken()));
                            minMut.setText(String.valueOf(args.minMut()));
                            maxMut.setText(String.valueOf(args.maxMut()));
                            genomLen.setText(String.valueOf(args.genomLenght()));
                            variant.setValue(args.variant());
                            coolDown.setText(String.valueOf(args.coolDown()));
                            grassAtStart.setText(String.valueOf(args.grassAtStart()));
                            waterMapTextField.setText(String.valueOf(args.waterNumber()));
                            waterMapDaysTextField.setText(String.valueOf(args.waterDays()));
                            waterMapPercentageTextField.setText(String.valueOf(args.waterPercentage()));
                        }
                        updateButtonAvailability(newValue);
                    });
                    preset.setValue(preset.getItems().get(0));
                }
            }
        }
    }

    public void editPreset(ActionEvent actionEvent) {
        String selectedPreset = preset.getValue();
        if (selectedPreset != null) {
            Arguments args = gatherArguments();
            updatePreset(selectedPreset, args);
        }
    }

    private boolean isPresetNameValid(String presetName) {
        if (presetName.trim().isEmpty()) {
            return false;
        }

        String presetsFolder = "/presets";
        File presetsDir = new File(Objects.requireNonNull(getClass().getResource(presetsFolder)).getFile());
        File presetFile = new File(presetsDir, presetName + ".json");

        return !presetFile.exists();
    }

    @FXML
    private void savePreset(ActionEvent actionEvent) {
        String presetNameValue = presetName.getText().trim();

        if (isPresetNameValid(presetNameValue)) {
            Arguments args = gatherArguments();
            Gson gson = new Gson();
            String json = gson.toJson(args);
            String presetsFolder = "/presets";
            URL resourcesUrl = getClass().getResource(presetsFolder);

            if (resourcesUrl != null) {
                File presetsDir = new File(resourcesUrl.getFile());
                File presetFile = new File(presetsDir, presetNameValue + ".json");

                try (FileWriter writer = new FileWriter(presetFile)) {
                    writer.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                preset.getItems().add(presetNameValue);

                preset.setValue(presetNameValue);
                presetName.clear();
            }
        }
    }

    private Arguments loadPreset(String presetName) {
        String presetsFolder = "/presets";
        File presetsDir = new File(Objects.requireNonNull(getClass().getResource(presetsFolder)).getFile());
        File presetFile = new File(presetsDir, presetName + ".json");

        try (FileReader fileReader = new FileReader(presetFile)) {
            Gson gson = new Gson();
            return gson.fromJson(fileReader, Arguments.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void updatePreset(String presetName, Arguments args) {
        String presetsFolder = "/presets";
        File presetsDir = new File(Objects.requireNonNull(getClass().getResource(presetsFolder)).getFile());
        File presetFile = new File(presetsDir, presetName + ".json");

        try (FileWriter writer = new FileWriter(presetFile)) {
            Gson gson = new Gson();
            String json = gson.toJson(args);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePreset(ActionEvent actionEvent) {
        String selectedPreset = preset.getValue();
        if (selectedPreset != null) {
            String presetsFolder = "/presets";
            File presetsDir = new File(Objects.requireNonNull(getClass().getResource(presetsFolder)).getFile());
            File presetFile = new File(presetsDir, selectedPreset + ".json");

            if (presetFile.exists()) {
                if (presetFile.delete()) {
                    if(preset.getSelectionModel().getSelectedIndex()==0)
                        preset.getSelectionModel().select(1);
                    preset.getItems().remove(selectedPreset);
                }
            }
        }
    }

    private boolean isRestrictedPreset(String presetName) {
        return presetName.matches("NormalMap[12]|WaterMap[12]");
    }

    private void updateButtonAvailability(String selectedPreset) {
        boolean isRestrictedPreset = isRestrictedPreset(selectedPreset);
        editSet.setDisable(isRestrictedPreset);
        deleteButton.setDisable(isRestrictedPreset);
    }

}