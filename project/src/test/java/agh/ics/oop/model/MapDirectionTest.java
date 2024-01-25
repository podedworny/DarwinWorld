package agh.ics.oop.model;

import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void getITest(){
        assertEquals(MapDirection.N.getI(),0);
        assertEquals(MapDirection.NE.getI(),1);
        assertEquals(MapDirection.E.getI(),2);
        assertEquals(MapDirection.SE.getI(),3);
        assertEquals(MapDirection.S.getI(),4);
        assertEquals(MapDirection.SW.getI(),5);
        assertEquals(MapDirection.W.getI(),6);
        assertEquals(MapDirection.NW.getI(),7);
    }

    @Test
    public void toStringTest(){
        assertEquals(MapDirection.N.toString(), "N");
        assertEquals(MapDirection.NE.toString(), "NE");
        assertEquals(MapDirection.E.toString(), "E");
        assertEquals(MapDirection.SE.toString(), "SE");
        assertEquals(MapDirection.S.toString(), "S");
        assertEquals(MapDirection.SW.toString(), "SW");
        assertEquals(MapDirection.W.toString(), "W");
        assertEquals(MapDirection.NW.toString(), "NW");
    }

    @Test
    public void nextTest(){
        assertEquals(MapDirection.N.next(),MapDirection.NE);
        assertEquals(MapDirection.NE.next(),MapDirection.E);
        assertEquals(MapDirection.E.next(),MapDirection.SE);
        assertEquals(MapDirection.SE.next(),MapDirection.S);
        assertEquals(MapDirection.S.next(),MapDirection.SW);
        assertEquals(MapDirection.SW.next(),MapDirection.W);
        assertEquals(MapDirection.W.next(),MapDirection.NW);
        assertEquals(MapDirection.NW.next(),MapDirection.N);
    }

    @Test
    public void oppositeTest(){
        assertEquals(MapDirection.N.opposite(),MapDirection.S);
        assertEquals(MapDirection.NE.opposite(),MapDirection.SW);
        assertEquals(MapDirection.E.opposite(),MapDirection.W);
        assertEquals(MapDirection.SE.opposite(),MapDirection.NW);
        assertEquals(MapDirection.S.opposite(),MapDirection.N);
        assertEquals(MapDirection.SW.opposite(),MapDirection.NE);
        assertEquals(MapDirection.W.opposite(),MapDirection.E);
        assertEquals(MapDirection.NW.opposite(),MapDirection.SE);
    }

    @Test
    public void toUnitVectorTest(){
        assertEquals(MapDirection.N.toUnitVector(),new Vector2d(0,1));
        assertEquals(MapDirection.NE.toUnitVector(),new Vector2d(1,1));
        assertEquals(MapDirection.E.toUnitVector(),new Vector2d(1,0));
        assertEquals(MapDirection.SE.toUnitVector(),new Vector2d(1,-1));
        assertEquals(MapDirection.S.toUnitVector(),new Vector2d(0,-1));
        assertEquals(MapDirection.SW.toUnitVector(),new Vector2d(-1,-1));
        assertEquals(MapDirection.W.toUnitVector(),new Vector2d(-1,0));
        assertEquals(MapDirection.NW.toUnitVector(),new Vector2d(-1,1));
    }
}
