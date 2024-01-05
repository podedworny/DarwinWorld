package agh.ics.oop.model;

import java.util.Comparator;
import java.util.Random;

public class Animal implements Comparable<Animal>{
    private Vector2d position; // pozycja
    private MapDirection orientation; // orientacja
    private final Genom genom; //nasz genom
    private int energy; //poziom energii
    private int age; //wiek
    private int childrencount; // liczba dzieci

    public Animal(Vector2d pos, int energy, Genom genom){
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = pos;  // pozycja rodzicow
        this.energy = energy; // ilosc energii i jak wyglada genom tworzymy w sex-metodzie
        this.genom = genom;
        age = 0;
        childrencount = 0;
    }

    public Animal(int width, int height, int energy, int genomLen){ // konstruktor tylko do stworzenia animali na poczatku
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = new Vector2d(random.nextInt(width),random.nextInt(height));  // tu bedzie chyba losowanie pozycji
        this.energy = energy;
        this.genom = new Genom(genomLen);
        age = 0;
        childrencount = 0;
    }

    public void move(){
        MapDirection[] genomTab = genom.getMoves();
        int ind = genom.getIndex();
        //energy--; // zmniejszanie energii
        // canMoveTo na to pole
        position.add(genomTab[ind].toUnitVector()); //(zmiana jego pozycji)
        for (int i=0; i<genomTab[ind].getI();i++){  // obracanie zwierzakiem jak śmigłem (od 0 do 7 razy)
            orientation = orientation.next();
        } // trzeba sprawdzac czy wychodzi poza mape a jak tak to zmienic jego orientacje lub pozycje
    }

    public Vector2d getPosition(){
        return position;
    }

    public Comparator<Animal> comparator(){
        return Comparator
                .comparingInt((Animal a) -> a.energy)
                .thenComparingInt(a -> a.age)
                .thenComparingInt(a -> a.childrencount)
                .thenComparing(a -> new Random().nextInt());
    }

    @Override
    public int compareTo(Animal animal) {
        return comparator().compare(this, animal);
    }

    public Genom getGenom() {
        return genom;
    }

    public int getEnergy() {
        return energy;
    }
}