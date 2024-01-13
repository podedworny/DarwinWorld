package agh.ics.oop.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Water implements WorldElement{
    Vector2d position;
    Map<MapDirection, Water> neighbours = new HashMap<>();
    Water(Vector2d position){
        this.position = position;
    }

    public void addNeighbour(MapDirection direction, Water neighbour){
        neighbours.put(direction,neighbour);
    }

    public void removeNeighbour(MapDirection direction){
        neighbours.remove(direction);
    }
    @Override
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

    @Override
    public String toString(){
        return "/images/water128.png";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Water water = (Water) o;
        return Objects.equals(position, water.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
