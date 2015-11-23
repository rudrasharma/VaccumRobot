package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class StandardRobot extends Robot {

	public StandardRobot(RectangularRoom room, double speed) {
		super(room, speed);
	}

	@Override
	public void updatePositionAndClean() {
		if (getRoom().isCleanable(getPosition().getNewPosition(getDirection(), getSpeed()))) {
			setPosition(getPosition().getNewPosition(getDirection(), getSpeed()));
			getRoom().cleanTileAtPosition(getPosition());
		} else {
			setDireciton(getRandomDirection());
		}
	}


}
