package agh.ics.oop.model;

import java.util.*;

public class Animal implements WorldElement{
    private final int myId;
    private Vector2d position; // pozycja
    private MapDirection orientation; // orientacja
    private final Genom genom; //nasz genom
    private int energy; //poziom energii
    private int age; //wiek
    private int childrenCount = 0; // liczba dzieci
    private int deathDate; // dzien smierci
    private int grassEaten; // liczba zjedzonej trawy
    protected final List<Animal> kids = new LinkedList<>(); // lista dzieci


    public Animal(Vector2d pos, int energy, Genom genom, int ID){
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = pos;  // pozycja rodzicow
        this.energy = energy; // ilosc energii i jak wyglada genom tworzymy w sex-metodzie
        this.genom = genom;
        age = 0;
        childrenCount = 0;
        myId = ID;
    }

    public Animal(int width, int height, int energy, int genomLen, int minMutation, int maxMutation, int ID){ // konstruktor tylko do stworzenia animali na poczatku
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];  // losowanie orientacji (oby nie gej)
        this.position = new Vector2d(random.nextInt(width),random.nextInt(height));  // tu bedzie chyba losowanie pozycji
        this.energy = energy;
        this.genom = new Genom(genomLen, minMutation, maxMutation);
        age = 0;
        childrenCount = 0;
        myId = ID;
    }

    public void move(IMap map){ //tutaj w labach jest movevalidator ktory wlasnie implementuje worldmap, mozna to potem ew dodac
        MapDirection[] mapValues = MapDirection.values();
        MapDirection[] genomTab = genom.getMoves();
        int ind = genom.getIndex();

        for (int i=0; i<mapValues[genomTab[ind].getI()].getI();i++)  // obracanie zwierzakiem jak śmigłem (od 0 do 7 razy)
            orientation = orientation.next();

        Vector2d newPosition = map.getNewPosition(position,orientation);//(zmiana jego pozycji)

        if (newPosition.equals(position)) orientation = orientation.opposite();
        if(map.canMoveTo(newPosition)) position = newPosition;
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

    public void nextDay(){
        age++;
    }

    public void setDeathDate(int deathDate){
        this.deathDate = deathDate;
    }

    public void eatGrass(int grassEnergy){
        energy += grassEnergy;
        grassEaten++;
    }

    public static Comparator<Animal> animalComparator(){
        return Comparator.comparing(Animal::getEnergy, Comparator.reverseOrder())
                .thenComparing(Animal::getAge, Comparator.reverseOrder())
                .thenComparing(Animal::getChildrenCount, Comparator.reverseOrder())
                .thenComparing(animal -> new Random().nextBoolean());
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

    public int getMyId() {
        return myId;
    }
    public int getIndex(){
        return genom.getIndex();
    }
    public int getDeathDate(){
        return deathDate;
    }

    public int getGrassEaten(){
        return grassEaten;
    }

    public void addKid(Animal animal){
        kids.add(animal);
        childrenCount++;
    }
    @Override
    public String toString() {
        return "paw";
    }
    public int descendantCalculate(){
        Queue<Animal> animalList = new LinkedList<>(kids);
        Set<Animal> animalSet = new HashSet<>();

        while(!animalList.isEmpty()){
            Animal currentAnimal = animalList.poll();
            if(!animalSet.contains(currentAnimal)){
                animalSet.add(currentAnimal);
                animalList.addAll(currentAnimal.kids);
            }
        }

        return animalSet.size();
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