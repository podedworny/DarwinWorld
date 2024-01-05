package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{
    private final List<Animal> AnimalList = new ArrayList<>();
    private final RectangularMap map; //    private final WorldMap map;

    private Arguments args;
    public Simulation(Arguments args, RectangularMap map) {
        this.map = map;
        this.args = args;
        // konstruktor symulacji

        for (int i=0; i<args.animalInitNumber(); i++){
            Animal animal = new Animal(map.getWidth(),map.getHeight(), args.animalEnergy(), args.genomLenght());
            AnimalList.add(animal);
            map.placeNewAnimal(animal);
        }
        map.placeNewGrass(args.grassAtStart());

        //...



    }
    public void run(){
//        1. Usunięcie martwych zwierzaków z mapy.

//        2. Skręt i przemieszczenie każdego zwierzaka.

//        3. Konsumpcja roślin, na których pola weszły zwierzaki.

//        4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.

//        5. Wzrastanie nowych roślin na wybranych polach mapy.
        map.placeNewGrass(args.grassEachDay());
    }

}

