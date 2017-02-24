package project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class CalibrationCapteur {

	private PrintWriter p;
	private Couleur couleur;
	private short name;
	Robot robot;

	public CalibrationCapteur() {
		this.robot = new Robot();
		this.p = initFile();
		this.name = 0;
	}

	/*
	 * LCD.drawString("Appuyer sur ESCAPE", 0, 1); LCD.drawString(
	 * "pour stopper", 1, 2); LCD.drawString("Appuyer sur HAUT", 0, 3);
	 * LCD.drawString("pour ajout", 1, 4); LCD.drawString( "Appuyer sur DROITE",
	 * 0, 5); LCD.drawString("pour new color", 1, 6);
	 */
	protected void start() throws InterruptedException {
		boolean ok = false;
		boolean loop = true;
		while (loop) {
			switch (Button.waitForAnyEvent()) {
			case Button.ID_UP:
				LCD.drawString("Start pour " + name, 0, 3);
				for (int i = 0; i < 10; i++) {
					this.write(i);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				ok = true;
				LCD.drawString("Finie pour " + name, 0, 3);
				this.delayClearLCD();
				break;
			case Button.ID_RIGHT:
				if (ok) {
					this.name++;
				} else {
					LCD.drawString("Aucun releve ", 0, 3);
					LCD.drawString("pour couleur " + name, 1, 4);
					this.delayClearLCD();
				}
				break;
			case Button.ID_ESCAPE:
				loop = false;
				break;
			default:
				break;
			}
		}
		this.p.flush();
		this.p.close();
	}

	private void write(int releve) throws InterruptedException {
		this.couleur = this.robot.lireColor();
		this.couleur.setName(this.name);
		this.p.println(this.couleur);
	}

	private PrintWriter initFile() {
		PrintWriter p = null;
		try {
			File file = new File(this.robot.NAMEFILE);
			if (file.exists()) {
				file.delete();
			}
			p = new PrintWriter(new FileWriter(this.robot.NAMEFILE));
		} catch (NullPointerException a) {
			a.getStackTrace();
			System.out.println("Error : pointeur null");
		} catch (IOException a) {
			a.getStackTrace();
			System.out.println("Probleme d'IO");
		}
		return p;
	}

	private void delayClearLCD() {
		Delay.msDelay(500);
		LCD.clearDisplay();
	}

	public static void main(String[] args) throws InterruptedException {
		CalibrationCapteur test = new CalibrationCapteur();
		test.start();

	}
}