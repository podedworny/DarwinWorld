package agh.ics.oop;

import agh.ics.oop.model.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads = new ArrayList<>();
    private ExecutorService executorService;

    public SimulationEngine(List<Simulation> simulations){
        this.simulations = simulations;
    }

    public void runSync(){
        for (Simulation simulation : simulations){
            simulation.run();
        }
    }

    public void runAsync(){
        for (Simulation simulation : simulations){
            threads.add(new Thread(simulation));
        }
        for(Thread thread : threads){
            thread.start();
        }
    }

    public void stopSimulations() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

}
