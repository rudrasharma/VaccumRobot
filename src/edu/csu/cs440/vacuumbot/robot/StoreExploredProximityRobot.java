package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuaodell on 11/27/15.
 * This robot has the ability store a list of places where it has already been.
 * the implementation of this is done by allowing the robot to retain a list of
 * positions that have been visited
 *
 * High Level algorith.  Clean the tile adjacent to those that are already clean
 *                      avoid edges when possible
 *
 * We use a greedy best first for this, since the problem space is not known.
 */
public class StoreExploredProximityRobot extends Robot {
    private class PriorityPosition{
        double priority;
        Position position;

        public PriorityPosition(double priority, Position position) {
            this.priority = priority;
            this.position = position;
        }
    }


    List<Position> explored = new ArrayList<>();
    public StoreExploredProximityRobot(RectangularRoom room, double speed) {
        super(room, speed);
    }
    boolean lookingForEdge = true;

    @Override
    public void updatePositionAndClean() {
        Position p = getPosition();
        if (closeToEdge(p) == 0){
            //lookingForEdge = false;
        }

        if (explored.size() > 1 && explored.get(explored.size()-1) == p){
            //  if the last node is the same as our current then we had no move last time
            //  which means we're stuck, and should randomly choose a direction
            int index = (int) (Math.random() * 8);
            Position newPosition = getAvailablePositions(p).get(index);
            if (getRoom().isPositionInRoom(newPosition)) {
                setPosition(newPosition);
                getRoom().cleanTileAtPosition(getPosition());
            }
        }else{
            explored.add(p);
            ArrayList<PriorityPosition> positions = new ArrayList<>();
            for (Position possible: getAvailablePositions(p)) {
                double priority = 0;
                int i = 0;
                for (Position existing: explored) {
                    if (i % 3 == 0) {
                        double d = existing.distanceFrom(possible);
                        // if it's the same place we don't want it if we can avoid it.
                        priority += d == 0 ? explored.size() : d;
                    }
                    i++;
                }
                priority += lookingForEdge ? closeToEdge(possible) * proximitySensitivity : 0;
                positions.add(new PriorityPosition(priority, possible));
            }
            positions.sort((o1, o2) -> Double.compare(o1.priority, o2.priority));

            for (int i = 0; i < positions.size(); i++){
                Position newPosition = positions.get(i).position;
                if (getRoom().isPositionInRoom(newPosition)) {
                    setPosition(newPosition);
                    getRoom().cleanTileAtPosition(getPosition());
                    return;
                }
            }
            System.out.println("could not find a move");
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
                    return (dx + dy) / 2.0;
                }
            }else{
                // bottom left
                int dy = (getRoom().getHeight() - p.getY());
                if (p.getX() < proximitySensitivity || (getRoom().getHeight() - p.getY()) > proximitySensitivity){
                    return (dx + dy) / 2.0;
                }
            }
        }else{
            int dx = (getRoom().getWidth() - p.getX());
            if (p.getY() < getRoom().getHeight() / 2){
                // top right
                int dy = p.getY();
                if (p.getX() < proximitySensitivity || p.getY() < proximitySensitivity){
                    return (dx + dy) / 2.0;
                }
            }else{
                // bottom right
                int dy = (getRoom().getHeight() - p.getY());
                if (p.getX() < proximitySensitivity || (getRoom().getHeight() - p.getY()) > proximitySensitivity){
                    return (dx + dy) / 2.0;
                }
            }
        }
        return proximitySensitivity;
    }
}
