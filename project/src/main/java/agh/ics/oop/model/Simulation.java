package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements Runnable{
    private final List<Animal> animalList = new LinkedList<>(); //linked list bo do usuwania umierajacych przechodzimy po nich i usuwamy w O(1)
    private final RectangularMap map; //    private final WorldMap map;
    private final int grassCount;

    public Simulation(int startingNumberOfAnimals, int startingEnergy , int genomLenght, RectangularMap map, int grassCount) {
        this.map = map;
        this.grassCount = grassCount;
        // konstruktor symulacji

        for (int i=0; i<startingNumberOfAnimals; i++){
            Animal animal = new Animal(map.getWidth(),map.getHeight(), startingEnergy, genomLenght);
            animalList.add(animal);
            map.placeNewAnimal(animal);
        }
        map.placeNewGrass(grassCount);

        //...
    }
    public void run(){
        while (true) {
//        Skręt i przemieszczenie każdego zwierzaka.
            moveAnimals();
//        Konsumpcja roślin, na których pola weszły zwierzaki.
            eatGrass();
//        Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            reproduce();
//        Wzrastanie nowych roślin na wybranych polach mapy.
            map.placeNewGrass(grassCount);
//        Usunięcie martwych zwierzaków z mapy. <-- usuwanie powinno byc niby pierwsze, ale jak zaczynamy symulacje
//        od pierwszego etapu to najpierw sie ruszaja itd
            deleteDeadAnimals();
            try {
                Thread.sleep(500);
            }
            catch (Exception ignored){}
        }
    }

    private void deleteDeadAnimals(){
        Iterator<Animal> iterator = animalList.iterator(); // nie robie for eacha bo wywalilby blad przy usuwaniu
        while (iterator.hasNext()) {    // iteruje po liscie zwierzakow - usuwam z niej martwe i usuwam z planszy
            Animal animal = iterator.next();
            if (animal.getEnergy() == 0) {
                iterator.remove();
                map.removeAnimal(animal);
            }
        }
    }

    private void moveAnimals(){
        for (Animal animal : animalList)
            animal.move();
    }

    private void eatGrass(){
        //
    }

    private void reproduce(){
        //
    }
}

