package edu.csu.cs440.vacuumbot.robot;

import java.util.List;
import java.util.Random;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class RandomRobot extends Robot{
    
    private final Random randomGenerator;

    public RandomRobot(RectangularRoom room, double speed) {
        super(room, speed);
        randomGenerator = new Random();
    }

    @Override
    public void updatePositionAndClean() {
        Position current = getPosition();
        if(getRoom().isCleanable(current)) {
            getRoom().cleanTileAtPosition(current);
        }
        List<Position> availablePositions = getAvailablePositions(current);
        int randomNextIndex = randomGenerator.nextInt(availablePositions.size());
        Position randomNext = availablePositions.get(randomNextIndex);
        setPosition(randomNext);
    }

}
