package agh.ics.oop.model;

import java.util.*;

import static java.lang.Math.round;

public abstract class AbstractMap implements IMap{
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final List<Animal> allAnimalList = new LinkedList<>(); // wszystkie zyjace zwierzaki kiedykolwiek
    private final List<Animal> animalList = new LinkedList<>();
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<MapDirection[], Integer> genomDictionary = new HashMap<>();
    private Animal fatherOfFathers = new Animal(new Vector2d(0,0),0,new Genom(1));
    protected final Arguments args;
    private final int width;
    protected final int height;
    private final int fields;
    private int day = 0;
    private int sumDaysOfDeadAnimals=0;
    private int deadAnimals = 0;
    private int grassFields = 0;

    public AbstractMap(Arguments args, MapChangeListener observer){
        this.args = args;
        this.width = args.mapWidth();
        this.height = args.mapHeight();
        this.fields = this.width * this.height;
        addObserver(observer);

        placeStartAnimals(args);
        placeNewGrass(args.grassAtStart());
    }

    public void move(Animal animal){
        if (animals.get(animal.getPosition()).contains(animal)){
            removeAnimal(animal);
            animal.move(this);
            placeAnimal(animal);
            animal.decreaseEnergy(args.energyCost());
            animal.getGenom().nextIndexDefault();
        }
    }

    public void moveAnimals(){
        for (Animal animal : animalList)
            move(animal);
    }

    private void placeStartAnimals(Arguments args) {
        for (int i = 0; i< args.animalInitNumber(); i++){
            Animal animal = new Animal(width,height, args.animalEnergy(), args.genomLenght());
            animalList.add(animal);
            allAnimalList.add(animal);
            placeAnimal(animal);
            genomDictionary.compute(animal.getGenom().getMoves(), (key, value) -> (value == null) ? 1 : value + 1);
        }
    }

    public void placeNewGrass(int grassCount){
        Random random = new Random();
        for (int i=0; i<grassCount; i++) {
            int x = random.nextInt(width);
            int y = setPositionY();
            Vector2d position = new Vector2d(x, y);
            while (grassFields<=fields && grasses.get(position)!=null){
                x = random.nextInt(width);
                y = setPositionY();
                position = new Vector2d(x, y);
            }
            Grass grass = new Grass(position,args.grassEnergy());
            grasses.put(position,grass);
            grassFields++;
        }
        day++;
        mapChanged();
    }

    public void eatGrass(){
        Set<Vector2d> grassPositions = grasses.keySet();
        Iterator<Vector2d> iterator = grassPositions.iterator();
        while (iterator.hasNext()){
            Vector2d position = iterator.next();
            if (animals.get(position) != null){
                iterator.remove();
                animals.get(position).get(0).eatGrass(args.grassEnergy());
                removeGrass(position);
            }
        }
    }

    public void placeAnimal(Animal animal){ // ================
        Vector2d position = animal.getPosition();
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
        animals.get(position).sort(Animal.animalComparator());
    }

    public void reproduce(){ // ================
        Set<Vector2d> animalsPositions = animals.keySet();
        for (Vector2d position : animalsPositions){
            List<Animal> animalPos = animals.get(position);
            List<Animal> kids = new ArrayList<>();
            for (int i = 0; i < animalPos.size(); i += 2) { // to tez do przepisania ale niech bedzie poki co
                if (i + 1 < animalPos.size()  && animalPos.get(i).getEnergy()>=args.energyTaken() && animalPos.get(i+1).getEnergy()>=args.energyTaken())
                    kids.add(animalCopulation(animalPos.get(i), animalPos.get(i + 1)));
            }
            for (Animal kid : kids){
                placeAnimal(kid);
                animalList.add(kid);
                allAnimalList.add(kid);
                genomDictionary.compute(kid.getGenom().getMoves(), (key, value) -> (value == null) ? 1 : value + 1);
            }
        }
    }

    public Animal animalCopulation(Animal mother, Animal father){ // ================
        MapDirection[] moves = childMoves(mother,father);
        Animal child = new Animal(mother.getPosition(),args.energyTaken()*2,new Genom(moves));
        mother.decreaseEnergy(args.energyTaken());
        father.decreaseEnergy(args.energyTaken());
        mother.newKid();
        mother.addKid(child);
        father.newKid();
        father.addKid(child);
        return child;
    }

