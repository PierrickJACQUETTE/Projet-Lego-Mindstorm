package project;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robot {

	RegulatedMotor right, left;

	int roueDiam = 5;
	int roboDiam = 15;

	int direction = 1;
	int ligne = 1;
	int courbe = 1;
	final short SUIVRE = 1;
	final short VITESSEMOYENNE;

	public Robot() {
		right = new EV3LargeRegulatedMotor(MotorPort.B);
		left = new EV3LargeRegulatedMotor(MotorPort.D);
		this.VITESSEMOYENNE = (short) ((right.getSpeed() < left.getSpeed()) ? right.getSpeed() : left.getSpeed());
	}

	public void begSync() {
		left.synchronizeWith(new RegulatedMotor[] { right });
		right.startSynchronization();
	}

	public void avance() {
		right.forward();
		left.forward();
	}

	public void endSync() {
		right.endSynchronization();
		Delay.msDelay(10);
	}

	// ATTENTION LES MOTEURS SONT EN 3:1
	// On calcule le rapport distance avec le diametre de la roue. Puis en
	// convertit en degree.
	public void avance(int distance) {
		int deg = (int) Math.round((distance / roueDiam) * 360.0) / 3;
		right.rotate(deg, true);
		left.rotate(deg);
	}

	/*
	 * Tourne a droite roboDiam * PI represente la distance que parcours le
	 * robot en faisant un tour complet sur lui meme. Ici on veux juste 90% donc
	 * on divise par 4 [360/4]. On fait ensuite la conversion en degree.
	 */
	public void pivotD(int ang) {
		int deg = (int) Math.round((((roboDiam * Math.PI) / (360 / ang)) / roueDiam) * 360.0) / 3;
		right.rotate(-deg, true);
		left.rotate(deg);
	}

	public void pivotG(int ang) {
		int deg = (int) Math.round((((roboDiam * Math.PI) / (360 / ang)) / roueDiam) * 360.0) / 3;
		left.rotate(-deg, true);
		right.rotate(deg);
	}

	public void tourneD() {
		int tmp = 2;
		right.setSpeed(right.getSpeed() / tmp);
		this.begSync();
		this.avance();
		this.endSync();
		right.setSpeed(right.getSpeed() * tmp);
	}

	public void tourneG() {
		int tmp = 2;
		left.setSpeed(left.getSpeed() / tmp);
		this.begSync();
		this.avance();
		this.endSync();
		left.setSpeed(left.getSpeed() * tmp);
	}

	public void mytourneD() {
		if (courbe == 3) {
			int tmp = right.getSpeed() / courbe;
			tmp = (tmp < 0) ? 0 : tmp; // 0 ou vitesse actuelle a voir....
			right.setSpeed(tmp);
		}
		// agir sur roue gauche ?
		this.begSync();
		this.avance();
		this.endSync();
	}

	public void mytourneG() {
		if (courbe == 3) {
			int tmp = left.getSpeed() / courbe;
			tmp = (tmp < 0) ? 0 : tmp; // 0 ou vitesse actuelle a voir....
			left.setSpeed(tmp);
		}
		// agir sur roue droite ?
		this.begSync();
			this.avance();
		this.endSync();
	}

	public void mypivotD(int tentative) {
		right.setSpeed(VITESSEMOYENNE/tentative);
		left.setSpeed(VITESSEMOYENNE);
	}

	public void mypivotG(int tentative) {
		
		left.setSpeed(VITESSEMOYENNE/tentative);
		right.setSpeed(VITESSEMOYENNE);
	}

	public void accelerer() {
		right.setSpeed(30 + right.getSpeed());
		left.setSpeed(30 + left.getSpeed());
		courbe = 1;
	}

	public void ralentir() {
		right.setSpeed(VITESSEMOYENNE);
		left.setSpeed(VITESSEMOYENNE);
		ligne = 1;
	}
}
