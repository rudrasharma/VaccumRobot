package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public enum RobotType {
    STANDARD, RANDOM_WALK;

    public Robot getRobot(RectangularRoom room, double speed) {
        if (this == RANDOM_WALK) {
            return new RandomWalkRobot(room, speed);
        } else {
            return new StandardRobot(room, speed);
        }
    }
}
