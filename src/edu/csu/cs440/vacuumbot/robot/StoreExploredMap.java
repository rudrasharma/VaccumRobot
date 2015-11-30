package edu.csu.cs440.vacuumbot.robot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

    private final Set<Position> roomPositions;
    private final Random randomGenerator;
    List<Position> explored = new ArrayList<>();

    public StoreExploredMap(RectangularRoom room, double speed) {
        super(room, speed);
        randomGenerator = new Random();
        roomPositions = getRoomPositions(room.getWidth(), room.getHeight());
    }

    private Set<Position> getRoomPositions(int width, int height) {
        Set<Position> positions = new HashSet<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Position roomPosition = new Position(x, y);
                positions.add(roomPosition);
            }
        }
        return positions;
    }

    @Override
    public void updatePositionAndClean() {
        Position p = getPosition();

        if (explored.size() > 1 && explored.get(explored.size() - 1) == p) {
            // if the last node is the same as our current then we had no move
            // last time
            // which means we're stuck, and should randomly choose a direction
            List<Position> availablePositions = getAvailablePositions(getPosition());

            int randomNextIndex = randomGenerator.nextInt(availablePositions.size());
            Position randomNext = availablePositions.get(randomNextIndex);
            while (!roomPositions.contains(randomNext)) {
                randomNextIndex = randomGenerator.nextInt(availablePositions.size());
                randomNext = availablePositions.get(randomNextIndex);
            }
            setPosition(randomNext);
            getRoom().cleanTileAtPosition(randomNext);
        } else {
            explored.add(p);
            ArrayList<PriorityPosition> positions = new ArrayList<>();
            for (Position possible : getAvailablePositions(p)) {
                double priority = 0;
                for (Position existing : explored) {
                    double d = existing.distanceFrom(possible);
                    // if it's the same place we don't want it if we can avoid
                    // it.
                    priority += d == 0 ? explored.size() : d;
                }
                positions.add(new PriorityPosition(priority, possible));
            }
            positions.sort((o1, o2) -> Double.compare(o1.priority, o2.priority));
            int index = 0;
            Position newPosition = positions.get(index).position;
            while (!roomPositions.contains(newPosition)) {
                index++;
                newPosition = positions.get(index).position;
            }
            setPosition(newPosition);
            getRoom().cleanTileAtPosition(getPosition());
        }
    }

}
