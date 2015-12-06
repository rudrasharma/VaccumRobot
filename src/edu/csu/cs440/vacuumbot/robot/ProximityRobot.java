package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by joshuaodell on 12/5/15.
 */
public class ProximityRobot extends Robot{
    private class PriorityPosition{
        double priority;
        Position position;

        public PriorityPosition(double priority, Position position) {
            this.priority = priority;
            this.position = position;
        }
    }


    List<Position> explored = new ArrayList<>();
    public ProximityRobot(RectangularRoom room, double speed) {
        super(room, speed);
        setDireciton(180);
    }
    boolean foundEdge = false;
    double curve = 45;
    double test = 1;
    double time = -0.01;

    @Override
    public void updatePositionAndClean() {
        Position p = getPosition();
        if (closeToEdge(p) == 0){
           foundEdge = true;
        }

        if (foundEdge){
            if (getRoom().isCleanable(getPosition().getNewPosition(getDirection(), getSpeed()))) {
                setPosition(getPosition().getNewPosition(getDirection(), getSpeed()));
                getRoom().cleanTileAtPosition(getPosition());
                if (getRoom().isPositionInRoom(getPosition())) {
                    setPosition(getPosition());
                    getRoom().cleanTileAtPosition(getPosition());
                }
            } else {
                setDireciton(getRandomDirection());
            }
        }else{
            int newDirection = getDirection() + (int)curve;
            newDirection = (newDirection + 360) % 360;
            setDireciton(newDirection);

            List<Position> availablePositions = getAvailablePositions(getPosition());
            int randomNextIndex = (getDirection()) / 45;

            Position randomNext = availablePositions.get(randomNextIndex);


//            List<Position> availablePositions = getAvailablePositions(p);
//            int randomNextIndex = new Random().nextInt(10) > 2 ? 1 : 0;
//            Position randomNext = availablePositions.get(randomNextIndex);
            if (getRoom().isPositionInRoom(randomNext)) {
                setPosition(randomNext);
                getRoom().cleanTileAtPosition(randomNext);
            }
            test = ( Math.exp( (time)) * 45);
            curve = 22.5 + test;
            time -= 0.01;
        }
    }

    static int proximitySensitivity = 20;
    // 0 is the closes to edge
    double closeToEdge(Position p){
        if (p.getX() < getRoom().getWidth() / 2){
            int dx = p.getX();
            if (p.getY() < getRoom().getHeight() / 2){
                //  top left
                int dy = p.getY();
                if (p.getX() < proximitySensitivity || p.getY() < proximitySensitivity){
                    return Math.min(dx, dy);
                }
            }else{
                // bottom left
                int dy = (getRoom().getHeight() - p.getY());
                if (p.getX() < proximitySensitivity || (getRoom().getHeight() - p.getY()) > proximitySensitivity){
                    return Math.min(dx, dy);
                }
            }
        }else{
            int dx = (getRoom().getWidth() - p.getX());
            if (p.getY() < getRoom().getHeight() / 2){
                // top right
                int dy = p.getY();
                if (p.getX() < proximitySensitivity || p.getY() < proximitySensitivity){
                    return Math.min(dx, dy);
                }
            }else{
                // bottom right
                int dy = (getRoom().getHeight() - p.getY());
                if (p.getX() < proximitySensitivity || (getRoom().getHeight() - p.getY()) > proximitySensitivity){
                    return Math.min(dx, dy);
                }
            }
        }
        return proximitySensitivity;
    }
}
