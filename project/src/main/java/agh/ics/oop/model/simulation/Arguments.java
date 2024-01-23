package agh.ics.oop.model.simulation;

public record Arguments(String mapType, int mapWidth, int mapHeight,
                        int grassEnergy, int copulationEnergy,int animalEnergy,
                        int energyCost, int animalInitNumber, int grassEachDay, int coolDown,
                        int grassAtStart, int energyTaken, int minMut, int maxMut, int genomLenght,
                        String variant, int waterNumber, int waterPercentage, int waterDays
                        ){}

