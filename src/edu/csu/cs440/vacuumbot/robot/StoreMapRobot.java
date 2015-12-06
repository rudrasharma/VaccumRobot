package edu.csu.cs440.vacuumbot.robot;

import java.util.List;
import java.util.Random;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class StoreMapRobot extends Robot {

    private final Random randomGenerator;
    private RectangularRoom map;

    public StoreMapRobot(RectangularRoom room, double speed) {
        super(room, speed);
        randomGenerator = new Random();
        this.map = room;
    }
    protected boolean roomContains(Position position) {
        return 0 <= position.getX() && position.getX() < map.getWidth() && 0 <= position.getY()
                && position.getY() < map.getHeight();
    }
    @Override
    public void updatePositionAndClean() {
        Position currentPosition = getPosition();
        if(roomContains(currentPosition)) {
            getRoom().cleanTileAtPosition(currentPosition);
        }
        
        List<Position> availablePositions = getAvailablePositions(currentPosition);
        
        int randomNextIndex = randomGenerator.nextInt(availablePositions.size());
        Position randomNext = availablePositions.get(randomNextIndex);
        while (!roomContains(randomNext)) {
            randomNextIndex = randomGenerator.nextInt(availablePositions.size());
            randomNext = availablePositions.get(randomNextIndex);
        }
        setPosition(randomNext);
    }

}
