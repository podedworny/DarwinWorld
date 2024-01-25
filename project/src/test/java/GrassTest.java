import agh.ics.oop.model.element.Grass;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrassTest {
    Grass g1 = new Grass(new Vector2d(1,2), 10);
    Grass g2 = new Grass(new Vector2d(0,10), 0);
    Grass g3 = new Grass(new Vector2d(16,5), 13);
    Grass g4 = new Grass(new Vector2d(20,0), 50);

    @Test
    public void getPositionTest(){
        assertEquals(g1.getPosition(), new Vector2d(1,2));
        assertEquals(g2.getPosition(), new Vector2d(0,10));
        assertEquals(g3.getPosition(), new Vector2d(16,5));
        assertEquals(g4.getPosition(), new Vector2d(20,0));
    }

    @Test
    public void getOrientatnionTest(){
        assertEquals(g1.getOrientation(), MapDirection.N);
        assertEquals(g2.getOrientation(), MapDirection.N);
        assertEquals(g3.getOrientation(), MapDirection.N);
        assertEquals(g4.getOrientation(), MapDirection.N);
    }

    @Test
    public void getEnergyTest(){
        assertEquals(g1.getEnergy(), 10);
        assertEquals(g2.getEnergy(), 0);
        assertEquals(g3.getEnergy(), 13);
        assertEquals(g4.getEnergy(), 50);
    }

    @Test
    public void isAtTest(){
        assertTrue(g1.isAt(new Vector2d(1,2)));
        assertTrue(g2.isAt(new Vector2d(0,10)));
        assertTrue(g3.isAt(new Vector2d(16,5)));
        assertTrue(g4.isAt(new Vector2d(20,0)));

        assertFalse(g1.isAt(new Vector2d(1,3)));
        assertFalse(g2.isAt(new Vector2d(1,2)));
        assertFalse(g3.isAt(new Vector2d(3,3)));
        assertFalse(g4.isAt(new Vector2d(6,2)));
    }

    @Test
    public void toStringTest(){
        assertEquals(g1.toString(),"grass");
        assertEquals(g2.toString(),"grass");
        assertEquals(g3.toString(),"grass");
        assertEquals(g4.toString(),"grass");
    }
}
