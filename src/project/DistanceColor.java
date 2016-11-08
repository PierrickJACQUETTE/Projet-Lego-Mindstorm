package project;

public class DistanceColor {

	private Color color;
	private float distance;

	public DistanceColor(Color color, float distance) {
		this.color = color;
		this.distance = distance;
	}

	protected Color getColor() {
		return color;
	}

	protected void setColor(Color color) {
		this.color = color;
	}

	protected float getDistance() {
		return distance;
	}

	protected void setDistance(float distance) {
		this.distance = distance;
	}

}
