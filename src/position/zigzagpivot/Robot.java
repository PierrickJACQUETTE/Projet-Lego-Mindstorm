package position.zigzagpivot;

import java.util.ArrayList;
import java.util.List;

import color.Couleur;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.geometry.Point;

public class Robot {

	public final String NAMEFILE = "Couleur.txt";
	public final short SUIVRE = 1;
	public final short DEPART = 2;
	public final short BACKGROUND = 0;
	private RegulatedMotor right, left;
	private HiTechnicColorSensor ecs;
	private SampleProvider colorRGBSensor;

	private double head;
	public final double TRACK = 7.5;
	public final double ENCODER = Math.PI * 4.2 / 360;
	private List<Integer> vitesseRight;
	private List<Integer> vitesseLeft;
	private Point positionCurrent;

	public Robot() {
		this.right = new EV3LargeRegulatedMotor(MotorPort.B);
		this.left = new EV3LargeRegulatedMotor(MotorPort.D);
		this.right.setSpeed(400);
		this.left.setSpeed(400);
		this.ecs = new HiTechnicColorSensor(LocalEV3.get().getPort("S1"));
		this.colorRGBSensor = ecs.getRGBMode();

		this.head = 0;
		this.vitesseLeft = new ArrayList<Integer>();
		this.vitesseRight = new ArrayList<Integer>();
		this.positionCurrent = new Point(0, 0);
	}

	/*
	 * lit la couleur tant qu'il y a pas d'erreur et la renvoie
	 */
	protected Couleur lireColor() {
		float[] sample = new float[this.colorRGBSensor.sampleSize()];
		while (true) {
			try {
				this.colorRGBSensor.fetchSample(sample, 0);
				Thread.sleep(10);
				break;
			} catch (Exception e) {
				LCD.drawString("Une erreur est apparue lors du fetch", 0, 1);
			}
		}
		return new Couleur(this.convertToRGB(sample[0]), this.convertToRGB(sample[1]), this.convertToRGB(sample[2]),
				(short) 0);
	}

	private float convertToRGB(float f) {
		return f * 255f;
	}

	private void begSync() {
		this.left.startSynchronization();
		this.right.startSynchronization();
	}

	private void endSync() {
		this.right.endSynchronization();
		this.left.endSynchronization();
	}

	protected void avance(int vitesseMotorRight, int vitesseMotorLeft) {
		this.right.setSpeed(Math.abs(vitesseMotorRight));
		this.left.setSpeed(Math.abs(vitesseMotorLeft));
		this.vitesseRight.add(vitesseMotorRight);
		this.vitesseLeft.add(vitesseMotorLeft);
		if (vitesseMotorLeft >= 0 && vitesseMotorRight >= 0) {
			this.begSync();
			this.right.forward();
			this.left.forward();
			this.endSync();
		} else if (vitesseMotorLeft < 0 && vitesseMotorRight >= 0) {
			rotateG();
		} else if (vitesseMotorLeft >= 0 && vitesseMotorRight < 0) {
			rotateD();
		} else {
			this.begSync();
			this.right.backward();
			this.left.backward();
			this.endSync();
		}
	}

	protected void rotateD() {
		this.begSync();
		right.backward();
		left.forward();
		this.endSync();
	}

	protected void rotateG() {
		this.begSync();
		left.backward();
		right.forward();
		this.endSync();
	}

	protected int getVitesseRight() {
		return this.right.getSpeed();
	}

	protected int getVitesseLeft() {
		return this.left.getSpeed();
	}

	protected void setVitesse(int vitesseMotorRight, int vitesseMotorLeft) {
		this.right.setSpeed(vitesseMotorRight);
		this.left.setSpeed(vitesseMotorLeft);
	}

	protected void stop() {
		this.begSync();
		this.right.stop();
		this.left.stop();
		this.endSync();
	}

	protected int vitesseMoyenneRight() {
		int res = 0;
		for (Integer i : this.vitesseRight) {
			res += i;
		}
		return (this.vitesseRight.size() != 0) ? res / this.vitesseRight.size() : res;
	}

	protected int vitesseMoyenneLeft() {
		int res = 0;
		for (Integer i : this.vitesseLeft) {
			res += i;
		}
		return (this.vitesseLeft.size() != 0) ? res / this.vitesseLeft.size() : res;
	}

	protected void clearListVitesse() {
		this.vitesseLeft.clear();
		this.vitesseRight.clear();
	}

	protected double getHead() {
		return head;
	}

	protected void setHead(double newHead) {
		this.head = newHead;
	}

	protected Point getPoint() {
		return positionCurrent;
	}

	protected void setPoint(Point newPosition) {
		this.positionCurrent = newPosition;
	}
}