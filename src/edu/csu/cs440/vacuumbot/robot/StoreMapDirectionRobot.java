package edu.csu.cs440.vacuumbot.robot;

import java.util.HashSet;
import java.util.Set;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class StoreMapDirectionRobot extends Robot {
    
    private final Set<Position> roomPositions;
    //clean from east to west, and west to east
    private Direction cleanDirection;
    //when at the edge move north or south
    private Direction moveStrategy;

    public StoreMapDirectionRobot(RectangularRoom room, double speed) {
        super(room, speed);
        roomPositions = getRoomPositions(room.getWidth(), room.getHeight());
        cleanDirection = getRandom(Direction.EAST, Direction.WEST);
        moveStrategy = getRandom(Direction.NORTH, Direction.WEST);
    }
    
    private Direction getRandom(Direction... choices){
        return choices[(int)Math.random()*choices.length];
    }
    //only return east or west
    private Direction getOpposite(Direction direction) {
        if(direction == Direction.EAST) {
            return Direction.WEST;
        }else if(direction == Direction.WEST) {
            return Direction.EAST;
        }else if(direction == Direction.NORTH) {
            return Direction.SOUTH;
        }
        return Direction.NORTH;
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
        Position forwardPoint = move(currentPosition, cleanDirection);
        if (!roomPositions.contains(forwardPoint)) {
            forwardPoint = move(currentPosition, moveStrategy);
            if (!roomPositions.contains(forwardPoint)) {
                moveStrategy = getOpposite(moveStrategy);
                forwardPoint = move(currentPosition, moveStrategy);
            }
            cleanDirection = getOpposite(cleanDirection);
        }
        setPosition(forwardPoint);
    }
    
    private Position move(Position p, Direction dir){
        if(dir==Direction.EAST) {
            return new Position(p.getX() + 1, p.getY() - 0);
        }else if(dir==Direction.WEST) {
            return new Position(p.getX() - 1, p.getY() - 0);
        }else if(dir==Direction.NORTH) {
            return new Position(p.getX() - 0, p.getY() + 1);
        }else if(dir==Direction.SOUTH) {
            return  new Position(p.getX() - 0, p.getY() - 1);
        }
        return null;
    }
  
    private enum Direction {EAST, WEST, NORTH, SOUTH};

}
