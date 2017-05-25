package position.zigzagpivot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import color.FindColor;
import lejos.robotics.geometry.Point;

public class FollowLearnZigZag {

	private Robot ev3;
	private FindColor find;
	private int seenColor;
	private final int KP = 150;
	private final int KI = 10;
	private final int KD = 1000;
	private final double OFFSET;
	private final int TP = 300;

	private PrintWriter p;
	private String fileDonnee = "point.txt";
	private List<Point> listPoint;
	private List<Double> listHead;

	public FollowLearnZigZag() {
		this.ev3 = new Robot();
		this.ev3.setVitesse(TP, TP);
		this.find = new FindColor(this.ev3.NAMEFILE);
		OFFSET = ((double) (this.ev3.BACKGROUND - this.ev3.SUIVRE)) / 2 + this.ev3.SUIVRE;
		this.listPoint = new ArrayList<Point>();
		this.listHead = new ArrayList<Double>();
		initFile();
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

	private void writeCircuit() {
		for (Point p : this.listPoint) {
			this.p.println(p.getX() + " " + p.getY());
			this.p.flush();
		}
		for (Double d : this.listHead) {
			p.println(d);
			p.flush();
		}
	}

	public void pivot() {
		this.ev3.stop();
		int vitesseRight = this.ev3.getVitesseRight();
		int vitesseLeft = this.ev3.getVitesseLeft();
		int c = (vitesseLeft < vitesseRight) ? 0 : 1;
		long times2 = 250, times = System.currentTimeMillis();
		long timesTourne = 0;
		this.ev3.setVitesse(300, 300);
		while (seenColor != ev3.SUIVRE) {
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
		double angle = timesTourne * 1 / 10; // 360° = 3600ms a 300 vitesse
		angle /= 2;
		double x = this.ev3.getPoint().getX() + this.ev3.TRACK * Math.cos(Math.toRadians(angle));
		double y = this.ev3.getPoint().getY() + this.ev3.TRACK * Math.sin(Math.toRadians(angle));
		this.ev3.setPoint(new Point((float) x, (float) y));
		this.ev3.setHead(this.ev3.getHead() + angle);
		this.listPoint.add(this.ev3.getPoint());
		this.listHead.add(this.ev3.getHead());
		this.ev3.setVitesse(vitesseLeft, vitesseRight);
	}

	public void avance() {
		long onRTT = Long.MAX_VALUE;
		int calculRTT = 0, lastSeen = -1;
		double lastError = 0, integral = 0;
		long nbBack = System.currentTimeMillis(), calculRttREf = System.currentTimeMillis();
		this.seenColor = this.find.whatColor(ev3.lireColor());
		while (this.seenColor != this.ev3.DEPART) {
			if (seenColor == lastSeen && seenColor == this.ev3.SUIVRE) {
				nbBack = System.currentTimeMillis();
				calculatePointCurrent();
			}
			if (calculRTT < 2 && seenColor != lastSeen) {
				calculRTT++;
				if (calculRTT == 2) {
					onRTT = System.currentTimeMillis() - calculRttREf;
					onRTT += onRTT / 2;
				}
			}
			if (System.currentTimeMillis() - nbBack > onRTT) {
				pivot();
				nbBack = System.currentTimeMillis();
				integral = 0;
				lastError = 0;
			} else {
				double error = this.seenColor - OFFSET;
				integral += error;
				double derivative = error - lastError;
				double turn = KP * error + KI * integral + KD * derivative;
				this.ev3.avance((int) (TP + turn), (int) (TP - turn));
				lastError = error;
			}
			lastSeen = seenColor;
			this.seenColor = this.find.whatColor(ev3.lireColor());
		}
	}

	public void inLineBlack() {
		this.seenColor = this.find.whatColor(ev3.lireColor());
		while (this.seenColor == this.ev3.DEPART) {
			this.ev3.avance(300, 300);
			this.seenColor = this.find.whatColor(ev3.lireColor());
		}
		this.listPoint.add(this.ev3.getPoint());
	}

	protected void calculatePointCurrent() {
		double displacement = (this.ev3.vitesseMoyenneLeft() + this.ev3.vitesseMoyenneRight()) * this.ev3.ENCODER / 2;
		double rotation = (this.ev3.vitesseMoyenneRight() - this.ev3.vitesseMoyenneLeft()) * this.ev3.ENCODER
				/ this.ev3.TRACK;
		double angle = this.ev3.getHead() + rotation / 2;
		double x = this.ev3.getPoint().getX() + displacement * Math.cos(Math.toRadians(angle));
		double y = this.ev3.getPoint().getY() + displacement * Math.sin(Math.toRadians(angle));
		this.ev3.setPoint(new Point((float) x, (float) y));
		this.ev3.setHead(this.ev3.getHead() + rotation);
		this.ev3.clearListVitesse();
		this.listPoint.add(this.ev3.getPoint());
		this.listHead.add(this.ev3.getHead());
	}

	public static void main(String[] args) {
		FollowLearnZigZag f = new FollowLearnZigZag();
		System.out.println("tour1");
		f.avance();
		System.out.println("tour2");
		f.inLineBlack();
		f.ev3.stop();
		f.writeCircuit();
	}
}
