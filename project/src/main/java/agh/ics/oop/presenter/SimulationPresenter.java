package agh.ics.oop.presenter;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.util.List;
import java.util.Objects;


public class SimulationPresenter implements MapChangeListener {

    public GridPane mapGrid;
    public Label dayLabel;
    public Label numberOfAnimals;
    public SplitPane mainSplitPane;
    private RectangularMap rMap;
    @FXML
    private Label simulationLabel;
    @FXML
    public Button startSimulation;
    private Arguments args;

    public static void setArguments(Arguments args){
        // sie okaze czy cos tu bedzie
        // argumenty sa w args juz
    }

    public void setWorldMap(RectangularMap rMap){
        this.rMap = rMap;
    }


    public void drawMap(RectangularMap worldMap) {
//        simulationLabel.setText(worldMap.toString());
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
//
//                    imageView.setFitWidth(CELL_WIDTH);
//                    imageView.setFitHeight(CELL_HEIGHT);
                    Label label = new Label("*");
                    if (path.equals("/images/paw128.png")) {
//                        imageView.setRotate(worldMap.objectAt(new Vector2d(i, j)).getOrientation().getI() * 45);
                        label.setText("A");
                    }
                    label.setAlignment(Pos.CENTER);
                    int adjustedI = i - bottomLeft.getX() ;
                    int adjustedJ = colSize  - (j - bottomLeft.getY()) -1;

                    mapGrid.add(label, adjustedI, adjustedJ);
                }
            }
        }

        dayLabel.setText("Day " + Simulation.getDay());
        numberOfAnimals.setText("Number of animals " + Simulation.numberOfAnimals());
    }

    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        Platform.runLater(() -> {
            clearGrid();
            drawMap(worldMap);
        });
    }

    @FXML
    public void onSimulationStartClicked() {
        Simulation simulation = new Simulation(rMap.getArgs(), rMap);
        SimulationEngine simulationEngine = new SimulationEngine(List.of(simulation));
        simulationEngine.runAsync();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    // tu beda metody z odpalaniem symulacji i printowaniem mapy i wykresu(?)

}
