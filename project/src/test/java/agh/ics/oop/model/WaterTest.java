package agh.ics.oop.model;


import agh.ics.oop.model.element.Water;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaterTest {
    Water w1 = new Water(new Vector2d(1,2));
    Water w2 = new Water(new Vector2d(0,10));
    Water w3 = new Water(new Vector2d(16,5));
    Water w4 = new Water(new Vector2d(20,0));

    @Test
    public void getPositionTest(){
        assertEquals(w1.getPosition(), new Vector2d(1,2));
        assertEquals(w2.getPosition(), new Vector2d(0,10));
        assertEquals(w3.getPosition(), new Vector2d(16,5));
        assertEquals(w4.getPosition(), new Vector2d(20,0));
    }

    @Test
    public void getOrientatnionTest(){
        assertEquals(w1.getOrientation(), MapDirection.N);
        assertEquals(w2.getOrientation(), MapDirection.N);
        assertEquals(w3.getOrientation(), MapDirection.N);
        assertEquals(w4.getOrientation(), MapDirection.N);
    }

    @Test
    public void isAtTest(){
        assertTrue(w1.isAt(new Vector2d(1,2)));
        assertTrue(w2.isAt(new Vector2d(0,10)));
        assertTrue(w3.isAt(new Vector2d(16,5)));
        assertTrue(w4.isAt(new Vector2d(20,0)));

        assertFalse(w1.isAt(new Vector2d(1,3)));
        assertFalse(w2.isAt(new Vector2d(1,2)));
        assertFalse(w3.isAt(new Vector2d(3,3)));
        assertFalse(w4.isAt(new Vector2d(6,2)));
    }

    @Test
    public void toStringTest(){
        assertEquals(w1.toString(),"water");
        assertEquals(w2.toString(),"water");
        assertEquals(w3.toString(),"water");
        assertEquals(w4.toString(),"water");
    }

    @Test
    public void neighboursTest(){
        w1.addNeighbour(MapDirection.N,w2);
        w1.addNeighbour(MapDirection.E,w3);
        w2.addNeighbour(MapDirection.S,w1);
        w3.addNeighbour(MapDirection.W,w1);

        Map<MapDirection, Water> w1Neighbours = w1.getNeighbours();
        Map<MapDirection, Water> w2Neighbours = w2.getNeighbours();
        Map<MapDirection, Water> w3Neighbours = w3.getNeighbours();

        assertEquals(w2, w1Neighbours.get(MapDirection.N));
        assertEquals(w3, w1Neighbours.get(MapDirection.E));
        assertEquals(w1, w2Neighbours.get(MapDirection.S));
        assertEquals(w1, w3Neighbours.get(MapDirection.W));
    }
}
