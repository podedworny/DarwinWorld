package agh.ics.oop.model;

import java.util.*;

public class Simulation implements Runnable{
    private static final List<Animal> animalList = new LinkedList<>(); //linked list bo do usuwania umierajacych przechodzimy po nich i usuwamy w O(1)
    private final List<Animal> deadAnimalList = new LinkedList<>(); // martwe zwierzaki aby policzyc srednia wieku
    private final List<Animal> allAnimalList = new LinkedList<>(); // wszystkie zyjace zwierzaki kiedykolwiek
    private final RectangularMap map; //    private final WorldMap map;
    private final Arguments args;
    private static int day= 0;
    private final Map<MapDirection[], Integer> genomDictionary = new HashMap<>();

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
    public void run(){
        while (true) {
//        1. Usunięcie martwych zwierzaków z mapy.
            deleteDeadAnimals();
//            System.out.println(map);
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
            reproduce();
//        5. Wzrastanie nowych roślin na wybranych polach mapy.
            map.placeNewGrass(args.grassEachDay());
            // 6. liczenie potomkow
            descendantCounting();
            try {
                Thread.sleep(args.coolDown());
            }
            catch (Exception ignored){}

            for (Animal animal: animalList)
                animal.nextDay();
            day ++;
            if(animalList.isEmpty()){
                System.err.println("KONIEC");
                break;
            }
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
                deadAnimalList.add(animal);
                System.out.println(animal.getGenom());
                System.out.println(genomDictionary.get(animal.getGenom().getMoves()));
                genomDictionary.compute(animal.getGenom().getMoves(), (key, value) -> (value <= 1) ? null : value - 1);
                iterator.remove();
//                System.out.println("");
//                System.out.println(map.ilosc());
                //System.out.println("DEAD:");
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
        double result = 0.0;
        for (Animal animal : deadAnimalList)
            result += animal.getAge();
        return result / deadAnimalList.size();
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

