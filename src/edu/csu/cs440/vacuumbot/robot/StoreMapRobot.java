package edu.csu.cs440.vacuumbot.robot;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class StoreMapRobot extends Robot {

    private final Set<Position> roomPositions;
    private final Random randomGenerator;

    public StoreMapRobot(RectangularRoom room, double speed) {
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
        Position currentPosition = getPosition();
        if(roomPositions.contains(currentPosition)) {
            getRoom().cleanTileAtPosition(currentPosition);
        }
        
        List<Position> availablePositions = getAvailablePositions(currentPosition);
        
        int randomNextIndex = randomGenerator.nextInt(availablePositions.size());
        Position randomNext = availablePositions.get(randomNextIndex);
        while (!roomPositions.contains(randomNext)) {
            randomNextIndex = randomGenerator.nextInt(availablePositions.size());
            randomNext = availablePositions.get(randomNextIndex);
        }
        setPosition(randomNext);
    }

}
