package agh.ics.oop.model;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

public class Animal implements Comparable<Animal>, WorldElement{
    private final int myId;
    static int id;
    private Vector2d position; // pozycja
    private MapDirection orientation; // orientacja
    private final Genom genom; //nasz genom
    private int energy; //poziom energii
    private int age; //wiek
    private int childrenCount; // liczba dzieci

    public Animal(Vector2d pos, int energy, Genom genom){
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = pos;  // pozycja rodzicow
        this.energy = energy; // ilosc energii i jak wyglada genom tworzymy w sex-metodzie
        this.genom = genom;
        age = 0;
        childrenCount = 0;
        myId = id++;
    }

    public Animal(int width, int height, int energy, int genomLen){ // konstruktor tylko do stworzenia animali na poczatku
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = new Vector2d(random.nextInt(width),random.nextInt(height));  // tu bedzie chyba losowanie pozycji
        this.energy = energy;
        this.genom = new Genom(genomLen);
        age = 0;
        childrenCount = 0;
        myId = id++;
    }

    public void move( RectangularMap map){ //tutaj w labach jest movevalidator ktory wlasnie implementuje worldmap, mozna to potem ew dodac
        MapDirection[] mapValues = MapDirection.values();
        MapDirection[] genomTab = genom.getMoves();
        int ind = genom.getIndex();

        for (int i=0; i<mapValues[genomTab[ind].getI()].getI();i++){  // obracanie zwierzakiem jak śmigłem (od 0 do 7 razy)
            orientation = orientation.next();
        }

        Vector2d newPosition = position.add(orientation.toUnitVector());//(zmiana jego pozycji)

        if(newPosition.getX()==-1){
            newPosition = new Vector2d(map.getWidth()-1,newPosition.getY());
        }
        else if(newPosition.getX()==map.getWidth()){
            newPosition = new Vector2d(0,newPosition.getY());
        }

        if(newPosition.getY()==-1){
            newPosition = new Vector2d(newPosition.getX(),0);
            orientation = orientation.opposite();
        }
        else if(newPosition.getY()== map.getHeight()){
            newPosition = new Vector2d(newPosition.getX(), map.getHeight()-1);
            orientation = orientation.opposite();
        }
        position = newPosition;
//        if (map.canMoveTo(newPosition)) // ustawianie nowej pozycji jezeli git
//            position = newPosition; // to w sumie dla rMap sie wydaje bez sensu, bo wyzej ustawilismy tak ze zawsze musi byc git
//                                    // ale dla wody bedzie przydatne, najwyzej sie zmieni
    }

    public Vector2d getPosition(){
        return position;
    }


    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public MapDirection getOrientation() {
        return orientation;
    }

    public void decreaseEnergy(int energyLevel){
        energy -= energyLevel;
    }
    public void newKid(){
        childrenCount++;
    }

    public void nextDay(){
        age++;
    }

    public void eatGrass(int grassEnergy){
        energy += grassEnergy;
    }

    @Override
    public int compareTo(Animal other) {
        int energyComparison = Integer.compare(other.energy, this.energy);
        if (energyComparison != 0) {
            return energyComparison;
        }

        int ageComparison = Integer.compare(other.age, this.age);
        if (ageComparison != 0) {
            return ageComparison;
        }

        int childrenComparison = Integer.compare(other.childrenCount, this.childrenCount);
        if (childrenComparison != 0) {
            return childrenComparison;
        }

        return Integer.compare(new Random().nextInt(3) - 1, 0);
    }

    public Genom getGenom() {
        return genom;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    @Override
    public String toString() {
        return "/images/paw128.png";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return myId == animal.myId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myId);
    }
}