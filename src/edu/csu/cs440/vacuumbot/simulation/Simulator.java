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

    private final double speed;
    private final int width;
    private final int height;
    private final int maxDepth;
    private final double threashold;
    private final RobotType type;
    private Terminal terminal;
    //	private SwingTerminal swingTerminal;
    private Screen screen;
    private VirtualScreen virtualScreen;

    public Simulator(double speed, int width, int height, int maxDepth, double threashold,
                     RobotType type) throws IOException {
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.maxDepth = maxDepth;
        this.threashold = threashold;
        this.type = type;
//		swingTerminal = new SwingTerminal();
        terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        virtualScreen = new VirtualScreen(screen);
        virtualScreen.startScreen();
        virtualScreen.setMinimumSize(new TerminalSize(width,height));
        virtualScreen.clear();
    }

    public int runSimulation() throws IOException {
        int totalTime = 0;
        TextGraphics tGraphics = virtualScreen.newTextGraphics();
        tGraphics.fillRectangle(new TerminalPosition(0,0), new TerminalSize(width,height), getAscii(175));

        RectangularRoom room = new RectangularRoom(width, height);
        Set<Robot> robots = new HashSet<>();
        robots.add(type.getRobot(room, speed));

        while (totalTime < this.maxDepth &&
                (room.getNumTiles() * this.threashold) > room.getNumCleanedTiles()) {
            for (Robot robot : robots) {
                Position p = robot.getPosition();
                robot.updatePositionAndClean();
//					System.out.println(robot.getPosition().toString());
                virtualScreen.setCharacter(robot.getPosition().getX(),
                        robot.getPosition().getY(),
                        new TextCharacter(getAscii(218)));
                if(totalTime % (this.maxDepth / 100) == 0) {
                    // only update UI every so often
                    String str = String.format("%s \t Complete %.2f \t depth %6d / %6d",
                            robot.getPosition().toString(),
                            ((double) room.getNumCleanedTiles() / room.getNumTiles()) * 100,
                            totalTime, this.maxDepth);
                    tGraphics.putString(2, height + 1, str);
                    virtualScreen.refresh();
                }
            }
            totalTime += 1;
        }
        for (Robot robot : robots) {
            String str = String.format("%s \t Complete %.2f \t depth %6d / %6d",
                    robot.getPosition().toString(),
                    ((double) room.getNumCleanedTiles() / room.getNumTiles()) * 100,
                    totalTime, this.maxDepth);
            tGraphics.putString(2, height + 1, str);
            virtualScreen.refresh();
        }
        virtualScreen.readInput();
        virtualScreen.stopScreen();

        return totalTime;
    }

    public static void main(String[] args) throws IOException {
        double completionThreadhold = 0.60;
        int[] depths = {100,1000,10000,100000,1000000};
        for (int d : depths){
            try {
                Simulator simulator = new Simulator(2, 80, 20, d, completionThreadhold, RobotType.STORE_MAP_ROBOT);
                System.out.println(simulator.runSimulation());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static final char[] EXTENDED = { 0x00C7, 0x00FC, 0x00E9, 0x00E2,
            0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
            0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
            0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
            0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
            0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
            0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
            0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
            0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
            0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
            0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
            0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
            0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
            0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
            0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
            0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
            0x207F, 0x00B2, 0x25A0, 0x00A0 };

    public static final char getAscii(int code) {
        if (code >= 0x80 && code <= 0xFF) {
            return EXTENDED[code - 0x7F];
        }
        return (char) code;
    }
}
