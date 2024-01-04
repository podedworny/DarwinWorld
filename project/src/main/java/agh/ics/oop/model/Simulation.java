package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{
    private final List<Animal> AnimalList = new ArrayList<>();
    private final RectangularMap map; //    private final WorldMap map;
    private final int grassCount;

    public Simulation(int startingNumberOfAnimals, int startingEnergy , int genomLenght, RectangularMap map, int grassCount) {
        this.map = map;
        this.grassCount = grassCount;
        // konstruktor symulacji

        for (int i=0; i<startingNumberOfAnimals; i++){
            Animal animal = new Animal(map.getWidth(),map.getHeight(), startingEnergy, genomLenght);
            AnimalList.add(animal);
            map.placeNewAnimal(animal);
        }
        map.placeNewGrass(grassCount);

        //...
    }
    public void run(){
        //Usunięcie martwych zwierzaków z mapy.
//        Skręt i przemieszczenie każdego zwierzaka.
//        Konsumpcja roślin, na których pola weszły zwierzaki.
//        Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
//        Wzrastanie nowych roślin na wybranych polach mapy.

    }

}

