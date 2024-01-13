package agh.ics.oop.model;

import java.util.*;

import static java.lang.Math.round;

public abstract class AbstractMap implements IMap{
    private final List<MapChangeListener> observers = new ArrayList<>();
    private final List<Animal> allAnimalList = new LinkedList<>(); // wszystkie zyjace zwierzaki kiedykolwiek
    protected final List<Animal> animalList = new LinkedList<>();
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    protected final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<MapDirection[], Integer> genomDictionary = new HashMap<>();
    private final Animal fatherOfFathers = new Animal(new Vector2d(0,0),0,new Genom(1, 1,1));
    protected final Arguments args;
    protected final int width;
    protected final int height;
    protected final int fields;
    protected int day = 0;
    private int sumDaysOfDeadAnimals=0;
    private int deadAnimals = 0;
    private int grassFields = 0;
    private int equatorBottom = 0;
    private int equatorHeight = 0;
    private int equatorFields = 0;
    private int equatorGrassFields = 0;
    private int worseFieldsGrass = 0;
    private int worseFields = 0;

    public AbstractMap(Arguments args, MapChangeListener observer){
        this.args = args;
        this.width = args.mapWidth();
        this.height = args.mapHeight();
        this.fields = this.width * this.height;
        this.equatorBottom =  this.height * 2 / 5;
        this.equatorHeight =  this.height / 5 + this.height % 2;
        this.equatorFields = this.width * equatorHeight;
        this.worseFields = this.fields - this.equatorFields;
        addObserver(observer);

        placeStartAnimals(args);
        placeStartGrass(args.grassAtStart());
    }

    public void move(Animal animal){
        if (animals.get(animal.getPosition()).contains(animal)){
            removeAnimal(animal);
            animal.move(this);
            placeAnimal(animal);
            animal.decreaseEnergy(args.energyCost());
            if (args.variant().equals("Default"))
                animal.getGenom().nextIndexDefault();
            else
                animal.getGenom().nextIndexVariant();
        }
    }

    public void moveAnimals(){
        for (Animal animal : animalList)
            move(animal);
    }

    private void placeStartAnimals(Arguments args) {
        for (int i = 0; i< args.animalInitNumber(); i++){
            Animal animal = new Animal(width,height, args.animalEnergy(), args.genomLenght(), args.minMut(), args.maxMut());
            animalList.add(animal);
            allAnimalList.add(animal);
            placeAnimal(animal);
            genomDictionary.compute(animal.getGenom().getMoves(), (key, value) -> (value == null) ? 1 : value + 1);
        }
    }

    public void placeGrass(int grassCount, boolean checkCondition) {
        Random random = new Random();
        for (int i = 0; i < grassCount && grassFields < fields; i++) {
            boolean status = random.nextInt(100) < 80;
            int x = random.nextInt(width);
            int y = setPositionY(status);

            Vector2d position;
            while (grasses.get(position = new Vector2d(x, y)) != null &&
                    ((status && equatorGrassFields < equatorFields) || (!status && worseFieldsGrass < worseFields))) {
                x = random.nextInt(width);
                y = setPositionY(status);
            }

            if (grasses.get(position) == null && (!checkCondition || canMoveTo(position))) {
                if (status && equatorGrassFields < equatorFields) equatorGrassFields++;
                if (!status && worseFieldsGrass < worseFields) worseFieldsGrass++;
                grasses.put(position, new Grass(position, args.grassEnergy()));
                grassFields++;
            }
        }
//        if (!checkCondition) {
            day++;
//        }
        mapChanged();
    }

    public void placeStartGrass(int grassCount) {
        placeGrass(grassCount, false);
    }

    public void placeNewGrass(int grassCount) {
        placeGrass(grassCount, true);
    }

    private int setPositionY(boolean status){ // ================
        Random random = new Random();
        if (status) {
            return equatorBottom + random.nextInt(equatorHeight);
        }
        else {
            int y = random.nextInt(height - equatorHeight);
            if (y < equatorBottom)
                return y;
            else
                return y + equatorHeight;
        }
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

    public boolean canMoveTo(Vector2d position){
        return position.getY() >= 0 && position.getY() < height && position.getX() >= 0 && position.getX() <= width;
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

    public Vector2d getNewPosition(Vector2d position, MapDirection direction){
        Vector2d newPosition = position.add(direction.toUnitVector());
        if (newPosition.getY() == -1) return position;
        if (newPosition.getY() == height) return position;
        if (newPosition.getX() == -1) return new Vector2d(width-1, newPosition.getY());
        if (newPosition.getX() == width) return new Vector2d(0, newPosition.getY());
        return newPosition;
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

    protected void mapChanged(){
        for (MapChangeListener observer:observers){
            observer.mapChanged(this);
        }
    }
    public Animal getAnimal(Vector2d position){
        return animals.get(position).get(0);
    }
}
