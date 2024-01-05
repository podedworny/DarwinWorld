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

    public static MapDirection[] childMoves(Animal an1, Animal an2){ // do dokończenia
        Random random = new Random();
        MapDirection[] newMoves = new MapDirection[an1.getGenom().getMoves().length];
//        MapDirection[] moves1 = an1.getGenom().getMoves();
//        MapDirection[] moves2 = an2.getGenom().getMoves();
//        int en1 = an1.getEnergy();
//        int en2 = an2.getEnergy();
//        int pivot = en1 / (en1 + en2);
//        int side = random.nextInt(2);
//
//        if (en1 > en2) {
//            if (side == 0) { // lewa strona en1
//                System.arraycopy(moves1, 0, newMoves, 0, pivot);
//                System.arraycopy(moves2, pivot, newMoves, pivot, moves1.length - pivot);
//            } else { //prawa strona en1
//                System.arraycopy(moves2, 0, newMoves, 0, pivot);
//                System.arraycopy(moves1, pivot, newMoves, pivot, moves1.length - pivot);
//            }
//        }
//        else{
//            if (side == 0) { // lewa strona en2
//                System.arraycopy(moves2, 0, newMoves, 0, pivot);
//                System.arraycopy(moves1, pivot, newMoves, pivot, moves1.length - pivot);
//            } else { //prawa strona en2
//                System.arraycopy(moves1, 0, newMoves, 0, pivot);
//                System.arraycopy(moves2, pivot, newMoves, pivot, moves1.length - pivot);
//            }
//        }
        return newMoves;
    }
    public void nextIndex(){  //wariant 3
        Random random = new Random();
        if(random.nextInt(10)<8){
            index++;
        }
        else{
            index = random.nextInt(moves.length);
        }
    }
    public int getIndex() {
        return index;
    }

    public MapDirection[] getMoves() {
        return moves;
    }
}
