package agh.ics.oop.model;

public class Grass {
    private final Vector2d position;
    private int Energy;
    public void setEnergy(int energy){
        Energy = energy;
    }
    public Grass(Vector2d position) {
        this.position = position;
    }
    public Vector2d getPosition() {
        return position;
    }
    public int getEnergy() {
        return Energy;
    }
}
