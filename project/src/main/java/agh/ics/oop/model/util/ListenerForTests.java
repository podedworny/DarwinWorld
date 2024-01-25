package agh.ics.oop.model.util;

import agh.ics.oop.model.map.IMap;

public class ListenerForTests implements MapChangeListener{
    @Override
    public void mapChanged(IMap worldMap) {
        System.out.println("Map Changed");
    }
}
