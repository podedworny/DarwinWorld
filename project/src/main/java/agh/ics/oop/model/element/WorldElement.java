package agh.ics.oop.model.element;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

public interface WorldElement {
    Vector2d getPosition();
    String toString();
    boolean isAt(Vector2d position);
    MapDirection getOrientation();
}
