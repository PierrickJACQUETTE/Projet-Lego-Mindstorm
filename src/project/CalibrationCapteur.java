package project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class CalibrationCapteur {

	private PrintWriter p;
	private Color couleur;
	private String dernier;

	public CalibrationCapteur() {
		this.p = initFile();
		this.couleur = new Color(0f, 0f, 0f, 0);
		this.dernier = "";
	}

	protected void start() {
		int name = 0;
		int releve = 0, lastReleve = 0;
		// vaut 1 si Button Up et vaut 2 si Button Right
		int lastAction = 0;
		boolean ok = false, lastOk = false;
		boolean loop = true;
		while (loop) {
			/*
			 * LCD.drawString("Appuyer sur ESCAPE", 0, 1); LCD.drawString(
			 * "pour stopper", 1, 2); LCD.drawString("Appuyer sur HAUT", 0, 3);
			 * LCD.drawString("pour ajout", 1, 4); LCD.drawString(
			 * "Appuyer sur DROITE", 0, 5); LCD.drawString("pour new color", 1,
			 * 6);
			 */
			switch (Button.waitForAnyEvent()) {
			case Button.ID_UP:
				for (int i = 0; i < 10; i++) {
					this.write(name, releve);
					lastReleve = releve;
					releve++;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lastOk = ok;
				ok = true;
				lastAction = 1;
				break;
			case Button.ID_RIGHT:
				if (ok) {
					name++;
					lastReleve = releve;
					releve = 0;
					for (int i = 0; i < 10; i++) {
						this.write(name, releve);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					releve++;

					lastAction = 2;
				} else {
					LCD.drawString("Aucun releve ", 0, 3);
					LCD.drawString("pour couleur " + name, 1, 4);
					Util.DelayClearLCD();
				}
				break;
			case Button.ID_LEFT:
				LCD.drawString("Annulation du ", 0, 3);
				LCD.drawString("Releve " + releve + " couleur " + name, 1, 4);
				this.dernier = "";
				switch (lastAction) {
				case 1:
					releve = lastReleve;
					ok = lastOk;
					lastAction = 0;
					break;
				case 2:
					name--;
					releve = lastReleve;
					lastAction = 0;
					break;
				default:
					break;
				}
				break;
			case Button.ID_ESCAPE:
				loop = false;
				break;
			default:
				break;
			}

		}
		this.p.println(this.dernier);
		this.p.flush();
		this.p.close();
	}

	private void write(int name, int releve) {
		this.couleur = Util.lireColor();
		this.couleur.setName(name);
		this.p.println(this.dernier);
		this.dernier = this.couleur.toString();

		LCD.drawString("Releve " + releve + " couleur " + name, 0, 3);
		Util.DelayClearLCD();
	}

	private PrintWriter initFile() {
		PrintWriter p = null;
		try {
			File file = new File(Util.NAMEFILE);
			if (file.exists()) {
				file.delete();
			}
			p = new PrintWriter(new FileWriter(Util.NAMEFILE));
		} catch (NullPointerException a) {
			a.getStackTrace();
			System.out.println("Error : pointeur null");
		} catch (IOException a) {
			a.getStackTrace();
			System.out.println("Probleme d'IO");
		}
		return p;
	}

	public static void main(String[] args) {
		CalibrationCapteur test = new CalibrationCapteur();
		test.start();

	}
}