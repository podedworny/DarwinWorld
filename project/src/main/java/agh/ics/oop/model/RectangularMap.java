package agh.ics.oop.model;

import java.util.*;

public class RectangularMap extends AbstractMap {

    public RectangularMap(Arguments args, MapChangeListener presenter) {
        super(args, presenter);
    }

    public boolean canMoveTo(Vector2d position){
        return position.getY() >= 0 && position.getY() < super.height;
    }

    @Override
    public boolean isWaterMap() {
        return false;
    }
}

