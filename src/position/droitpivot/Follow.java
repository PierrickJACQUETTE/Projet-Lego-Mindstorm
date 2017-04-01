package position.droitpivot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import color.FindColor;
import lejos.robotics.geometry.Point;

public class Follow {

	private Robot ev3;
	private FindColor find;
	private int seenColor;
	private List<Element> circuit;
	private List<Point> listPoint;
	private String fileDonnee = "point.txt";
	private PrintWriter p;

	public Follow() {
		this.ev3 = new Robot();
		this.find = new FindColor(this.ev3.NAMEFILE);
		this.circuit = new ArrayList<Element>();
		this.listPoint = new ArrayList<Point>();
		initFile();
	}

	private double rayon(Point point1, Point point2, Point point3) {

		double point1Point2 = Math
				.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
		double point2Point3 = Math
				.sqrt(Math.pow(point3.getX() - point2.getX(), 2) + Math.pow(point3.getY() - point2.getY(), 2));
		double point3Point1 = Math
				.sqrt(Math.pow(point1.getX() - point3.getX(), 2) + Math.pow(point1.getY() - point3.getY(), 2));
		double div = (Math.pow(point3Point1, 2) + Math.pow(point1Point2, 2) - Math.pow(point2Point3, 2))
				/ (2 * point3Point1 * point1Point2);
		div = (div > 1) ? 1 : div;
		double res = point2Point3 / (2 * Math.sin(Math.acos(div)));
		return (Double.isNaN(res)) ? 0 : res;
	}

	private double euclide(Point point1, Point point2) {
		double dX = point1.getX() - point2.getX();
		double dY = point1.getY() - point2.getY();
		return Math.sqrt((dX * dX) + (dY * dY));
	}

	private void refreshCircuit() {
		if (listPoint.size() > 2) {
			int sizeCircuit = circuit.size();
			int size = listPoint.size();
			// regrouper sous virage
			if (sizeCircuit != 0 && this.ev3.getDirection() == this.ev3.getLastDirection()) {

				System.out.println("regrouper");

				circuit.get(sizeCircuit - 1).setPoint2(listPoint.get(size - 1));
				circuit.get(sizeCircuit - 1).setAngle(this.ev3.angle);
				;
			} else { // new virage

				System.out.println("new");
				circuit.add(new Element(listPoint.get(size - 2), listPoint.get(size - 1), this.ev3.angle));
				// listPoint.remove(listPoint.get(size - 4));
				// listPoint.remove(listPoint.get(size - 3));
			}
		}
	}

	private int calculateMinDistance() {
		System.out.println("l157 " + this.ev3.getPoint().getX() + " " + this.ev3.getPoint().getY());
		List<DistanceObjectPoint> distancePoint = new ArrayList<DistanceObjectPoint>();
		for (int i = 0; i < circuit.size(); i++) {
			float distance = (float) euclide(this.ev3.getPoint(), circuit.get(i).getPoint1());
			distancePoint.add(new DistanceObjectPoint(distance, circuit.get(i).getPoint1(), i));
			System.out.println(distance + " " + i);
		}
		int pos = -1;
		if (distancePoint.size() > 0) {
			float min = distancePoint.get(0).getDistance();
			pos = distancePoint.get(0).getIndiceCircuit();
			for (DistanceObjectPoint distanceObjectPoint : distancePoint) {
				if (distanceObjectPoint.getDistance() < min) {
					min = distanceObjectPoint.getDistance();
					pos = distanceObjectPoint.getIndiceCircuit();
				}
			}
		}
		return pos;
	}

