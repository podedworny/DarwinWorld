package agh.ics.oop.model.element;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

public class Grass implements WorldElement {
    private final Vector2d position;
    private final int Energy;
    public Grass(Vector2d position, int energy) {
        this.position = position;
        this.Energy = energy;
    }
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public MapDirection getOrientation() {
        return MapDirection.N;
    }

    public int getEnergy() {
        return Energy;
    }

    @Override
    public String toString() {
        return "grass";
    }
}
