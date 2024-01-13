package agh.ics.oop.presenter;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.SimulationState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class SimulationPresenter implements MapChangeListener {

    public GridPane mapGrid;
    public Label dayLabel;
    public Label numberOfAnimals;
    public SplitPane mainSplitPane;
    public Label data1;
    public Label data2;
    private IMap map;
    @FXML
    private Label simulationLabel;
    @FXML
    public Button startSimulation;
    private Arguments args;
    private Simulation simulation;
    private Stage primaryStage;
    private boolean firstClick=true;

    private static final Image GRASS = new Image(Objects.requireNonNull(SimulationPresenter.class.getResource("/images/grass128.png")).toExternalForm());
    private static final Image ANIMAL = new Image(Objects.requireNonNull(SimulationPresenter.class.getResource("/images/paw128.png")).toExternalForm());
    private static final Image WATER = new Image(Objects.requireNonNull(SimulationPresenter.class.getResource("/images/water128.png")).toExternalForm());

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.primaryStage.setOnCloseRequest(event -> {
            System.out.println("Okno zostało zamknięte");
//            Platform.exit();
            simulation.setState(SimulationState.FINISHED);
        });
    }

//    public static void setArguments(Arguments args){
//        // sie okaze czy cos tu bedzie
//        // argumenty sa w args juz
//    }

    public void setWorldMap(IMap map){
        this.map = map;
    }


    public void drawMap(IMap worldMap) {
        mapGrid.setAlignment(Pos.CENTER);
        int CELL_WIDTH = 15;
        int CELL_HEIGHT = 15;
        Arguments args = worldMap.getArgs();
        Vector2d topRight = new Vector2d(args.mapWidth(),args.mapHeight());
        Vector2d bottomLeft = new Vector2d(0,0);
        int rowSize = args.mapWidth();
        int colSize = args.mapHeight();
        for (int j = 0; j < rowSize; j++) {
            ColumnConstraints column = new ColumnConstraints(CELL_WIDTH);
            mapGrid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < colSize; i++) {
            RowConstraints row = new RowConstraints(CELL_HEIGHT);
            mapGrid.getRowConstraints().add(row);
        }

        for (int i = bottomLeft.getX(); i < topRight.getX(); i++) {
            for (int j = bottomLeft.getY(); j < topRight.getY(); j++) {
                if (worldMap.objectAt(new Vector2d(i, j)) != null) {
//                    Label label = new Label(worldMap.objectAt(new Vector2d(i, j)).toString());
                    String path = worldMap.objectAt(new Vector2d(i, j)).toString();

//                    Image image = new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
//                    ImageView imageView = new ImageView(image);
                    ImageView imageView;

                    Label label = new Label("*");
                    if (path.equals("/images/paw128.png")) {
                        imageView = new ImageView(ANIMAL);
                        imageView.setRotate(worldMap.objectAt(new Vector2d(i, j)).getOrientation().getI() * 45);
                        label.setText("A");
                    }
                    else if (path.equals("/images/water128.png")){
                        imageView = new ImageView(WATER);
                    }
                    else{
                        imageView = new ImageView(GRASS);
                    }
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_HEIGHT);
                    label.setAlignment(Pos.CENTER);
                    int adjustedI = i - bottomLeft.getX() ;
                    int adjustedJ = colSize  - (j - bottomLeft.getY()) -1;

                    mapGrid.add(imageView, adjustedI, adjustedJ);
                }
            }
        }
//        int date = Simulation.getDay();
        dayLabel.setText("Day " + worldMap.getDay());
        numberOfAnimals.setText("Number of animals " + worldMap.numberOfAnimals());
        data2.setText("Most populat genom: " + Arrays.toString(worldMap.getMostPopularGenom()) +"\nGrassFields: "+ worldMap.getGrassFields() + "\nAverage Energy Level: "+ worldMap.averageEnergyLevel()+"\nAverage child count: "+ worldMap.averageChildrenCount()+"\nAverage Dead Animal Age: " + worldMap.averageAge());
    }

    @Override
    public void mapChanged(IMap worldMap) {
        Platform.runLater(() -> {
            clearGrid();
            drawMap(worldMap);
        });
    }

    public void onSimulationStartClicked() {
        if (firstClick) {
            Thread thread = new Thread((simulation));
            thread.start();
            firstClick = false;
        } else {
            simulation.setState(SimulationState.STARTED);
        }
    }

    public void stopSimulation(){
        simulation.stopSimulation();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

}
