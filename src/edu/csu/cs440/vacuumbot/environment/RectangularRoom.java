package edu.csu.cs440.vacuumbot.environment;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RectangularRoom {

	private final int width;
	private final int height;
	private final Set<Position> cleaned;

	public RectangularRoom(int width, int height) {
		this.width = width;
		this.height = height;
		this.cleaned = new HashSet<>();
	}

	public void cleanTileAtPosition(Position position) {
		double x = Math.floor(position.getX());
		double y = Math.floor(position.getY());
		Position roundedPosition = new Position(x, y);
		if (!cleaned.contains(roundedPosition)) {
			cleaned.add(position);
		}
	}

	public boolean isTileCleaned(Position position) {
		return cleaned.contains(position);
	}

	public Position getRandomPosition() {
		Random random = new Random();
		double randomX = width * random.nextDouble();
		double randomY = height * random.nextDouble();
		return new Position(randomX, randomY);
	}

	public boolean isPositionInRoom(Position position) {
		return 0 <= position.getX() && position.getX() < this.width && 0 <= position.getX()
				&& position.getY() < this.height;
	}

	public int getNumTiles() {
		return width * height;
	}

	public int getNumCleanedTiles() {
		return cleaned.size();
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

}
