package position.droitpivot;

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

	private Point point;
	private int direction;
	private int lastDirection;

	private double angle;
	private long timeAvance;
	private float vitesse;

	public Robot() {
		this.right = new EV3LargeRegulatedMotor(MotorPort.B);
		this.left = new EV3LargeRegulatedMotor(MotorPort.D);
		this.right.setSpeed(400);
		this.left.setSpeed(400);
		this.point = new Point(0, 0);
		this.ecs = new HiTechnicColorSensor(LocalEV3.get().getPort("S1"));
		this.colorRGBSensor = ecs.getRGBMode();
		this.direction = 1;
		this.angle = 0;
		this.timeAvance = 0;
		this.vitesse = this.right.getSpeed();
	}

	public void robotFin() {
		this.ecs.close();
		this.right.stop();
		this.left.stop();
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

	protected void arc() {
		this.begSync();
		this.right.forward();
		this.left.forward();
		this.endSync();
	}

	public void begSync() {
		this.left.startSynchronization();
		this.right.startSynchronization();
	}

	public void endSync() {
		this.right.endSynchronization();
		this.left.endSynchronization();
	}

	protected void setVitesse(int vitesseMotorRight, int vitesseMotorLeft) {
		this.right.setSpeed(vitesseMotorRight);
		this.left.setSpeed(vitesseMotorLeft);
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

	/**
	 * Calcul le nouveau x et y pour dessiner
	 * 
	 * @param pivotDone
	 *            est ce que le pivot a eu lieu
	 */
	protected void setPoint(boolean pivotDone) {
		float distance = 0;
		if (pivotDone) {
			distance = 4;
		} else {
			distance = vitesse * this.timeAvance / 1000;
			distance = distance * 13 / 400;
		}
		float x = (float) (this.point.getX() + distance * Math.cos(Math.toRadians(this.angle)));
		float y = (float) (this.point.getY() + distance * Math.sin(Math.toRadians(this.angle)));
		this.point = new Point(x, y);
		this.timeAvance = 0;
	}

	protected void setAngle(double angle) {
		this.angle += angle;
	}

	/**
	 * @return the point
	 */
	protected Point getPoint() {
		return point;
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

	public void setTimeAvance(long timeAvance) {
		this.timeAvance = timeAvance;
	}

	public double getAngle() {
		return angle;
	}

	protected void stop() {
		this.begSync();
		this.right.stop();
		this.left.stop();
		this.endSync();
	}

}