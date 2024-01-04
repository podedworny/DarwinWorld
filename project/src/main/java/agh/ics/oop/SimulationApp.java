package agh.ics.oop;

import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("menu.fxml"));
        BorderPane viewRoot = loader.load();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    public static void simulationWindow(int mapWidth, int mapHeight,
                                        int grassEnergy, int copulationEnergy,
                                        int animalEnergy, int energyCost,
                                        int animalInitNumber, int grassEachDay,
                                        int coolDown) throws Exception {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SimulationApp.class.getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter.setArguments(mapWidth, mapHeight, grassEnergy, copulationEnergy,
                                        animalEnergy, energyCost, animalInitNumber, grassEachDay, coolDown);
        SimulationPresenter presenter = loader.getController();

        // akcje wywołania mapy observera itp

        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private static void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menu");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}

