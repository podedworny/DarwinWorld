package agh.ics.oop.model;

import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    Vector2d test_1 = new Vector2d(1,2);
    Vector2d test_2 = new Vector2d(3,4);
    Vector2d test_3 = new Vector2d(1,0);
    Vector2d test_4 = new Vector2d(2,-1);
    @Test
    public void equals_test(){
        assertTrue(test_1.equals(new Vector2d(1,2)));
        assertFalse(test_1.equals(test_2));
        assertFalse(test_1.equals(test_3));
        assertTrue(test_2.equals(new Vector2d(3,4)));
        assertFalse(test_2.equals(test_1));
        assertFalse(test_2.equals(test_3));
        assertTrue(test_3.equals(new Vector2d(1,0)));
        assertFalse(test_3.equals(test_1));
        assertFalse(test_3.equals(test_2));
    }

    @Test
    public void toString_test(){
        assertEquals("(1,2)",test_1.toString());
        assertEquals("(3,4)",test_2.toString());
        assertEquals("(1,0)",test_3.toString());
        assertEquals("(2,-1)",test_4.toString());
    }

    @Test
    public void getX_test(){
        assertEquals(1,test_1.getX());
        assertEquals(3,test_2.getX());
        assertEquals(1,test_3.getX());
        assertEquals(2,test_4.getX());
    }

    @Test
    public void getY_test(){
        assertEquals(2,test_1.getY());
        assertEquals(4,test_2.getY());
        assertEquals(0,test_3.getY());
        assertEquals(-1,test_4.getY());
    }


    @Test
    public void add_test(){
        assertEquals(new Vector2d(4,6),test_1.add(test_2));
        assertEquals(new Vector2d(5,3),test_2.add(test_4));
        assertEquals(new Vector2d(4,4),test_3.add(test_2));
        assertEquals(new Vector2d(3,1),test_4.add(test_1));
        assertEquals(new Vector2d(3,-1),test_4.add(test_3));
    }


}
