package project;

import lejos.hardware.lcd.LCD;

public class Follow {

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

	public Follow() {
		ev3 = new Robot();
		find = new FindColor(ev3.NAMEFILE);
	}

	protected void ligneDroite() {
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

	protected void pivot() {
		while (seenColor != ev3.SUIVRE && c < 10) {
			if (c % 2 == 0) {
				ev3.avance("droite");
			} else {
				ev3.avance("gauche");
			}
			if (first) {
				c = (lastC % 2 == 0) ? 0 : 1;
				switch (c % 2) {
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

	protected void tourne() {
		int direction = ev3.getDirection();
		while (seenColor != ev3.SUIVRE) {
			ev3.tourne(direction);
			ev3.setCourbe(ev3.getCourbe() + 1);
			seenColor = find.whatColor(ev3.lireColor());
			if (ev3.getCourbe() % 10 == 0) {
				georges = 0;
				ev3.setDirection(1);
			}
		}
	}

	protected void start() {
		c = 0;
		lastC = 0;
		georges = 0;
		lastDirection = 1;
		long tempsGeorges = System.currentTimeMillis();
		long tempsGeorgesMax = 1500;
		do {
			seenColor = find.whatColor(ev3.lireColor());
			ev3.setLigne(1);
			ligneDroite();
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
				ev3.vitesseMoyenne();
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

	public static void main(String[] args) {
		Follow f = new Follow();
		f.start();
	}

}
