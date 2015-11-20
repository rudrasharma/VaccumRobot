package edu.csu.cs440.vacuumbot.robot;

import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public class RandomWalkRobot extends Robot {

	public RandomWalkRobot(RectangularRoom room, double speed) {
		super(room, speed);
	}

	@Override
	public void updatePositionAndClean() {
		setDireciton(getRandomDirection());
		while (getRoom().isPositionInRoom(getPosition().getNewPosition(getDirection(), getSpeed()))) {
			setDireciton(getRandomDirection());
		}
		setPosition(getPosition().getNewPosition(getDirection(), getSpeed()));
		getRoom().cleanTileAtPosition(getPosition());
	}

}
