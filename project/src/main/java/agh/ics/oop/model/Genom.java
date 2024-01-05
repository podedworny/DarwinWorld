package agh.ics.oop.model;

import java.util.Random;

public class Genom {
    private MapDirection[] moves; // <- tablica z naszymi genami (kolejnymi ruchami)
    private int index; // <- index gdzie aktualnie znajdujemy się w tablicy

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
        //mutacja do zrobienia
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
    public void nextIndexDefult(){
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
}
