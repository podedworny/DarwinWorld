package agh.ics.oop.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genom {
    private final MapDirection[] moves; // <- tablica z naszymi genami (kolejnymi ruchami)
    private int index; // <- index gdzie aktualnie znajdujemy się w tablicy
//    private MapDirection[] directions;
    public Genom(int len) {
        Random random = new Random();
        MapDirection[] directions = MapDirection.values();
        MapDirection[] moves = new MapDirection[len];
        for (int i=0; i<len; i++){
            moves[i] = directions[random.nextInt(directions.length)];
        }
        this.moves = moves;
        this.index = random.nextInt(moves.length);
    }

    public Genom(MapDirection[] moves){
        Random random = new Random();
        this.moves = moves;
        this.index = random.nextInt(moves.length);
        mutation();
    }

    private void mutation(){
        // wybieramy liczbe mutacji -> tworzymy liste indexow i mieszamy ja -> bierzemy tylko n liczbe mutacji
        // dany index zmieniamy / narazie jest szansa ze moze byc ten sam ruch po mutacji ale to mozna zmienic
        Random random = new Random();
        int mutationCount = random.nextInt(moves.length);
        List<Integer> indexes = IntStream.range(0,moves.length).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);
        indexes.subList(0,mutationCount);
        MapDirection[] directions = MapDirection.values();
        for (Integer index : indexes) {
            moves[index] = directions[random.nextInt(directions.length)];
        }
    }

    public void nextIndexVariant(){  //wariant 3
        Random random = new Random();
        if(random.nextInt(10)<8){
            if (index+1 == moves.length) {
                index = 0;
            }
            else {
                index++;
            }
        }
        else{
            index = random.nextInt(moves.length);
        }
    }
    public void nextIndexDefault(){
        if (index+1 == moves.length) {
            index = 0;
        }
        else {
            index++;
        }
    }
    public int getIndex() {
        return index;
    }

    public MapDirection[] getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return Arrays.toString(moves);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genom genom = (Genom) o;
        return Arrays.equals(moves, genom.moves);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(index);
        result = 31 * result + Arrays.hashCode(moves);
        return result;
    }
}
