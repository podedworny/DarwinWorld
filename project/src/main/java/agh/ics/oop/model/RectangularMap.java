package agh.ics.oop.model;

import java.util.*;

public class RectangularMap {
    private final int width;
    private final int height;
    protected final Map<Vector2d, TreeSet<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public RectangularMap(int width, int height) {
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
            Grass grass = new Grass(position);
            grasses.put(position,grass);
        }
    }

    public void placeNewAnimal(Animal animal){
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
    public void animalCopulation(Animal mother, Animal father){
        //zakładam ze juz sprawdzono czy mają energie na sex i są na tym samym polu
        MapDirection[] moves = Genom.childMoves(mother,father);//metoda do stworzenia genu dziecka
//        Animal child = new Animal(mother.getPosition(),/*energia dzieciaka*/,new Genom(moves));

    }


// public void jedzenie_trawki

}

