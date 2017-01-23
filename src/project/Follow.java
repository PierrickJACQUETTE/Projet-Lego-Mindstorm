package project;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Follow {
	
	protected static void start(){

		Robot ev3 = new Robot();
		
		FindColor find = new FindColor();
		//int followedColor = find.whatColor(Util.lireColor(), false);
		int seenColor;

		int georges = 0;
		
		do {
			ev3.begSync();
			ev3.avance();
			ev3.ligne++;
			//ev3.accelerer();
			ev3.endSync();
			
			seenColor = find.whatColor(Util.lireColor(), false);
			int c = 0;
			if(ev3.direction == 1){
				LCD.clearDisplay();
				LCD.drawString("Methode 1", 3, 3);
				georges++;
				while (seenColor != ev3.suivre && c < 10) {		
					//ev3.ralentir();
					switch (c % 2) {
					case 0:
						ev3.pivotD(7 * (++c));
						ev3.direction = 0;
						break;
					case 1:
						ev3.pivotG(7 * (++c));	
						ev3.direction = 2;
						break;
					}
					seenColor = find.whatColor(Util.lireColor(), false);
				}
			} else {
				while(seenColor != ev3.suivre){
					//ev3.ralentir();
					LCD.clearDisplay();
					LCD.drawString("Methode a la con", 3, 3);
					switch(ev3.direction){
					case 0:
						ev3.tourneD();
						break;
					case 2:
						ev3.tourneG();
						break;
					}
					ev3.courbe++;
					seenColor = find.whatColor(Util.lireColor(), false);
					if(ev3.courbe == 10){ georges = 0; ev3.direction = 1;}
				}
			}	
			if(georges == 1) ev3.direction = 1;
		} while (seenColor == ev3.suivre);
	}
	
	public static void main(String[] args) {
		start();
	}
}
