package edu.csu.cs440.vacuumbot.robot;

import java.util.Random;

import edu.csu.cs440.vacuumbot.environment.Position;
import edu.csu.cs440.vacuumbot.environment.RectangularRoom;

public abstract class Robot {

	private final static int MAX_ANGLE_RANGE = 360;
	private int direction;
	private RectangularRoom room;
	private final double speed;
	private Position position;

	public Robot(RectangularRoom room, double speed) {
		Random random = new Random();
		this.direction = getRandomDirection();
		this.room = room;

		if (speed > 0) {
			this.speed = speed;
		} else {
			throw new IllegalArgumentException("Speed cannot be less then 0");
		}
		position = new Position(room.getWidth() * random.nextDouble(), room.getHeight() * random.nextDouble());

	}

	public RectangularRoom getRoom() {
		return room;
	}

	public void setRoom(RectangularRoom room) {
		this.room = room;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getDirection() {
		return direction;
	}

	public void setDireciton(int direction) {
		this.direction = direction;
	}

	public abstract void updatePositionAndClean();

	public double getSpeed() {
		return speed;
	}

	protected int getRandomDirection() {
		return (int) (Math.random() * MAX_ANGLE_RANGE);
	}
}
