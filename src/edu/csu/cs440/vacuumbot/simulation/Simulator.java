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
    RectangularRoom room;
    int totalTime = 0;
    Robot robot;

    private Terminal terminal;
    private Screen screen;
    private VirtualScreen virtualScreen;
    TextGraphics tGraphics;

    public Simulator(double speed, int width, int height, int maxDepth, double threashold,
                     RobotType type, boolean showUI) throws IOException {
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.maxDepth = maxDepth;
        this.threashold = threashold;
        this.type = type;
        room = new RectangularRoom(width, height);
        robot = type.getRobot(room, speed);
        if (showUI) {
            terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            virtualScreen = new VirtualScreen(screen);
            virtualScreen.startScreen();
            virtualScreen.setMinimumSize(new TerminalSize(width, height));
            virtualScreen.clear();
            tGraphics = virtualScreen.newTextGraphics();
            tGraphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), getAscii(175));
        }
    }

    public int runSimulation() throws IOException {
        while (totalTime < this.maxDepth && (room.getNumTiles() * this.threashold) > room.getNumCleanedTiles()) {
            Position p = robot.getPosition();
            robot.updatePositionAndClean();
            if (virtualScreen != null)
                virtualScreen.setCharacter(robot.getPosition().getX(), robot.getPosition().getY(), new TextCharacter(getAscii(218)));
            if(totalTime % (this.maxDepth / 100) == 0) {
                // only update UI every so often
                String str = String.format("%s \t Complete %.2f \t depth %6d / %6d", robot.getPosition().toString(), ((double) room.getNumCleanedTiles() / room.getNumTiles()) * 100, totalTime, this.maxDepth);
                if (tGraphics != null)
                    tGraphics.putString(2, height + 1, str);
                if (virtualScreen != null)
                    virtualScreen.refresh();
            }
            totalTime += 1;
        }
        if (virtualScreen != null) {
            String str = String.format("%s \t Complete %.2f \t depth %6d / %6d", robot.getPosition().toString(), ((double) room.getNumCleanedTiles() / room.getNumTiles()) * 100, totalTime, this.maxDepth);
            tGraphics.putString(2, height + 1, str);
            virtualScreen.refresh();
            virtualScreen.readInput();
            virtualScreen.stopScreen();
        }
        return totalTime;
    }

    @Override
    public String toString() {
        return "Simulator{" +
                "maxDepth=" + maxDepth +
                ", threashold=" + threashold +
                ", type=" + type +
                ", totalTime=" + totalTime +
                '}';
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public double getThreashold() {
        return threashold;
    }

    public RobotType getType() {
        return type;
    }

    public int getTotalTime() {
        return totalTime;
    }
    public double getPercentCleaned(){
        return (double)room.getNumCleanedTiles() / (double)room.getNumTiles();
    }

    public static void main(String[] args) throws IOException {
        RunSimulations();
//        double completionThreadhold = 1;
//        int[] depths = {100,1000,10000,100000,1000000};
//        for (int d : depths){
//            try {
//                Simulator simulator = new Simulator(2, 80, 20, d, completionThreadhold, RobotType.STORE_EXPLORED_NODES_AND_MAP, true);
//                System.out.println(simulator.runSimulation());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }
    public static void RunSimulations() throws IOException{
        Set<Simulator> simulations = new HashSet<>();
        int width = 80;
        int height = 20;

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        VirtualScreen virtualScreen = new VirtualScreen(screen);
        virtualScreen.startScreen();
        virtualScreen.setMinimumSize(new TerminalSize(width, height));
        virtualScreen.clear();
        TextGraphics tGraphics = virtualScreen.newTextGraphics();
//        tGraphics.drawRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), getAscii(175));
        double[] completionThreadholds = {0.10, 0.20, 0.50, 0.80, 1.0};
        int[] depths = {1000000};

        String headingString = String.format("%30s", "Robot Type");
        for (double t : completionThreadholds){
            headingString += String.format("    %.2f", t);
        }
        tGraphics.putString(0,0, headingString);
        tGraphics.drawLine(0,1, headingString.length(), 1, "=".charAt(0));
        virtualScreen.refresh();
        int threasholdIndex = 0;
        for(double threashold : completionThreadholds){
            for (int d : depths){
                for (RobotType robotType : RobotType.values()){
                    tGraphics.putString(0,(robotType.ordinal()*3)+2, String.format("%30s", robotType.toString()));
                    try {
                        int totalDepths = 0;
                        double totalPercentages = 0;
                        int successes = 0;

                        for (int i = 1; i <= 100; i++){
                            Simulator simulator = new Simulator(2, width, height, d, threashold, robotType, false);
                            totalDepths += simulator.runSimulation();
                            totalPercentages += simulator.getPercentCleaned();
                            if (simulator.getPercentCleaned() >= threashold){
                                successes++;
                            }
                            System.out.println(simulator.toString());
                            simulations.add(simulator);
                            tGraphics.putString(0,(RobotType.values().length*3)+2, simulator.toString());

                            tGraphics.putString(30 + (threasholdIndex * 8),(robotType.ordinal()*3)+2, String.format("%8d", totalDepths / i));
                            tGraphics.putString(30 + (threasholdIndex * 8),(robotType.ordinal()*3)+3, String.format("%7.1f%%", (totalPercentages/i)*100));
                            tGraphics.putString(30 + (threasholdIndex * 8),(robotType.ordinal()*3)+4, String.format(" %3d/%3d", successes, i));
                            virtualScreen.refresh();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            threasholdIndex++;
        }
        virtualScreen.refresh();
        virtualScreen.readInput();
        virtualScreen.stopScreen();

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