	private void pivot() {
		int c = this.ev3.getDirection();
		long times2 = 250;
		long times = System.currentTimeMillis();
		long timesTourne = 0;
		this.ev3.setVitesse(150, 150);
		while (seenColor != ev3.SUIVRE && c < 10) {
			if (System.currentTimeMillis() - times > times2) {
				timesTourne += (c % 2 == 0) ? -times2 : times2;
				c++;
				times2 += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				times = System.currentTimeMillis();
			} else {
				if (c % 2 == 0) {
					ev3.rotateD();
				} else {
					ev3.rotateG();
				}
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
		this.ev3.stop();
		long addTimesTourne = System.currentTimeMillis() - times;
		timesTourne += (c % 2 == 0) ? -addTimesTourne : addTimesTourne;
		double angle = timesTourne * 1 / 20; // 180° = 36000ms a 150 vitesse
		System.out.println("===end : angle : " + angle);
		this.ev3.setDirection(c % 2);
		this.ev3.setAngle(angle);
		this.ev3.setVitesse(400, 400);
	}

	private void tour1() {
		boolean first = true;
		long time = 0;
		do {
			if (first) {
				first = false;
				time = System.currentTimeMillis();
			}
			long timePostSortie = System.currentTimeMillis();
			if (this.ev3.getLastDirection() == this.ev3.getDirection() && this.circuit.size() > 1) {
				this.ev3.arc();
				// de l'ancien
			} else {
				this.ev3.arc();
			}
			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.ev3.timeAvance = timePostSortie - time;
				this.ev3.setLastDirection(this.ev3.getDirection());
				first = true;
				this.ev3.setPoint(false);
				this.write(this.ev3.getPoint());
				System.out.println("point avant : " + ev3.getPoint().getX() + " " + ev3.getPoint().getY());
				this.listPoint.add(this.ev3.getPoint());
				this.pivot();
				this.ev3.setPoint(true);
				this.write(this.ev3.getPoint());
				this.ev3.setAngle(0);
				System.out.println("point apres : " + ev3.getPoint().getX() + " " + ev3.getPoint().getY());
				this.listPoint.add(this.ev3.getPoint());
				this.refreshCircuit();
			}
		} while (this.seenColor == this.ev3.SUIVRE && this.seenColor != this.ev3.DEPART);
	}

	private void inLineBlack() {
		System.out.println("l233 : " + this.ev3.getPoint().getX() + " " + this.ev3.getPoint().getY());
		this.ev3.newPose();
		System.out.println("l235 : " + this.ev3.getPoint().getX() + " " + this.ev3.getPoint().getY());
		do {
			// this.ev3.setPoint();
			this.ev3.arc();
			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.pivot();
				// this.ev3.setPoint();
			}
		} while (this.seenColor == this.ev3.DEPART);
	}

	private void tour2() {
		do {
			int positionProche = calculateMinDistance();
			System.out.println("l248 : pos : " + positionProche);
			double distanceEntree = 999;
			double distanceSortie = 999;
			if (positionProche != -1 && circuit.size() > positionProche) {
				distanceEntree = euclide(this.ev3.getPoint(), circuit.get(positionProche).getPoint1());
				// distanceSortie = euclide(this.ev3.getPoint(),
				// circuit.get(positionProche).getPoint3());
			}
			if (circuit.size() - 2 > positionProche && distanceSortie <= distanceEntree) {

				System.out.println("arc new");

				// double newRayon = rayon(this.ev3.getPoint(),
				// circuit.get(positionProche++).getPoint2(),
				// circuit.get(positionProche++).getPoint3());
				// this.ev3.getPilot().arc(newRayon, ANGLE, true);
				this.ev3.arc();
			} else if (circuit.size() - 1 > positionProche && distanceEntree < distanceSortie) {

				System.out.println("arc");

				// this.ev3.getPilot().arc(circuit.get(positionProche).getRayon(),
				// ANGLE, true);
				this.ev3.arc();
			} else {

				System.out.println("droit");

				// this.ev3.getPilot().arc(AVANCEDROIT, ANGLE, true);
				this.ev3.arc();
			}

			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.pivot();
			}
			// this.ev3.setPoint();
		} while (this.seenColor == this.ev3.SUIVRE && this.seenColor != this.ev3.DEPART);
	}

	private void write(Point point) {
		this.p.println(point.getX() + " " + point.getY());
		this.p.flush();
	}

	private void writeCircuit() {
		for (Element e : circuit) {
			// System.out.println(e);
			this.p.println(e);
			this.p.flush();
		}
	}

	private void initFile() {
		try {
			File file = new File(this.fileDonnee);
			if (file.exists()) {
				file.delete();
			}
			p = new PrintWriter(new FileWriter(this.fileDonnee));
		} catch (NullPointerException a) {
			a.getStackTrace();
			System.out.println("Error : pointeur null");
		} catch (IOException a) {
			a.getStackTrace();
			System.out.println("Probleme d'IO");
		}
	}

	public static void main(String[] args) {
		Follow f = new Follow();
		f.tour1();
		// f.writeCircuit();
		// boolean tour3 = true;
		// while (tour3) {
		// f.tour1();
		// f.inLineBlack();
		// f.tour2();
		// }
		// f.ev3.robotFin();
	}

}