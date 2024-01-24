package agh.ics.oop.model.simulation;

import agh.ics.oop.model.map.IMap;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation implements Runnable{
    private final IMap map;
    private final int coolDown;
    private final int grassEachDay;
    private String simulationName;
    private String fileName;
    private FileWriter writer;
    private CSVWriter csvWriter;
    private SimulationState state = SimulationState.STARTED;
    private final boolean saveData;

    public Simulation(int coolDown, int grassEachDay, IMap map, boolean saveData) {
        this.map = map;
        this.coolDown = coolDown;
        this.grassEachDay = grassEachDay;
        this.saveData = saveData;

        if(saveData){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            this.simulationName = dateFormat.format(new Date());
            this.fileName = "simulationRaports/simulationreport-" + simulationName + ".csv";

            try {
                File directory = new File("simulationRaports/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                writer = new FileWriter(fileName);
                csvWriter = new CSVWriter(writer);

                String[] header = {"Day", "Number of animals", "Number of grass fields", "Most popular genom", "Average energy level", "Average child count", "Average dead animal age", "Number of animals ever lived"};
                csvWriter.writeNext(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setState(SimulationState state) {
        if(this.state!=SimulationState.FINISHED)
            this.state = state;
    }

    public SimulationState getState() {
        return state;
    }

    public void stopSimulation() {
        state = SimulationState.STOPED;
    }

//    private final Lock mapLock = new ReentrantLock();
//
//    public Lock getMapLock() {
//        return mapLock;
//    }

    public void run(){
        while (true) {
            switch (state) {
                case STARTED -> {

//                    try {
                    if(saveData) {
                        try {
                            String[] data = {
                                    String.valueOf(map.getDay()),
                                    String.valueOf(map.numberOfAnimals()),
                                    String.valueOf(map.getGrassFields()),
                                    Arrays.toString(map.getMostPopularGenom()),
                                    String.valueOf(map.averageEnergyLevel()),
                                    String.valueOf(map.averageChildrenCount()),
                                    String.valueOf(map.averageAge()),
                                    String.valueOf(map.everAnimalCount())
                            };
                            csvWriter.writeNext(data);
                            csvWriter.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                        map.deleteDeadAnimals();
                        map.moveAnimals();
                        map.eatGrass();
                        map.reproduce();
                        map.placeNewGrass(grassEachDay);
                        map.animalsNextDate();
//                    } finally {
//                        mapLock.unlock();
//                    }
                    try {
                        Thread.sleep(coolDown);
                    }
                    catch (Exception ignored){}
                    if(map.numberOfAnimals() == 0)
                        state = SimulationState.FINISHED;
                }
                case STOPED -> sleep(100);
                case FINISHED -> {
                    if(saveData)
                        closeCsvWriter();
                    return;
                }
            }
        }
    }

    public void closeCsvWriter() {
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
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