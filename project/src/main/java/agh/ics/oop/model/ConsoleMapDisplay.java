package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        System.out.println(worldMap);
    }
}
