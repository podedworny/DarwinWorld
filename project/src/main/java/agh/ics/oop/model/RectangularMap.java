package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;

public class RectangularMap {
    private final int width;
    private final int height;
    protected final Map<Vector2d, Animal> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
