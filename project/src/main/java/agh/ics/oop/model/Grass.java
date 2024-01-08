package agh.ics.oop.model;

public class Grass implements WorldElement{
    private final Vector2d position;
    private int Energy;
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
        return "/images/grass128.png";
    }
}
