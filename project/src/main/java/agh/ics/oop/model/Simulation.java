package agh.ics.oop.model;

import java.util.*;

public class Simulation implements Runnable{
    private static final List<Animal> animalList = new LinkedList<>(); //linked list bo do usuwania umierajacych przechodzimy po nich i usuwamy w O(1)
    private final RectangularMap map; //    private final WorldMap map;
    private final Arguments args;
    private int day= 0;

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
            System.out.println(map);
//            System.out.println(animalList.toArray().length);
//
//            System.out.println(animalList.get(0).getPosition().toString());
//            System.out.println(Arrays.toString(animalList.get(0).getGenom().getMoves()));
//            System.out.println(animalList.get(0).getGenom().getIndex());
//            System.out.println(animalList.get(1).getPosition().toString());
//            System.out.println(Arrays.toString(animalList.get(1).getGenom().getMoves()));
//            System.out.println(animalList.get(1).getGenom().getIndex());
//        2. Skręt i przemieszczenie każdego zwierzaka.
            moveAnimals();
//        3. Konsumpcja roślin, na których pola weszły zwierzaki.
            map.eatGrass();
//        4. Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            map.reproduce();
//        5. Wzrastanie nowych roślin na wybranych polach mapy.
            map.placeNewGrass(args.grassEachDay());

            try {
                Thread.sleep(20);
            }
            catch (Exception ignored){}

            for (Animal animal: animalList)
                animal.nextDay();
            day ++;
        }
    }

    public static void addNewAnimal(Animal animal){
        animalList.add(animal);
    }

    private void deleteDeadAnimals(){
        Iterator<Animal> iterator = animalList.iterator(); // nie robie for eacha bo wywalilby blad przy usuwaniu
        while (iterator.hasNext()) {    // iteruje po liscie zwierzakow - usuwam z niej martwe i usuwam z planszy
            Animal animal = iterator.next();
            if (animal.getEnergy() <= 0) {
                System.out.println(iterator);
                iterator.remove();
//                System.out.println("");
//                System.out.println(map.ilosc());
                System.out.println("DEAD:");
                map.removeAnimal(animal);
//                System.out.println(map.ilosc());
            }
        }
//        System.out.println("POROWNANIE " + animalList.toArray().length + " " + map.ilosc());
    }

    private void moveAnimals(){
        for (Animal animal : animalList)
            map.move(animal);


    }

}

