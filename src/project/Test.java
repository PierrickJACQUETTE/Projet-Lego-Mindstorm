package project;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Test {
	
	public static void main(String[] args) {
		Robot a = new Robot();
		a.begSync();
		a.avance();
		LCD.clearDisplay();
		LCD.drawInt(a.left.getSpeed(), 3,3);
		LCD.drawString(a.left.getMaxSpeed()+"", 4, 4);
		Delay.msDelay(3000);
		a.endSync();
	}

}
