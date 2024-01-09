package agh.ics.oop.model;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Simulation implements Runnable{
    private static final List<Animal> animalList = new LinkedList<>(); //linked list bo do usuwania umierajacych przechodzimy po nich i usuwamy w O(1)
//    private final List<Animal> deadAnimalList = new LinkedList<>(); // martwe zwierzaki aby policzyc srednia wieku
    private final List<Animal> allAnimalList = new LinkedList<>(); // wszystkie zyjace zwierzaki kiedykolwiek
    private final RectangularMap map; //    private final WorldMap map;
    private final Arguments args;
    private static int day= 0;
    private final Map<MapDirection[], Integer> genomDictionary = new HashMap<>();
    private SimulationState state = SimulationState.STARTED;
    private int deadAnimals = 0;
    private int sumDaysOfDeadAnimals=0;

    public Simulation(Arguments args, RectangularMap map) {
        this.map = map;
        this.args = args;
        // konstruktor symulacji
        for (int i=0; i<args.animalInitNumber(); i++){
            Animal animal = new Animal(map.getWidth(),map.getHeight(), args.animalEnergy(), args.genomLenght());
            animalList.add(animal);
            allAnimalList.add(animal);
            map.placeAnimal(animal);
            System.out.println(animal.getGenom());
            genomDictionary.compute(animal.getGenom().getMoves(), (key, value) -> (value == null) ? 1 : value + 1);
        }
        map.placeNewGrass(args.grassAtStart());

        //...
    }

    public void setState(SimulationState state) {
        if(this.state!=SimulationState.FINISHED){
            this.state = state;
        }
    }

    public void stopSimulation() {
        state = SimulationState.STOPED;
    }

    public void run(){
        while (true) {
            switch (state) {
                case STARTED -> {
                    deleteDeadAnimals();
                    moveAnimals();
                    map.eatGrass();
                    reproduce();
                    map.placeNewGrass(args.grassEachDay());
                    descendantCounting();

                    try {
                        Thread.sleep(args.coolDown());
                    }
                    catch (Exception ignored){}

                    for (Animal animal: animalList)
                        animal.nextDay();
                    day ++;
                    if(animalList.isEmpty()){
                        state = SimulationState.FINISHED;
                        System.err.println("KONIEC");
                    }
                }
                case STOPED -> sleep(100);
                case FINISHED -> {
                    return;
                }
            }

        }
    }
    public static void sleep(int milliseconds) {
        try {
            Thread sleepingThread = new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            sleepingThread.start();
            sleepingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void descendantCounting() {
        for (Animal animal : allAnimalList)
            animal.descendantCalculate();
    }

    public static int getDay() {
        return day;
    }

    public static int numberOfAnimals() {
        return animalList.size();
    }


    private void deleteDeadAnimals(){
        Iterator<Animal> iterator = animalList.iterator(); // nie robie for eacha bo wywalilby blad przy usuwaniu
        while (iterator.hasNext()) {    // iteruje po liscie zwierzakow - usuwam z niej martwe i usuwam z planszy
            Animal animal = iterator.next();
            if (animal.getEnergy() <= 0) {
                animal.setDeathDate(day);
//                deadAnimalList.add(animal);
                deadAnimals++;
                sumDaysOfDeadAnimals += animal.getAge();
                System.out.println(animal.getGenom());
                System.out.println(genomDictionary.get(animal.getGenom().getMoves()));
                genomDictionary.compute(animal.getGenom().getMoves(), (key, value) -> (value <= 1) ? null : value - 1);
                iterator.remove();
                map.removeAnimal(animal);
            }
        }
    }

    private void moveAnimals(){
        for (Animal animal : animalList)
            map.move(animal);
    }

    public void reproduce(){
        // biore wszystkie pozycje na ktorych sa zwierzaki
        // i dla kazdego pola biore dwojke (jezeli istnieje i ma energie) na sex
        // zwracam zwierzaka i klade go na plansze
        Set<Vector2d> animalsPositions = map.animals.keySet();
        for (Vector2d position : animalsPositions){
            List<Animal> animalPos = map.animals.get(position);
            List<Animal> kids = new ArrayList<>();
            for (int i = 0; i < animalPos.size(); i += 2) { // to tez do przepisania ale niech bedzie poki co
                if (i + 1 < animalPos.size()  && animalPos.get(i).getEnergy()>=args.energyTaken() && animalPos.get(i+1).getEnergy()>=args.energyTaken())
                    kids.add(map.animalCopulation(animalPos.get(i), animalPos.get(i + 1)));
            }
            for (Animal kid : kids){
                map.placeAnimal(kid);
                animalList.add(kid);
                allAnimalList.add(kid);
                genomDictionary.compute(kid.getGenom().getMoves(), (key, value) -> (value == null) ? 1 : value + 1);
            }
        }
    }

    public double averageEnergyLevel(){
        double result = 0.0;
        for (Animal animal : animalList)
            result += animal.getEnergy();
        return result / animalList.size();
    }

    public double averageChildrenCount(){
        double result = 0.0;
        for (Animal animal : animalList)
            result += animal.getChildrenCount();
        return result / animalList.size();
    }

    public double averageAge(){
//        double result = 0.0;
//        for (Animal animal : deadAnimalList)
//            result += animal.getAge();
//        return result / deadAnimalList.size();
        if (deadAnimals ==0)
            return 0;
        return (double) sumDaysOfDeadAnimals / deadAnimals;
    }

    public MapDirection[] getMostPopularGenom(){
        int k = 0;
        MapDirection[] result = null;
        for (Map.Entry<MapDirection[], Integer> entry : genomDictionary.entrySet()) {
            int frequency = entry.getValue();
            if (frequency > k) {
                k = frequency;
                result = entry.getKey();
            }
        }
        return result;
    }


}

