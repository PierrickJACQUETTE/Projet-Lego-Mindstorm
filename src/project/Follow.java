package project;

import lejos.hardware.lcd.LCD;

public class Follow {

	private Robot ev3;
	private FindColor find;
	private int seenColor;
	private int c;
	private long times;
	private long times2;
	private boolean first;
	private int georges;

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
			} else if (ev3.getLigne() == 15) {
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
				first = true;
				times2 += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				times = System.currentTimeMillis();
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
	}

	protected void tourne() {
		int directionLast = ev3.getDirection();
		while (seenColor != ev3.SUIVRE) {
			ev3.tourne(directionLast);
			ev3.setCourbe(ev3.getCourbe() + 1);
			seenColor = find.whatColor(ev3.lireColor());
			if (ev3.getCourbe() == 10) {
				georges = 0;
				ev3.setDirection(1);
				ev3.setCourbe(0);
			}
		}
	}

	protected void start() {
		georges = 0;
		long tempsGeorges = System.currentTimeMillis();
		long tempsGeorgesMax = 1250;
		do {
			seenColor = find.whatColor(ev3.lireColor());
			ev3.setLigne(1);
			ligneDroite();
			LCD.clearDisplay();
			LCD.drawString(System.currentTimeMillis() - tempsGeorges + "", 0, 4);
			if (System.currentTimeMillis() - tempsGeorges > tempsGeorgesMax) {
				georges = 0;
				tempsGeorges = System.currentTimeMillis();
			}
			c = 0;
			if (ev3.getDirection() == 1) {
				times = System.currentTimeMillis();
				times2 = 50;
				first = true;
				seenColor = find.whatColor(ev3.lireColor());
				ev3.changeVitesse(150, 150);
				pivot();
			} else {
				ev3.vitesseMoyenne();
				tourne();
				ev3.vitesseMoyenne();
			}
			if (georges == 2 || georges == 1) {
				ev3.setDirection(1);
			}
		} while (seenColor == ev3.SUIVRE);
		ev3.robotFin();
	}

	public static void main(String[] args) {
		Follow f = new Follow();
		f.start();
	}

}
