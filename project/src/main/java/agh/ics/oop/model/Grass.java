package agh.ics.oop.model;

public class Grass {
    private final Vector2d position;
    private int Energy;
    public Grass(Vector2d position, int energy) {
        this.position = position;
        this.Energy = energy;
    }
    public Vector2d getPosition() {
        return position;
    }
    public int getEnergy() {
        return Energy;
    }
}
