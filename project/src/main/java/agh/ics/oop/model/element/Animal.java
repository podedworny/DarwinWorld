package agh.ics.oop.model.element;

import agh.ics.oop.model.map.IMap;
import agh.ics.oop.model.util.Genome;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;

public class Animal implements WorldElement {
    private final int myId;
    private Vector2d position;
    private MapDirection orientation;
    private final Genome genome;
    private int energy;
    private int age;
    private int childrenCount = 0;
    private int deathDate;
    private int grassEaten;
    protected final List<Animal> kids = new LinkedList<>();

    public Animal(Vector2d pos, int energy, Genome genome, int ID){
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];
        this.position = pos;
        this.energy = energy;
        this.genome = genome;
        age = 0;
        childrenCount = 0;
        myId = ID;
    }

    public Animal(int width, int height, int energy, int genomeLen, int minMutation, int maxMutation, int ID){
        MapDirection[] directions = MapDirection.values();
        Random random = new Random();
        this.orientation = directions[random.nextInt(directions.length)];
        this.position = new Vector2d(random.nextInt(width),random.nextInt(height));
        this.energy = energy;
        this.genome = new Genome(genomeLen);
        age = 0;
        childrenCount = 0;
        myId = ID;
    }

    public void move(IMap map){
        MapDirection[] mapValues = MapDirection.values();
        List<MapDirection> genomeTab = genome.getMoves();
        int ind = genome.getIndex();

        for (int i=0; i<mapValues[genomeTab.get(ind).getI()].getI();i++)
            orientation = orientation.next();

        Vector2d newPosition = map.getNewPosition(position,orientation);

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

    public Genome getGenome() {
        return genome;
    }
    public int getGrassEaten(){
        return grassEaten;
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
        return genome.getIndex();
    }
    public int getDeathDate(){
        return deathDate;
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