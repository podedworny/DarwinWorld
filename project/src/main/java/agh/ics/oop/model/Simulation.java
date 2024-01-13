package agh.ics.oop.model;

import java.util.concurrent.TimeUnit;

public class Simulation implements Runnable{
    private final IMap map;
    private final int coolDown;
    private final int grassEachDay;

    private SimulationState state = SimulationState.STARTED;


    public Simulation(int coolDown, int grassEachDay, IMap map) {
        this.map = map;
        this.coolDown = coolDown;
        this.grassEachDay = grassEachDay;
    }

    public void setState(SimulationState state) {
        if(this.state!=SimulationState.FINISHED)
            this.state = state;
    }

    public void stopSimulation() {
        state = SimulationState.STOPED;
    }

    public void run(){
        while (true) {
            switch (state) {
                case STARTED -> {
                    map.deleteDeadAnimals();
                    map.moveAnimals();
                    map.eatGrass();
                    map.reproduce();
                    map.placeNewGrass(grassEachDay);
                    map.descendantCounting();
                    map.animalsNextDate();
                    try {
                        Thread.sleep(coolDown);
                    }
                    catch (Exception ignored){}
                    if(map.numberOfAnimals() == 0)
                        state = SimulationState.FINISHED;
                }
                case STOPED -> sleep(100);
                case FINISHED -> {
                    return;
                }
            }

        }
    }

    public static void sleep(int milliseconds) {
        try {
            Thread sleepingThread = new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            sleepingThread.start();
            sleepingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}