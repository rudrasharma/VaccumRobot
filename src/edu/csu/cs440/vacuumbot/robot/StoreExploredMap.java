package edu.csu.cs440.vacuumbot.robot;

import java.util.ArrayList;
import java.util.List;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class StoreExploredMap extends Robot {
    private class PriorityPosition {
        double priority;
        Position position;

        public PriorityPosition(double priority, Position position) {
            this.priority = priority;
            this.position = position;
        }
    }

    private final RectangularRoom map;
    List<Position> explored = new ArrayList<>();

    public StoreExploredMap(RectangularRoom room, double speed) {
        super(room, speed);
        map = room;
    }
    protected boolean roomContains(Position position) {
        return 0 <= position.getX() && position.getX() < map.getWidth() && 0 <= position.getY()
                && position.getY() < map.getHeight();
    }


    @Override
    public void updatePositionAndClean() {
        Position p = getPosition();
        if (explored.size() > 1 && explored.get(explored.size()-1) == p) {
            //  if the last node is the same as our current then we had no move last time
            //  which means we're stuck, and should randomly choose a direction
            int index = (int) (Math.random() * 8);
            Position newPosition = getAvailablePositions(p).get(index);
            while (!roomContains(newPosition)) {
                index = (int) (Math.random() * 8);
                newPosition = getAvailablePositions(p).get(index);
            }
                setPosition(newPosition);
                getRoom().cleanTileAtPosition(getPosition());
        }else {
            explored.add(p);
            ArrayList<PriorityPosition> positions = new ArrayList<>();
            for (Position possible : availablePositionsInRoom(p)) {
                double priority = 0;
                for (Position existing : explored) {
                    double d = existing.distanceFrom(possible);
                    // if it's the same place we don't want it if we can avoid it.
                    priority += d == 0 ? explored.size() : d;
                }
                positions.add(new PriorityPosition(priority, possible));
            }
            positions.sort((o1, o2) -> Double.compare(o1.priority, o2.priority));
            int index = 0;
            Position newPosition = positions.get(index).position;
            while (!roomContains(newPosition)) {
                index++;
                newPosition = getAvailablePositions(p).get(index);
            }
            setPosition(newPosition);
            getRoom().cleanTileAtPosition(getPosition());
        }

    }
    
    private  List<Position> availablePositionsInRoom(Position current){
        List<Position> positionsInRoom = new ArrayList<>();
        for(Position possible: getAvailablePositions(current)) {
            if(roomContains(possible)) {
                positionsInRoom.add(possible);
            }
        }
        return positionsInRoom;
    }
}
