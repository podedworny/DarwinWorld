package agh.ics.oop;

import agh.ics.oop.model.Arguments;
import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Simulation;
import javafx.application.Application;

import java.io.Console;

public class World {
    public static void main(String[] args) {
//        Application.launch(SimulationApp.class);
    Arguments arguments = new Arguments("Normal map",
            10,
            10,
            5,
            10,
            20,
            1,
            5,
            1,
            0,
            5,
            5,
            2,
            2,
            6,
            "normal",
            0);
    ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
    RectangularMap rMap = new RectangularMap(arguments);
    rMap.addObserver(consoleMapDisplay);
    Simulation simulation = new Simulation(arguments,rMap);
    simulation.run();
    }
}