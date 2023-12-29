package agh.ics.oop;

import agh.ics.oop.model.MapDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MapDirection> convert(String[] args) {
        List<MapDirection> tab = new ArrayList<>();
        for (String arg : args) {
            switch (arg) {
                case "0" -> tab.add(MapDirection.N);
                case "1" -> tab.add(MapDirection.NE);
                case "2" -> tab.add(MapDirection.E);
                case "3" -> tab.add(MapDirection.SE);
                case "4" -> tab.add(MapDirection.S);
                case "5" -> tab.add(MapDirection.SW);
                case "6" -> tab.add(MapDirection.W);
                case "7" -> tab.add(MapDirection.NW);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return tab;
    }
}