    public void deleteDeadAnimals(){ // ================
        Iterator<Animal> iterator = animalList.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.getEnergy() <= 0) {
                animal.setDeathDate(day);
                deadAnimals++;
                sumDaysOfDeadAnimals += animal.getAge();
                genomDictionary.compute(animal.getGenom().getMoves(), (key, value) -> (value <= 1) ? null : value - 1);
                iterator.remove();
                removeAnimal(animal);
            }
        }
    }

    public void removeAnimal(Animal animal) { // ================
        Vector2d position = animal.getPosition();
        if (animals.containsKey(position)) {
            List<Animal> animalList = animals.get(position);
            if (animalList.remove(animal) && animalList.isEmpty())
                animals.remove(position);
        }
    }

    public static MapDirection[] childMoves(Animal an1, Animal an2){ // ================
        Random random = new Random();
        MapDirection[] newMoves = new MapDirection[an1.getGenom().getMoves().length];
        MapDirection[] moves1 = an1.getGenom().getMoves();
        MapDirection[] moves2 = an2.getGenom().getMoves();
        int en1 = an1.getEnergy();
        int en2 = an2.getEnergy();

        int side = random.nextInt(2);

        if (en1 > en2) {
            int ind = (an1.getGenom().getMoves().length * en1 / (en1 + en2));
            if (side == 0) { // lewa strona en1
                System.arraycopy(moves1, 0, newMoves, 0, ind);
                System.arraycopy(moves2, ind, newMoves, ind, moves1.length - ind);
            } else { //prawa strona en1
                System.arraycopy(moves2, 0, newMoves, 0, ind);
                System.arraycopy(moves1, ind, newMoves, ind, moves1.length - ind);
            }
        }
        else{
            int ind = (an1.getGenom().getMoves().length * en2 / (en1 + en2));
            if (side == 0) { // lewa strona en2
                System.arraycopy(moves2, 0, newMoves, 0, ind);
                System.arraycopy(moves1, ind, newMoves, ind, moves1.length - ind);
            } else { //prawa strona en2
                System.arraycopy(moves1, 0, newMoves, 0, ind);
                System.arraycopy(moves2, ind, newMoves, ind, moves1.length - ind);
            }
        }
        return newMoves;
    }

    private int setPositionY(){ // ================
        Random random = new Random();
        int y;
        int mapPart = random.nextInt(10);
        if (mapPart < 8)
            y = height *2 / 5 + random.nextInt(height / 5 + height % 2); // równik
        else if(mapPart == 8 )
            y = random.nextInt(height * 2 / 5); // dolna część
        else
            y = height * 3 / 5 + height % 2 + random.nextInt(height * 2/ 5); // górna część
        return y;
    }

    public MapDirection[] getMostPopularGenom(){ // ================
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

    public void removeGrass(Vector2d vector2d){  // ================
        grasses.remove(vector2d);
        grassFields--;
    }

    public boolean isOccupied(Vector2d position){
        return (animals.get(position) != null || grasses.get(position) != null);
    }

    public WorldElement objectAt(Vector2d position){ // ================
        if (animals.get(position) != null)
            return animals.get(position).get(0);
        else
            return grasses.get(position);
    }

    public void animalsNextDate(){ // ================
        for (Animal animal : animalList)
            animal.nextDay();
    }

    public void descendantCounting() {
        fatherOfFathers.descendantCalculate();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Arguments getArgs() {
        return args;
    }

    public int getGrassFields() {
        return grassFields;
    }

    public int getDay() {
        return day;
    }

    public int numberOfAnimals() {
        return animalList.size();
    }

    public double averageEnergyLevel(){
        double result = 0.0;
        for (Animal animal : animalList)
            result += animal.getEnergy();

        return round(result / animalList.size() * 100)/100.00;
    }

    public double averageChildrenCount(){
        double result = 0.0;
        for (Animal animal : animalList)
            result += animal.getChildrenCount();
        return round(result / animalList.size() * 100)/100.00;
    }

    public double averageAge() {
        return (deadAnimals == 0) ? 0 : Math.round(((double) sumDaysOfDeadAnimals / deadAnimals) * 100.0) / 100.0;
    }

    public void addObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }

    private void mapChanged(){
        for (MapChangeListener observer:observers){
            observer.mapChanged(this);
        }
    }
}
