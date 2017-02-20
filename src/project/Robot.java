package project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.I2CException;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Robot {

	protected final String NAMEFILE = "Couleur.txt";
	protected final short SUIVRE = 1;
	protected final short VITESSEMOYENNE;

	private RegulatedMotor right, left;
	private HiTechnicColorSensor ecs;
	private SampleProvider colorRGBSensor;
	private int direction;
	private int ligne;
	private int courbe;

	private boolean inversion;

	public Robot() {
		this.right = new EV3LargeRegulatedMotor(MotorPort.B);
		this.left = new EV3LargeRegulatedMotor(MotorPort.D);
		this.VITESSEMOYENNE = (short) ((right.getSpeed() < left.getSpeed()) ? right.getSpeed() : left.getSpeed());
		this.ecs = new HiTechnicColorSensor(LocalEV3.get().getPort("S1"));
		this.colorRGBSensor = ecs.getRGBMode();
		this.direction = 1;
		this.ligne = 1;
		this.courbe = 1;
	}

	public void robotFin() {
		this.ecs.close();
		this.right.stop();
		this.left.stop();
	}

	protected void avance() {
		this.avance("devant");
	}

	protected void avance(String direction) {
		this.begSync();
		switch (direction) {
		case "devant":
			this.right.forward();
			this.left.forward();
			break;
		case "gauche":
			this.left.backward();
			this.right.forward();
			break;
		case "droite":
			this.right.backward();
			this.left.forward();
			break;
		default:
			break;
		}
		this.endSync();
	}

	protected Couleur lireColor() {
		int sampleSize = this.colorRGBSensor.sampleSize();
		float[] sample = new float[sampleSize];
		boolean erreur = true;
		while (erreur == true) {
			try {
				this.colorRGBSensor.fetchSample(sample, 0);
				erreur = false;
			} catch (I2CException e) {
			    LCD.drawString("Une erreur est apparue lors du fetch", 0, 1);
				erreur = true;
			}
		}
		short tmp = 0;
		return new Couleur(this.convertToRGB(sample[0]), this.convertToRGB(sample[1]), this.convertToRGB(sample[2]),
				tmp);
	}

	private float convertToRGB(float f) {
		return f * 255f;
	}

	public void begSync() {
		this.left.startSynchronization();
		this.right.startSynchronization();
	}

	public void endSync() {
		this.right.endSynchronization();
		this.left.endSynchronization();
		Delay.msDelay(10);
	}
	
	public void suiteTourne(int dir) {
		if (this.courbe % 10 == 3) {
			if (dir == 2) {
				this.right.setSpeed(VITESSEMOYENNE);
				this.left.setSpeed(this.left.getSpeed()+100);
			} else {
				this.left.setSpeed(VITESSEMOYENNE);
				this.right.setSpeed(this.right.getSpeed()+100);
			}
		}
		inversion = true;
		this.avance();
	}

	public void tourne(int dir) {
		if (this.courbe % 10 == 3) {
			int tmp = (dir == 2) ? this.left.getSpeed() : this.right.getSpeed();
			tmp /= this.courbe;
			tmp = (tmp < 0) ? 0 : tmp;
			if (dir == 2) {
				this.right.setSpeed(VITESSEMOYENNE);
				this.left.setSpeed(tmp);
			} else {
				this.left.setSpeed(VITESSEMOYENNE);
				this.right.setSpeed(tmp);
			}
		}
		inversion = true;
		this.avance();
	}

	public void accelerer() {
		changeVitesse(left.getSpeed() + 30, right.getSpeed() + 30);
	}

	public void decelerer() {
		changeVitesse(left.getSpeed() - 30, right.getSpeed() - 30);
	}

	public void vitesseMoyenne() {
		changeVitesse(VITESSEMOYENNE, VITESSEMOYENNE);
	}

	protected void changeVitesse(int vitesseGauche, int vitesseDroit) {
		this.right.setSpeed(vitesseDroit);
		this.left.setSpeed(vitesseGauche);
	}

	public int getDirection() {
		return this.direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getLigne() {
		return this.ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	protected int getCourbe() {
		return this.courbe;
	}

	protected void setCourbe(int courbe) {
		this.courbe = courbe;
	}
}
