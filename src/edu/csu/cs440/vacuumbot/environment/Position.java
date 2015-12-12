package edu.csu.cs440.vacuumbot.environment;

public class Position {

	private final int x;
	private final int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Position getNewPosition(double angle, double speed) {
		double oldX = this.x;
		double oldY = this.y;

		// TODO: Verify Math.toRadians is equals to math.radians in python
		double deltaY = (Math.cos(Math.toRadians(angle))) * speed;
		double deltaX = (Math.sin(Math.toRadians(angle))) * speed;

		int newX = (int)(oldX + deltaY);
		int newY = (int)(oldY + deltaX);

		return new Position(newX, newY);
	}


	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

	public double distanceFrom(Position p){
		return Math.abs(p.getX() - this.getX()) + Math.abs(p.getY() - this.getY());
	}

    @Override
    public String toString() {
		return "Position{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
