package agh.ics.oop.presenter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SimulationPresenter {
    private static int mapWidth;
    private static int mapHeight;
    private static int grassEnergy;
    private static int copulationEnergy;
    private static int animalEnergy;
    private static int energyCost;
    private static int animalInitNumber;
    private static int grassEachDay;
    private static int coolDown;
    @FXML
    private Label przyklad;

    // wszystkie argumenty potrzebne do stworzenia symulacj

    public static void setArguments(int mapW, int mapH,
                                    int grassE, int copulationE,
                                    int animalE, int energyC,
                                    int animalInitN, int grassEachD,
                                    int coolD){
        mapWidth = mapW;
        mapHeight = mapH;
        grassEnergy = grassE;
        copulationEnergy = copulationE;
        animalEnergy = animalE;
        energyCost = energyC;
        animalInitNumber = animalInitN;
        grassEachDay = grassEachD;
        coolDown = coolD;
    }

    // tu beda metody z odpalaniem symulacji i printowaniem mapy i wykresu(?)
}
