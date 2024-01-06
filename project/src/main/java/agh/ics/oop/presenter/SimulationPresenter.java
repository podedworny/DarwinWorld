package agh.ics.oop.presenter;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.Arguments;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Simulation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

public class SimulationPresenter implements MapChangeListener {

    private RectangularMap rMap;
    @FXML
    private Label simulationLabel;
    @FXML
    public Button startSimulation;

    public static void setArguments(Arguments args){
        // sie okaze czy cos tu bedzie
        // argumenty sa w args juz
    }
    public void setWorldMap(RectangularMap rMap){
        this.rMap = rMap;
    }
    public void drawMap(RectangularMap worldMap) {
        simulationLabel.setText(worldMap.toString());
    }

    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap(worldMap);
        });
    }

    @FXML
    public void onSimulationStartClicked() {
        Simulation simulation = new Simulation(rMap.getArgs(), rMap);
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
    }

    // tu beda metody z odpalaniem symulacji i printowaniem mapy i wykresu(?)

}
