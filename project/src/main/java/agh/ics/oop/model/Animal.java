package agh.ics.oop.model;

import java.util.Random;

public class Animal {
    private Vector2d position; // pozycja
    private MapDirection orientation; // orientacja
    private final Genom genom; //nasz genom
    private int energy; //poziom energii

    public Animal(Vector2d pos, int energy, Genom genom){
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = pos;  // tu bedzie chyba losowanie pozycji
        this.energy = energy;
        this.genom = genom;
    }
    public void move(){
        MapDirection[] genomTab = genom.getGenom();
        int ind = genom.getIndex();
        //energy--; // zmniejszanie energii
        // canMoveTo na to pole
        position.add(genomTab[ind].toUnitVector()); //(zmiana jego pozycji)
        for (int i=0; i<genomTab[ind].getI();i++){  // obracanie zwierzakiem jak śmigłem (od 0 do 7 razy)
            orientation = orientation.next();
        }
    }

}
