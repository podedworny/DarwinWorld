package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements Runnable{
    private final List<Animal> animalList = new LinkedList<>(); //linked list bo do usuwania umierajacych przechodzimy po nich i usuwamy w O(1)
    private final RectangularMap map; //    private final WorldMap map;
    private final Arguments args;

    public Simulation(Arguments args, RectangularMap map) {
        this.map = map;
        this.args = args;
        // konstruktor symulacji

        for (int i=0; i<args.animalInitNumber(); i++){
            Animal animal = new Animal(map.getWidth(),map.getHeight(), args.animalEnergy(), args.genomLenght());
            animalList.add(animal);
            map.placeAnimal(animal);
        }
        map.placeNewGrass(args.grassAtStart());

        //...
    }
    public void run(){
        while (true) {
//        1. Usunięcie martwych zwierzaków z mapy.
            deleteDeadAnimals();
//        2. Skręt i przemieszczenie każdego zwierzaka.
            moveAnimals();
//        3. Konsumpcja roślin, na których pola weszły zwierzaki.
            map.eatGrass();
//        4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            map.reproduce();
//        5. Wzrastanie nowych roślin na wybranych polach mapy.
            map.placeNewGrass(args.grassEachDay());
            try {
                Thread.sleep(args.coolDown());
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
            map.move(animal);
    }
}

