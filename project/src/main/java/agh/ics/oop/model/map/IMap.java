package agh.ics.oop.model.map;

import agh.ics.oop.model.element.Animal;
import agh.ics.oop.model.element.WorldElement;
import agh.ics.oop.model.simulation.Arguments;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.MapChangeListener;

import java.util.List;

public interface IMap {
    void deleteDeadAnimals();

    void moveAnimals();

    void eatGrass();

    void reproduce();

    void placeNewGrass(int grassEachDay);

    void animalsNextDate();

    int numberOfAnimals();

    int getWidth();

    int getHeight();
    
    Arguments getArgs();

    WorldElement objectAt(Vector2d vector2d);

    void addObserver(MapChangeListener observer);

    void removeObserver(MapChangeListener observer);

    int getDay();

    List<MapDirection> getMostPopularGenome();

    int getGrassFields();

    double averageEnergyLevel();

    double averageChildrenCount();

    double averageAge();
    boolean canMoveTo(Vector2d position);
    Vector2d getNewPosition(Vector2d position, MapDirection direction);
    Animal getAnimal(Vector2d position);
    boolean isWaterMap();
    int everAnimalCount();
    int getID();
    int getFreeFields();
}
