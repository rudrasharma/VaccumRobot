package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public enum RobotType {
	STANDARD, RANDOM_WALK, STORE_EXPLORED;
	
	public Robot getRobot(RectangularRoom room, double speed){
		if(this == RANDOM_WALK) {
			return new RandomWalkRobot(room, speed);
		}else if(this == STORE_EXPLORED){
			return new StoreExploredRobot(room, speed);
		}else {
			return new StandardRobot(room, speed);
		}
	}
}
