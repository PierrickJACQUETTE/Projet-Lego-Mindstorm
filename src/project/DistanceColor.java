package project;

public class DistanceColor {

	private Couleur color;
	private float distance;

	public DistanceColor(Couleur color, float distance) {
		this.color = color;
		this.distance = distance;
	}

	protected Couleur getColor() {
		return color;
	}

	protected void setColor(Couleur color) {
		this.color = color;
	}

	protected float getDistance() {
		return distance;
	}

	protected void setDistance(float distance) {
		this.distance = distance;
	}

}
