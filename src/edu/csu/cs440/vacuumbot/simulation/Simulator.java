package edu.csu.cs440.vacuumbot.simulation;

import java.util.HashSet;
import java.util.Set;

import edu.csu.cs440.vacuumbot.environment.RectangularRoom;
import edu.csu.cs440.vacuumbot.robot.Robot;
import edu.csu.cs440.vacuumbot.robot.RobotType;

public class Simulator {

	private final int numRobots;
	private final double speed;
	private final int width;
	private final int height;
	private final double minCoverage;
	private final int numTrials;
	private final RobotType type;

	public Simulator(int numRobots, double speed, int width, int height, double minCoverage, int numTrials,
			RobotType type) {
		this.numRobots = numRobots;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.minCoverage = minCoverage;
		this.numTrials = numTrials;
		this.type = type;
		runSimulation();
	}

	public double runSimulation() {
		int totalTime = 0;
		int num = numTrials;
		while (num > 0) {
			RectangularRoom room = new RectangularRoom(width, height);
			int i = numRobots;
			Set<Robot> robots = new HashSet<>();
			while (i > 0) {
				robots.add(type.getRobot(room, speed));
				i -= 1;
			}
			while (minCoverage * room.getNumTiles() > room.getNumCleanedTiles()) {
				for (Robot robot : robots) {
					robot.updatePositionAndClean();
				}
				totalTime += 1;
			}
			num -= 1;
		}

		return totalTime / numTrials;
	}

	public static void main(String[] args) {
		new Simulator(2, 100, 1000, 1000, 4, 5, RobotType.RANDOM_WALK);

	}
}
