package agh.ics.oop.model.map;

import agh.ics.oop.model.element.Water;
import agh.ics.oop.model.element.WorldElement;
import agh.ics.oop.model.simulation.Arguments;
import agh.ics.oop.model.util.MapDirection;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.MapChangeListener;

import java.util.*;

public class WaterMap extends AbstractMap {
    private final Map<Vector2d, Water> waters = new HashMap<>();
    private final List<Water> waterList = new ArrayList<>();
    private final int waterStartCount;
    private final int waterDay;
    private final int waterExtendedCount;
    private int waterCurrent=0;
    private int actualWaterExtended;

    public WaterMap(Arguments args, MapChangeListener presenter) {
        super(args, presenter);
        this.waterStartCount = args.waterNumber();
        this.waterDay = args.waterDays();
        this.waterExtendedCount = waterStartCount + (waterStartCount * args.waterPercentage() / 100);
        generateStartWater();
    }

    private void generateStartWater(){
        if (super.animalList.size() == super.fields) return;
        Random random = new Random();
        Vector2d position;

        int x = random.nextInt(super.width);
        int y = random.nextInt(super.height);

        while(animals.get(position = new Vector2d(x,y))!=null){
            x = random.nextInt(super.width);
            y = random.nextInt(super.height);
        }
        waterCurrent++;
        Water mainWater = new Water(position);
        waters.put(position, mainWater);
        waterList.add(mainWater);
        MapDirection direction;
        for (int i = 0; i< waterStartCount -1 && waterCurrent + super.animalList.size() < super.fields; i++){
            direction = MapDirection.values()[random.nextInt(4) * 2];
            for (int j = 0; j < 4; j++){
                if (mainWater.getNeighbours().get(direction) == null) {
                    position = super.getNewPosition(mainWater.getPosition(), direction);
                    if (animals.get(position) == null && canMoveTo(position)){
                        if (grasses.get(position) != null) removeGrass(position);
                        waterCurrent++;
                        Water neighbour = new Water(position);
                        waters.put(position, neighbour);
                        updateWaterFieldAdd(mainWater);
                        updateWaterFieldAdd(neighbour);
                        waterList.add(neighbour);
                        break;
                    }
                }
                direction = direction.next().next();
            }
            int index;
            do index = random.nextInt(waterCurrent);
            while (waterList.get(index).getNeighbours().size() == 4 || (waterList.get(index).getNeighbours().size() == 3 &&
                    (waterList.get(index).getPosition().getY()==0||waterList.get(index).getPosition().getY()==super.height-1)));
            mainWater = waterList.get(index);
        }
        super.mapChanged();
    }

    @Override
    public void animalsNextDate(){
        super.animalsNextDate();
        if (day % waterDay == 0) {
            if (day % (2 * waterDay) == 0)
                removeExtendWater();
            else
                extendWater();
        }
    }

    private void extendWater(){
        Random random = new Random();
        int waterC=0;
        int index;
        Water mainWater;
        do index = random.nextInt(waterCurrent);
        while (waterList.get(index).getNeighbours().size() == 4 || (waterList.get(index).getNeighbours().size() == 3 &&
                (waterList.get(index).getPosition().getY()==0||waterList.get(index).getPosition().getY()==super.height-1)));
        mainWater = waterList.get(index);
        MapDirection direction;
        Vector2d position;
        for (int i = 0; i<waterExtendedCount && waterCurrent + super.animalList.size() < super.fields; i++){
            direction = MapDirection.values()[random.nextInt(4) * 2];
            for (int j = 0; j < 4; j++){
                if (mainWater.getNeighbours().get(direction) == null) {
                    position = super.getNewPosition(mainWater.getPosition(), direction);
                    if (animals.get(position) == null && canMoveTo(position)){
                        if (grasses.get(position) != null) removeGrass(position);
                        waterCurrent++;
                        waterC++;
                        Water neighbour = new Water(position);
                        waters.put(position, neighbour);
                        updateWaterFieldAdd(mainWater);
                        updateWaterFieldAdd(neighbour);
                        waterList.add(neighbour);
                        break;
                    }
                }
                direction = direction.next().next();
            }
            do index = random.nextInt(waterCurrent);
            while (waterList.get(index).getNeighbours().size() == 4 || (waterList.get(index).getNeighbours().size() == 3 &&
                    (waterList.get(index).getPosition().getY()==0||waterList.get(index).getPosition().getY()==super.height-1)));
            mainWater = waterList.get(index);
        }
        actualWaterExtended = waterC;
        super.mapChanged();
    }

    private void removeExtendWater(){
        int startIndex = Math.max(0, waterCurrent - actualWaterExtended);
        Collections.shuffle(waterList);
        for (int i = waterCurrent - 1; i >= startIndex; i--) {
            updateWaterFieldRemove(waterList.get(i));
            waters.remove(waterList.get(i).getPosition());
            waterList.remove(i);
            waterCurrent--;
        }
        super.mapChanged();
    }

    private void updateWaterFieldAdd(Water water){
        for (int i = 0; i < 8; i+=2){
            MapDirection direction = MapDirection.values()[i];
            Vector2d newPosition = super.getNewPosition(water.getPosition(),direction);
            if (newPosition == water.getPosition()) continue;
            if (waters.get(newPosition) != null && water.getNeighbours().get(direction) == null)
                water.addNeighbour(direction,waters.get(newPosition));
            if (waters.get(newPosition) != null && waters.get(newPosition).getNeighbours().get(direction.opposite())==null)
                waters.get(newPosition).addNeighbour(direction.opposite(),water);
        }
    }

    private void updateWaterFieldRemove(Water water){
        for (int i = 0; i < 8; i+=2){
            MapDirection direction = MapDirection.values()[i];
            Vector2d newPosition = super.getNewPosition(water.getPosition(),direction);
            if (newPosition == water.getPosition()) continue;
            if (waters.get(newPosition) != null && water.getNeighbours().get(direction) != null)
                water.removeNeighbour(direction);
            if (waters.get(newPosition) != null && waters.get(newPosition).getNeighbours().get(direction.opposite()) != null)
                waters.get(newPosition).removeNeighbour(direction.opposite());
        }
    }

    @Override
    public WorldElement objectAt(Vector2d position){
        if(super.objectAt(position) != null) return super.objectAt(position);
        return waters.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position){
        return super.canMoveTo(position) && waters.get(position) == null;
    }

    @Override
    public boolean isWaterMap() {
        return true;
    }
}
