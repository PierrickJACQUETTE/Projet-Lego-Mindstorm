package position.droitpivot;

import lejos.robotics.geometry.Point;

public class Element {

	private Point point1;
	private Point point2;
	private double angle;

	public Element(Point point1, Point point2, double angle) {
		this.point1 = point1;
		this.point2 = point2;
		this.angle = angle;
	}

	/**
	 * @return the point1
	 */
	protected Point getPoint1() {
		return point1;
	}

	/**
	 * @param point2
	 *            the point2 to set
	 */
	protected void setPoint2(Point point2) {
		this.point2 = point2;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	protected void setAngle(double angle) {
		this.angle += angle;
	}

	@Override
	public String toString() {
		return point1.getX() + " " + point1.getY() + "\n" + point2.getX() + " " + point2.getY() + " " + angle;
	}

}
