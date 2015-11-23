package edu.csu.cs440.vacuumbot.simulation;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.screen.VirtualScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import edu.csu.cs440.vacuumbot.environment.Position;
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
	private Terminal terminal;
//	private SwingTerminal swingTerminal;
	private Screen screen;
	private VirtualScreen virtualScreen;

	public Simulator(int numRobots, double speed, int width, int height, double minCoverage, int numTrials,
			RobotType type) throws IOException {
		this.numRobots = numRobots;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.minCoverage = minCoverage;
		this.numTrials = numTrials;
		this.type = type;
//		swingTerminal = new SwingTerminal();
		terminal = new DefaultTerminalFactory().createTerminal();
		Screen screen = new TerminalScreen(terminal);
		virtualScreen = new VirtualScreen(screen);
		virtualScreen.startScreen();
		virtualScreen.setMinimumSize(new TerminalSize(width,height));
		virtualScreen.clear();
	}

	public double runSimulation() throws IOException {
		int totalTime = 0;
		int num = numTrials;
		TextGraphics tGraphics = virtualScreen.newTextGraphics();
		tGraphics.drawRectangle(new TerminalPosition(0,0), new TerminalSize(width,height), '+');
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
					Position p = robot.getPosition();
					robot.updatePositionAndClean();
//					if (robot.getPosition().getY() < 0 || robot.getPosition().getY() > room.getHeight() ||
//							robot.getPosition().getX() < 0 || robot.getPosition().getX() > room.getWidth()){
//						robot.setPosition(p);
//					}
//					System.out.println(robot.getPosition().toString());
					virtualScreen.setCharacter((int)(robot.getPosition().getX()),
							(int)(robot.getPosition().getY()),
							new TextCharacter('*'));
					tGraphics.putString(2, height+1,
							robot.getPosition().toString() + "\t Complete %" +
									((double)room.getNumCleanedTiles() / room.getNumTiles()) * 100);
					virtualScreen.refresh();
				}
				totalTime += 1;
			}
			num -= 1;
		}
		virtualScreen.readInput();
		virtualScreen.stopScreen();

		return totalTime / numTrials;
	}

	public static void main(String[] args) throws IOException {
		Simulator simulator = new Simulator(1, 2, 80, 20, 0.40, 1, RobotType.STANDARD);
		System.out.println(simulator.runSimulation());

	}
}
