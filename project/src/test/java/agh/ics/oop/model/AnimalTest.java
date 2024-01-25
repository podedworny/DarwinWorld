package agh.ics.oop.model;

import agh.ics.oop.model.element.Animal;
import agh.ics.oop.model.map.*;
import agh.ics.oop.model.simulation.Arguments;
import agh.ics.oop.model.util.Genome;
import agh.ics.oop.model.util.ListenerForTests;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimalTest {
    Arguments args = new Arguments("Normal map",
            100,
            100,
            100,
            10,
            100,
            10,
            0,
            0,
            100,
            0,
            5,
            0,
            0,
            1,
            "Deafult",
            0,
            0,
            0);
    MapDirection[] directions_r_1 = {MapDirection.N};
    Genome genome_r_1 = new Genome(directions_r_1,0,0);
    Animal test_r_1 = new Animal(new Vector2d(1,1), 10, genome_r_1, 11);
    MapDirection[] directions_r_2 = {MapDirection.S};
    Genome genome_r_2 = new Genome(directions_r_2,0,0);
    Animal test_r_2 = new Animal(new Vector2d(3,1), 10, genome_r_2,12);
    Animal c_test1 = new Animal(new Vector2d(10,10), 20, genome_r_2,13);
    Animal c_test2 = new Animal(new Vector2d(10,10), 10, genome_r_2,14);
    Animal c_test3 = new Animal(new Vector2d(10,10), 10, genome_r_2,15);
    Animal c_test4 = new Animal(new Vector2d(10,10), 10, genome_r_2,16);
    Animal c_test5 = new Animal(new Vector2d(10,10), 10, genome_r_2,17);


    ListenerForTests listenerForTests = new ListenerForTests();
    IMap testRMap = new RectangularMap(args, listenerForTests);

    @Test
    public void rMapTests(){
        testRMap.placeAnimal(test_r_1);
        testRMap.placeAnimal(test_r_2);
        c_test3.nextDay();
        c_test4.nextDay();
        c_test4.nextDay();
        c_test5.nextDay();
        c_test5.nextDay();
        c_test5.addKid(test_r_2);
        testRMap.placeAnimal(c_test5);
        testRMap.placeAnimal(c_test4);
        testRMap.placeAnimal(c_test3);
        testRMap.placeAnimal(c_test2);
        testRMap.placeAnimal(c_test1);

        //getPosition
        assertEquals(new Vector2d(1,1),test_r_1.getPosition());
        assertEquals(new Vector2d(3,1),test_r_2.getPosition());
        assertEquals(new Vector2d(10,10),c_test1.getPosition());
        assertEquals(new Vector2d(10,10),c_test2.getPosition());
        assertEquals(new Vector2d(10,10),c_test3.getPosition());
        assertEquals(new Vector2d(10,10),c_test4.getPosition());
        assertEquals(new Vector2d(10,10),c_test5.getPosition());

        //isAt
        assertTrue(test_r_1.isAt(new Vector2d(1,1)));
        assertTrue(test_r_2.isAt(new Vector2d(3,1)));
        assertTrue(c_test1.isAt(new Vector2d(10,10)));
        assertTrue(c_test2.isAt(new Vector2d(10,10)));
        assertTrue(c_test3.isAt(new Vector2d(10,10)));
        assertTrue(c_test4.isAt(new Vector2d(10,10)));
        assertTrue(c_test5.isAt(new Vector2d(10,10)));
        assertFalse(test_r_1.isAt(new Vector2d(14,41)));
        assertFalse(test_r_2.isAt(new Vector2d(11,19)));
        assertFalse(c_test1.isAt(new Vector2d(13,41)));
        assertFalse(c_test1.isAt(new Vector2d(10,71)));
        assertFalse(c_test1.isAt(new Vector2d(11,1)));
        assertFalse(c_test1.isAt(new Vector2d(1,21)));
        assertFalse(c_test1.isAt(new Vector2d(11,17)));


        //decreaseEnergy && getEnergy
        assertEquals(10,test_r_1.getEnergy());
        assertEquals(10,test_r_2.getEnergy());
        test_r_1.decreaseEnergy(1);
        test_r_2.decreaseEnergy(2);
        assertEquals(9,test_r_1.getEnergy());
        assertEquals(8,test_r_2.getEnergy());

        //nextDat && getAge
        assertEquals(0,test_r_1.getAge());
        assertEquals(0,test_r_2.getAge());
        test_r_1.nextDay();
        test_r_2.nextDay();

        assertEquals(1,test_r_1.getAge());
        assertEquals(1,test_r_2.getAge());
        assertEquals(0,c_test1.getAge());
        assertEquals(0,c_test2.getAge());
        assertEquals(1,c_test3.getAge());
        assertEquals(2,c_test4.getAge());
        assertEquals(2,c_test5.getAge());

        //getIndex
        assertEquals(11,test_r_1.getMyId());
        assertEquals(12,test_r_2.getMyId());
        assertEquals(13,c_test1.getMyId());
        assertEquals(14,c_test2.getMyId());
        assertEquals(15,c_test3.getMyId());
        assertEquals(16,c_test4.getMyId());
        assertEquals(17,c_test5.getMyId());

        //Comparator test

        assertEquals(c_test1, testRMap.returnListOfAnimals(new Vector2d(10,10)).get(0));
        assertEquals(c_test5, testRMap.returnListOfAnimals(new Vector2d(10,10)).get(1));
        assertEquals(c_test4, testRMap.returnListOfAnimals(new Vector2d(10,10)).get(2));
        assertEquals(c_test3, testRMap.returnListOfAnimals(new Vector2d(10,10)).get(3));
        assertEquals(c_test2, testRMap.returnListOfAnimals(new Vector2d(10,10)).get(4));

        //Kids && descendant
        c_test4.addKid(test_r_2);
        c_test3.addKid(test_r_1);
        c_test2.addKid(test_r_1);
        test_r_1.addKid(c_test1);
        test_r_2.addKid(c_test1);
        Animal kid = testRMap.animalCopulation(test_r_1,test_r_2);

        assertEquals(2,test_r_1.getChildrenCount());
        assertEquals(2,test_r_2.getChildrenCount());
        assertEquals(0,c_test1.getChildrenCount());
        assertEquals(1,c_test2.getChildrenCount());
        assertEquals(1,c_test3.getChildrenCount());
        assertEquals(1,c_test4.getChildrenCount());
        assertEquals(1,c_test5.getChildrenCount());

        assertEquals(2,test_r_1.descendantCalculate());
        assertEquals(2,test_r_2.descendantCalculate());
        assertEquals(3,c_test4.descendantCalculate());
        assertEquals(3,c_test5.descendantCalculate());
        assertEquals(3,c_test3.descendantCalculate());
        assertEquals(3,c_test2.descendantCalculate());
        assertEquals(0,c_test1.descendantCalculate());

        assertEquals(4, test_r_1.getEnergy());
        assertEquals(3, test_r_2.getEnergy());
        assertEquals(10, kid.getEnergy());

        //Move
        MapDirection t1 = test_r_1.getOrientation();
        MapDirection t2 = test_r_2.getOrientation();
        System.out.println(t2);
        testRMap.move(test_r_1);
        testRMap.move(test_r_2);
        assertEquals(t1, test_r_1.getOrientation());
        assertEquals(t2.opposite(), test_r_2.getOrientation());
        assertEquals(new Vector2d(1,1).add(t1.toUnitVector()), test_r_1.getPosition());
        assertEquals(new Vector2d(3,1).add(t2.opposite().toUnitVector()), test_r_2.getPosition());
    }

}
