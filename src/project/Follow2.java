package project;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class Follow2 {

	private Robot ev3;
	private FindColor find;
	private int seenColor;
	private int c;
	private int lastC;
	private long times;
	private long times2;
	private boolean first;
	private int georges;
	private int lastDirection;
	private boolean tourne;

	public Follow2() {
		ev3 = new Robot();
		find = new FindColor(ev3.NAMEFILE);
	}

	protected void ligneDroite() throws InterruptedException {
		boolean accelerer = true;
		while (ev3.SUIVRE == seenColor) {
			ev3.begSync();
			ev3.avance();
			ev3.endSync();
			if (ev3.getLigne() < 15 && accelerer) {
				ev3.accelerer();
				ev3.setLigne(ev3.getLigne() + 1);
			} else if (ev3.getLigne() == 30 && accelerer) {
				accelerer = false;
			} else if (ev3.getLigne() == 7) {
				accelerer = true;
			} else if (accelerer == false) {
				ev3.decelerer();
				ev3.setLigne(ev3.getLigne() - 1);
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
	}

	protected void pivot() throws InterruptedException {
		while (seenColor != ev3.SUIVRE && c < 10) {
			if (first) {
				c = lastC % 2;
				switch (c) {
				case 0:
					ev3.setDirection(0);
					georges++;
					break;
				case 1:
					ev3.setDirection(2);
					break;
				}
				first = false;
			}
			else{
				if (c % 2 == 0) {
					ev3.avance("droite");
				} else {
					ev3.avance("gauche");
				}
			}
			if (System.currentTimeMillis() - times > times2) {
				c++;
				lastC = c;
				first = true;
				times2 += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				times = System.currentTimeMillis();
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
	}
	
	protected void suiteTourne() throws InterruptedException {
		int direction = ev3.getDirection();
		while (seenColor == ev3.SUIVRE) {
			ev3.suiteTourne(direction);
			ev3.setCourbe(ev3.getCourbe() + 1);
			seenColor = find.whatColor(ev3.lireColor());
			if (ev3.getCourbe() % 10 == 0) {
				georges = 0;
				ev3.setDirection(1);
			}
		}
		ev3.setDirection(1);
	}

	protected void tourne() throws InterruptedException {
		int direction = ev3.getDirection();
		while (seenColor != ev3.SUIVRE) {
			ev3.tourne(direction);
			ev3.setCourbe(ev3.getCourbe() + 1);
			seenColor = find.whatColor(ev3.lireColor());
			if (ev3.getCourbe() % 10 == 0) {
				georges = 0;
			}
		}
	}

	protected void start() throws InterruptedException {
		c = 0;
		lastC=0;
		georges = 0;
		lastDirection = 1;
		tourne = false;
		long tempsGeorges = System.currentTimeMillis();
		long tempsGeorgesMax = 1500;
		do {
			seenColor = find.whatColor(ev3.lireColor());
			if (tourne == false) {
				ev3.setLigne(1);
				ligneDroite();
			} else {
				suiteTourne();
				tourne = false;
				ev3.vitesseMoyenne();
				Button.LEDPattern(0);
			}
			c = 0;
			if (ev3.getDirection() == 1) {
				times = System.currentTimeMillis();
				times2 = 250;
				first = true;
				seenColor = find.whatColor(ev3.lireColor());
				ev3.changeVitesse(150, 150);
				pivot();
			} else {
				ev3.vitesseMoyenne();
				tourne();
				tourne = true;
			}
			if (System.currentTimeMillis() - tempsGeorges > tempsGeorgesMax) {
				georges = 0;
				tempsGeorges = System.currentTimeMillis();
			}
			if (georges == 1 || georges == 2 || c % 2 != lastDirection % 2) {
				ev3.setDirection(1);
			}
			lastDirection = lastC;
		} while (seenColor == ev3.SUIVRE);
		ev3.robotFin();
	}

	public static void main(String[] args) throws InterruptedException {
		Follow2 f = new Follow2();
		f.start();
		f.ev3.robotFin();
	}

}
