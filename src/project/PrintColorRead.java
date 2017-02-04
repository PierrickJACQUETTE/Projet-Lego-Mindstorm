package project;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class PrintColorRead {

	public static void main(String[] a) {
		Robot robot = new Robot();
		boolean loop = true;
		while (loop) {
			LCD.clearDisplay();
			LCD.drawString("Appuyer sur ESCAPE", 0, 1);
			LCD.drawString("pour stopper", 1, 2);
			LCD.drawString("Appuyer sur HAUT", 0, 3);
			LCD.drawString("pour releve", 1, 4);
			switch (Button.waitForAnyEvent()) {
			case Button.ID_UP:
				Couleur c = robot.lireColor();
				LCD.clearDisplay();
				LCD.drawString("ROUGE : " + c.getRed(), 0, 1);
				LCD.drawString("VERT : " + c.getGreen(), 0, 2);
				LCD.drawString("BLUE : " + c.getBlue(), 0, 3);
				Delay.msDelay(8000);
				LCD.clear();
				break;
			case Button.ID_ESCAPE:
				loop = false;
				break;
			default:
				break;
			}

		}
	}
}
