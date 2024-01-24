package agh.ics.oop.model.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genom {
    private final MapDirection[] moves;
    private int index;

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

    public Genom(MapDirection[] moves, int minMutation, int maxMutation){
        Random random = new Random();
        this.moves = moves;
        this.index = random.nextInt(moves.length);

        if(minMutation != 0 && maxMutation != 0) mutation(minMutation, maxMutation);
    }

    private void mutation(int minMutation, int maxMutation){
        Random random = new Random();
        int mutationCount = (minMutation==maxMutation) ? minMutation : random.nextInt(maxMutation - minMutation) + minMutation;
        List<Integer> indexes = IntStream.range(0,moves.length).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);
        MapDirection[] directions = MapDirection.values();
        for (Integer index : indexes.subList(0,mutationCount)) {
            moves[index] = directions[random.nextInt(directions.length)];
        }
    }

    public void nextIndexVariant(){
        Random random = new Random();
        if(random.nextInt(10)<8)
            nextIndexDefault();
        else if(moves.length>1){
            int newIndex;
            do {
                newIndex = random.nextInt(moves.length);
            } while (newIndex == index);
            index = newIndex;
        }
    }

    public void nextIndexDefault(){
        if (index+1 == moves.length)
            index = 0;
        else
            index++;
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
