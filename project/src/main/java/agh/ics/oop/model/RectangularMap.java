package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public class RectangularMap {
    private final List<MapChangeListener> observers = new ArrayList<>();
    protected final Arguments args;
    private final int width;
    private final int height;
    protected final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public RectangularMap(Arguments args) {
        this.args = args;
        this.width = args.mapWidth();
        this.height = args.mapHeight();
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

    public boolean canMoveTo(Vector2d position){
        return position.getY() >= 0 && position.getY() < height;
    }
    public int ilosc(Vector2d position) {
        int totalAnimals = 0;

        // Iteruj przez wszystkie wartości w mapie
        for (List<Animal> animalList : animals.values()) {
            // Dodaj liczbę zwierząt w zbiorze do łącznej liczby zwierząt
            totalAnimals += animalList.size();

        }
        System.out.println(animals.get(position));
        System.out.println(animals.get(position));
        return totalAnimals;
    }



    public void move(Animal animal){
        if (animals.get(animal.getPosition()).contains(animal)){
            System.out.println("MOVE:");
            removeAnimal(animal);
            animal.move(this);
            placeAnimal(animal);
            animal.decreaseEnergy(args.energyCost());
            animal.getGenom().nextIndexDefault();
        }

    }


    public void placeAnimal(Animal animal){
//        if (canMoveTo(animal.getPosition())){ //zakladam tutaj ze dostarczony zwierzak ma juz ustawiona dobra pozycje
        Vector2d position = animal.getPosition();
        List<Animal> animalList = animals.computeIfAbsent(position, k -> new ArrayList<>());

        animalList.add(animal);

        // Sortuje listę dla określonej pozycji
        animalList.sort(Comparator.comparing(Animal::getEnergy)
                .reversed()
                .thenComparing(Animal::getAge)
                .thenComparing(Animal::getChildrenCount)
                .thenComparing(animal1 -> new Random().nextInt(3) - 1));
//        }
    }



//    public void removeAnimal(Animal animal){
////        System.out.println("PRZED " + ilosc());
//        Vector2d position = animal.getPosition();
//        if (animals.get(position).size() == 1) {
//
//            animals.remove(position);
////            System.out.println("PO1 " + ilosc());
//
//        }
//        else{
////            System.out.println("xd");
////            System.out.println(animals.get(position).size());
//            animals.get(position).remove(animal);
////            System.out.println("PO2 " + ilosc());
////            System.out.println(animals.get(position).size());
////            System.out.println("");
//        }
////        System.out.println("");
//    }
    public void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        //System.out.println("Removing animal at position: " + position);

        if (animals.containsKey(position)) {
            List<Animal> animalList = animals.get(position);
            if (animalList.remove(animal)) {
                //System.out.println("Animal removed successfully");
                if (animalList.isEmpty()) {
                    animals.remove(position);
                    //System.out.println("Empty set removed");
                }
            } else {
                //System.out.println("Animal not found in the set");
                ilosc(position);
            }
        } else {
            //System.out.println("No animals at the specified position");
        }
    }

    public Animal animalCopulation(Animal mother, Animal father){
        //zakładam ze juz sprawdzono czy mają energie na sex i są na tym samym polu
        MapDirection[] moves = childMoves(mother,father);
        Animal child = new Animal(mother.getPosition(),args.energyTaken()*2,new Genom(moves));
        placeAnimal(child);
        mother.decreaseEnergy(args.energyTaken());
        father.decreaseEnergy(args.energyTaken());
        mother.newKid();
        father.newKid();
        return child;  // wstępnie zwracamy dziecko, jesli nie bedzie potrzebne to zmienimy na voida
    }


    public void reproduce(){
        // biore wszystkie pozycje na ktorych sa zwierzaki
        // i dla kazdego pola biore dwojke (jezeli istnieje i ma energie) na sex
        // zwracam zwierzaka i klade go na plansze
        Set<Vector2d> animalsPositions = animals.keySet();
        for (Vector2d position : animalsPositions){
            if (animals.get(position).size()>1) {
                List<Animal> animalPos = new ArrayList<>(animals.get(position));
                for (int i = 0; i < animalPos.size(); i += 2) { // to tez do przepisania ale niech bedzie poki co
                    if (i + 1 < animalPos.size()  && animalPos.get(i).getEnergy()>args.energyTaken() && animalPos.get(i+1).getEnergy()>args.energyTaken()) {
                        Animal kid = animalCopulation(animalPos.get(i), animalPos.get(i + 1));
                        placeAnimal(kid);
                        Simulation.addNewAnimal(kid);
                    }
                }
            }
        }
    }

    public static MapDirection[] childMoves(Animal an1, Animal an2){ //metoda do stworzenia genu dziecka// ręki sobie za to nie dam uciąć
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

        // mutacje trzeba zrobic
        // mutacje dzieje sie automatycznie kiedy podajemy moves przy tworzeniu genomu

        return newMoves;
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

    public void removeGrass(Vector2d vector2d){
        grasses.remove(vector2d);
    }

    private int setPositionY(){
        // metoda która losuje Y // chyba dziala ale nie jestem pewny nieparzystych i małych wysokości
        Random random = new Random();
        int y;
        int mapPart = random.nextInt(10);
        if (mapPart < 8) {
            y = height / 5 + random.nextInt(height * 3 / 5 + height % 2); // równik
        }
        else if(mapPart == 8 ) {
            y = random.nextInt(height / 5); // dolna część
        }
        else {
            y = height * 4 / 5 + height % 2 + random.nextInt(height / 5); // górna część
        }
        return y;
    }

    public void placeNewGrass(int grassCount){
        Random random = new Random();
        for (int i=0; i<grassCount; i++) {
            int x = random.nextInt(width);
            int y = setPositionY();
            Vector2d position = new Vector2d(x, y);
            while (grasses.get(position)!=null){
                x = random.nextInt(width);
                y = setPositionY();
                position = new Vector2d(x, y);
            }
            Grass grass = new Grass(position,args.grassEnergy());
            grasses.put(position,grass);
        }
        mapChanged("123");
    }
    public boolean isOccupied(Vector2d position){
        return (animals.get(position) != null || grasses.get(position) != null);
    }

    public WorldElement objectAt(Vector2d position){
        if (animals.get(position) != null)
            return animals.get(position).get(0);
        else
            return grasses.get(position);
    }
    public void addObserver(MapChangeListener observer){
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }

    private void mapChanged(String message){
        for (MapChangeListener observer:observers){
            observer.mapChanged(this, message);
        }
    }


    @Override
    public String toString() {
        return new MapVisualizer(this).draw(new Vector2d(0,0),new Vector2d(width,height));
    }
}

