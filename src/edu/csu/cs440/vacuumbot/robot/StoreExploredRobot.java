package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshuaodell on 11/26/15.
 * This robot has the ability store a list of places where it has already been.
 * the implementation of this is done by allowing the robot to retain a list of
 * positions that have been visited
 *
 * High Level algorith.  Clean the tile adjacent to those that are already clean
 *
 * We use a greedy best first for this, since the problem space is not known.
 */
public class StoreExploredRobot extends Robot {
    private class PriorityPosition{
        double priority;
        Position position;

        public PriorityPosition(double priority, Position position) {
            this.priority = priority;
            this.position = position;
        }
    }


    List<Position> explored = new ArrayList<>();
    public StoreExploredRobot(RectangularRoom room, double speed) {
        super(room, speed);
    }

    @Override
    public void updatePositionAndClean() {
        Position p = getPosition();

        if (explored.size() > 1 && explored.get(explored.size()-1) == p) {
            //  if the last node is the same as our current then we had no move last time
            //  which means we're stuck, and should randomly choose a direction
            int index = (int) (Math.random() * 8);
            Position newPosition = getAvailablePositions(p).get(index);
            if (getRoom().isCleanable(newPosition)) {
                setPosition(newPosition);
                getRoom().cleanTileAtPosition(getPosition());
            }
        }else {
            explored.add(p);
            ArrayList<PriorityPosition> positions = new ArrayList<>();
            for (Position possible : getAvailablePositions(p)) {
                double priority = 0;
                for (Position existing : explored) {
                    double d = existing.distanceFrom(possible);
                    // if it's the same place we don't want it if we can avoid it.
                    priority += d == 0 ? explored.size() : d;
                }
                positions.add(new PriorityPosition(priority, possible));
            }
            positions.sort((o1, o2) -> Double.compare(o1.priority, o2.priority));
            Position newPosition = positions.get(0).position;

            if (getRoom().isCleanable(newPosition)) {
                setPosition(newPosition);
                getRoom().cleanTileAtPosition(getPosition());
            }
        }
    }
}
