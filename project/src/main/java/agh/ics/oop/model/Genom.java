package agh.ics.oop.model;

import java.util.Random;

public class Genom {
    private MapDirection[] genom; // <- tablica z naszymi genami (kolejnymi ruchami)
    private int index; // <- index gdzie aktualnie znajdujemy się w tablicy

    public Genom(MapDirection[] genom) {
        Random random = new Random();
        this.genom = genom;
        this.index = random.nextInt(genom.length);
    }

    public int getIndex() {
        return index;
    }

    public MapDirection[] getGenom() {
        return genom;
    }
}
