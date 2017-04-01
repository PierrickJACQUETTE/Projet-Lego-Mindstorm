package forwardSpeed;

import color.Couleur;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Robot {

	public final String NAMEFILE = "Couleur2.txt";
	public final short SUIVRE = 1;
	public final short DEPART = 2;
	public final short BACKGROUND = 0;
	private RegulatedMotor right, left;
	private HiTechnicColorSensor ecs;
	private SampleProvider colorRGBSensor;
	private int direction;
	private int lastDirection;

	public Robot() {
		this.right = new EV3LargeRegulatedMotor(MotorPort.B);
		this.left = new EV3LargeRegulatedMotor(MotorPort.D);
		this.right.setSpeed(400);
		this.left.setSpeed(400);
		this.ecs = new HiTechnicColorSensor(LocalEV3.get().getPort("S1"));
		this.colorRGBSensor = ecs.getRGBMode();
		this.direction = 1;
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
		if (vitesseMotorLeft >= 0 && vitesseMotorRight >= 0) {
			arc();
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

	protected void arc() {
		this.begSync();
		this.right.forward();
		this.left.forward();
		this.endSync();
	}

	protected void rotateD() {
		this.begSync();
		right.backward();
		left.forward();
		this.endSync();
	}

	public void rotateG() {
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

	/**
	 * @return the direction
	 */
	protected int getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	protected void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the lastDirection
	 */
	protected int getLastDirection() {
		return lastDirection;
	}

	/**
	 * @param lastDirection
	 *            the lastDirection to set
	 */
	protected void setLastDirection(int lastDirection) {
		this.lastDirection = lastDirection;
	}

	protected void stop() {
		this.begSync();
		this.right.stop();
		this.left.stop();
		this.endSync();
	}

}