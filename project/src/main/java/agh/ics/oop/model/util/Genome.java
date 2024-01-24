package agh.ics.oop.model.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genome {
    private final List<MapDirection> moves;
    private int index;

    public Genome(int len) {
        Random random = new Random();
        MapDirection[] directions = MapDirection.values();
        List<MapDirection> moves = new ArrayList<>();
        for (int i=0; i<len; i++){
            moves.add(directions[random.nextInt(directions.length)]);
        }
        this.moves = moves;
        this.index = random.nextInt(len);
    }

    public Genome(MapDirection[] moves, int minMutation, int maxMutation){
        Random random = new Random();
        this.moves = Arrays.asList(moves);
        this.index = random.nextInt(moves.length);

        if(minMutation != 0 && maxMutation != 0) mutation(minMutation, maxMutation);
    }

    private void mutation(int minMutation, int maxMutation){
        Random random = new Random();
        int mutationCount = (minMutation==maxMutation) ? minMutation : random.nextInt(maxMutation - minMutation) + minMutation;
        List<Integer> indexes = IntStream.range(0,moves.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);
        MapDirection[] directions = MapDirection.values();
        for (Integer index : indexes.subList(0,mutationCount)) {
            moves.set(index, directions[random.nextInt(directions.length)]);
        }
    }

    public void nextIndexVariant(){
        Random random = new Random();
        if(random.nextInt(10)<8)
            nextIndexDefault();
        index = random.nextInt(moves.size());

    }

    public void nextIndexDefault(){
        if (index+1 == moves.size())
            index = 0;
        else
            index++;
    }
    public int getIndex() {
        return index;
    }

    public List<MapDirection> getMoves() {
        return moves;
    }

    @Override
    public String toString() {
        return moves.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return Objects.equals(moves, genome.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves, index);
    }
}
