package agh.ics.oop.model.map;

import agh.ics.oop.model.simulation.Arguments;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.Vector2d;

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

