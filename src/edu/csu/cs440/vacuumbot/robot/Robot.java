package edu.csu.cs440.vacuumbot.robot;

import java.util.ArrayList;
import java.util.List;
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
		int x = (int)(room.getWidth() * random.nextDouble());
		int y = (int)(room.getHeight() * random.nextDouble());
		position = new Position(x,y);

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

	protected List<Position> getAvailablePositions(Position p){
		ArrayList<Position> returning = new ArrayList<>();

		//  0   3   5
		//  1       6
		//  2   4   7
		returning.add(new Position(p.getX() - 1, p.getY() - 1));
		returning.add(new Position(p.getX() - 1, p.getY() - 0));
		returning.add(new Position(p.getX() - 1, p.getY() + 1));
		returning.add(new Position(p.getX() - 0, p.getY() - 1));
		returning.add(new Position(p.getX() - 0, p.getY() + 1));
		returning.add(new Position(p.getX() + 1, p.getY() - 1));
		returning.add(new Position(p.getX() + 1, p.getY() - 0));
		returning.add(new Position(p.getX() + 1, p.getY() + 1));

		return returning;
	}

	@Override
	public String toString() {
		return "Robot{" +
				"direction=" + direction +
				", room=" + room +
				", speed=" + speed +
				", position=" + position +
				'}';
	}
}
