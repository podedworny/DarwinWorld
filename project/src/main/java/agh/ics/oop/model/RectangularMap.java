package agh.ics.oop.model;

import java.util.*;

public class RectangularMap {
    protected final Arguments args;
    private final int width;
    private final int height;
    protected final Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public RectangularMap(int width, int height, Arguments args) {
        this.args = args;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean canMoveTo(Vector2d position){
        return position.getY() >= 0 && position.getY() < height;
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
    }

    public void move(Animal animal){
        if (animals.get(animal.getPosition()).contains(animal)){
            removeAnimal(animal);
            animal.move(args.energyCost(), this);
            placeAnimal(animal);
        }
    }

    public void placeAnimal(Animal animal){
        if (canMoveTo(animal.getPosition())){ //zakladam tutaj ze dostarczony zwierzak ma juz ustawiona dobra pozycje
            if (animals.get(animal.getPosition()) == null){
                TreeSet<Animal> treeSet = new TreeSet<>();
                treeSet.add(animal);
                animals.put(animal.getPosition(), treeSet);
            }
            else{
                animals.get(animal.getPosition()).add(animal);
            }
        }
    }

    public void removeGrass(Vector2d vector2d){
        grasses.remove(vector2d);
    }

    public void removeAnimal(Animal animal){
        Vector2d position = animal.getPosition();
        if (animals.get(position).size() == 1)
            animals.remove(position);
        else
            animals.get(position).remove(animal);
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
                    if (animalPos.get(i + 1) != null && animalPos.get(i).getEnergy()>args.energyTaken() && animalPos.get(i+1).getEnergy()>args.energyTaken()) {
                        placeAnimal(animalCopulation(animalPos.get(i), animalPos.get(i + 1)));
                    }
                }
            }
        }
    }

    public static MapDirection[] childMoves(Animal an1, Animal an2){ //metoda do stworzenia genu dziecka

        // ręki sobie za to nie dam uciąć

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

        return newMoves;
    }

    public void eatGrass(){
        Set<Vector2d> grassPositions = grasses.keySet();
        for (Vector2d position : grassPositions){
            if (animals.get(position) != null){
                animals.get(position).first().eatGrass(args.grassEnergy());
                removeGrass(position);
            }
        }
    }

}

