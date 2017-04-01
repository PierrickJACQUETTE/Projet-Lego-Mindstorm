package position.droitpivot;

import lejos.robotics.geometry.Point;
import project.DistanceObject;

public class DistanceObjectPoint extends DistanceObject<Point> {

	Point point;
	int indiceCircuit;

	public DistanceObjectPoint(float distance, Point point, int indiceCircuit) {
		super(distance);
		this.point = point;
		this.indiceCircuit = indiceCircuit;
	}

	protected Point getPoint() {
		return point;
	}

	protected int getIndiceCircuit() {
		return indiceCircuit;
	}

}
