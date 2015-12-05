package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public enum RobotType {
	STORE_EXPLORED, STORE_EXPLORED_PROXIMITY, STORE_MAP_ROBOT, RANDOM_ROBOT, STORE_EXPLORED_NODES_AND_MAP;
	
	public Robot getRobot(RectangularRoom room, double speed){
		if(this == STORE_EXPLORED){
			return new StoreExploredRobot(room, speed);
		}else if(this == STORE_EXPLORED_PROXIMITY){
			return new StoreExploredProximityRobot(room, speed);
		}else if(this == STORE_MAP_ROBOT) {
		    return new StoreMapRobot(room, speed);
		}else if(this == RANDOM_ROBOT) {
            return new RandomRobot(room, speed);
        }else if(this == STORE_EXPLORED_NODES_AND_MAP) {
            return new StoreExploredMap(room, speed);
        }
		return null;
	}
}
