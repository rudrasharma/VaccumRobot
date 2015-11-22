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
    // private Screen screen;
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
        terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        virtualScreen = new VirtualScreen(screen);
        virtualScreen.startScreen();
        virtualScreen.clear();
    }

    public double runSimulation() throws IOException {
        int totalTime = 0;
        int num = numTrials;
        TextGraphics tGraphics = virtualScreen.newTextGraphics();
        tGraphics.drawRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), '*');
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
                    // System.out.println(robot.getPosition().toString());
                    if (robot.getPosition().getX() < width && robot.getPosition().getY() < height) {
                        virtualScreen.setCharacter((int) (robot.getPosition().getX()),
                                (int) (robot.getPosition().getY()), new TextCharacter('*'));
                    }
                    virtualScreen.refresh();
                }
                totalTime += 1;
            }
            num -= 1;
        }

        return totalTime / numTrials;
    }

    public static void main(String[] args) throws IOException {
        Simulator simulator = new Simulator(1, 2, 25, 25, 1, 1, RobotType.RANDOM_WALK);
        System.out.println(simulator.runSimulation());

    }
}
