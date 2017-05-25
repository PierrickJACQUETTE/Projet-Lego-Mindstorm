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

	/**
	 * Fonction permettant de regrouper les elements du circuit
	 */
	private void refreshCircuit() {
		if (listPoint.size() > 2) {
			int sizeCircuit = circuit.size();
			int size = listPoint.size();
			// regrouper sous virage
			if (sizeCircuit != 0 && this.ev3.getDirection() == this.ev3.getLastDirection()) {
				circuit.get(sizeCircuit - 1).setPoint2(listPoint.get(size - 1));
				circuit.get(sizeCircuit - 1).setAngle(this.ev3.getAngle());
			} else { // new virage
				circuit.add(new Element(listPoint.get(size - 2), listPoint.get(size - 1), this.ev3.getAngle()));
			}
		}
	}

	private void pivot() {
		int c = this.ev3.getDirection();
		long times2 = 250, timesTourne = 0;
		long times = System.currentTimeMillis();
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
		this.ev3.setDirection(c % 2);
		this.ev3.setAngle(angle);
		this.ev3.setVitesse(400, 400);
	}

	// lancer pivot avec les bons arguments
	private void lancerpivot() {
		this.ev3.setLastDirection(this.ev3.getDirection());
		this.ev3.setPoint(false);
		this.write(this.ev3.getPoint());
		this.listPoint.add(this.ev3.getPoint());
		this.pivot();
		this.ev3.setPoint(true);
		this.write(this.ev3.getPoint());
		this.ev3.setAngle(0);
		this.listPoint.add(this.ev3.getPoint());
		this.refreshCircuit();
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
			} else {
				this.ev3.arc();
			}
			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.ev3.setTimeAvance(timePostSortie - time);
				first = true;
				lancerpivot();
			}
		} while (this.seenColor == this.ev3.SUIVRE && this.seenColor != this.ev3.DEPART);
	}

	private void inLineBlack() {
		do {
			this.ev3.arc();
			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.pivot();
			}
		} while (this.seenColor == this.ev3.DEPART);
	}

	private void write(Point point) {
		this.p.println(point.getX() + " " + point.getY());
		this.p.flush();
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
			System.err.println("Error : pointeur null");
		} catch (IOException a) {
			a.getStackTrace();
			System.err.println("Probleme d'IO");
		}
	}

	public static void main(String[] args) {
		Follow f = new Follow();
		f.tour1();
		f.inLineBlack();
		f.tour1();
	}

}