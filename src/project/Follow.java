package project;

import lejos.hardware.lcd.LCD;

public class Follow {

	protected static void start() {
		Robot ev3 = new Robot();

		FindColor find = new FindColor();
		int seenColor;

		int georges = 0;

		do {
			seenColor = find.whatColor(Util.lireColor(), false);
			while(ev3.SUIVRE == seenColor){
			ev3.begSync();
			ev3.avance();
			ev3.ligne++;
			ev3.accelerer();
			ev3.endSync();
			seenColor = find.whatColor(Util.lireColor(), false);
			}
			georges = 0;
			seenColor = find.whatColor(Util.lireColor(), false);
			int c = 0;
			if (ev3.direction == 1) {
				LCD.clearDisplay();
				LCD.drawString("Methode 1", 3, 3);
				ev3.ralentir();
				long times = System.currentTimeMillis();
				long times2 = 50;
				boolean first = true;
				seenColor = find.whatColor(Util.lireColor(), false);
				ev3.right.setSpeed(150);
				ev3.left.setSpeed(150);
				while (seenColor != ev3.SUIVRE && c < 10) {
					ev3.begSync();
					if (c % 2 == 0) {
						ev3.right.backward();
						ev3.left.forward();
					} else {
						ev3.left.backward();
						ev3.right.forward();
					}
					ev3.endSync();
					if (first) {
						switch (c % 2) {
						case 0:
							ev3.direction = 0;
							georges++;
							break;
						case 1:
							ev3.direction = 2;
							break;
						}
						first = false;
					}
					if (System.currentTimeMillis() - times > times2) {
						c++;
						first = true;
						times2 += (c<3)? (c%2==0)? 250 : 260 : (c%2==0)? 500 : 520;
						times = System.currentTimeMillis();
					}
					seenColor = find.whatColor(Util.lireColor(), false);
				}
			} else {
				ev3.ralentir();
				int directionLast = ev3.direction;
				
				while (seenColor != ev3.SUIVRE) {
					
					LCD.clearDisplay();
					LCD.drawString("ici " + ev3.courbe, 3, 3);
					switch (directionLast) {
					case 0:
						ev3.mytourneD();
						break;
					case 2:
						ev3.mytourneG();
						break;
					}
					ev3.courbe++;
					seenColor = find.whatColor(Util.lireColor(), false);
					if (ev3.courbe == 10) {
						georges = 0;
						ev3.direction = 1;
						ev3.courbe = 0;
					}
				}
				ev3.ralentir();
			}
			if (georges == 2 || georges==1){
				ev3.direction = 1;
			}
		} while (seenColor == ev3.SUIVRE);
	}

	public static void main(String[] args) {
		start();
	}

}
