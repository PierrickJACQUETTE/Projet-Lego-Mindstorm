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

	/**
	 * Constructeur pour apprendre les couleurs au robot Il ecrit les releves
	 * dans un fichier avec un entier pour chaque couleur
	 */
	public CalibrationCapteur() {
		this.robot = new Robot();
		this.p = initFile();
		this.name = 0;
	}

	/**
	 * Quand on appuit sur le boutton du haut du robot, lit fois une couleur
	 * puis l ecrit dans le fichier, la lecture a lieu 10 fois de suite Quand on
	 * appuit sur le boutton de droite du robot, il passe a la couleur suivante
	 * Quand on appuit sur le boutton escape alors on finit l apprentissage des
	 * couleurs
	 * 
	 * @throws InterruptedException
	 */
	protected void start() throws InterruptedException {
		boolean ok = false;
		boolean loop = true;
		while (loop) {
			switch (Button.waitForAnyEvent()) {
			case Button.ID_UP:
				LCD.drawString("Start pour " + name, 0, 3);
				for (int i = 0; i < 10; i++) {
					this.couleur = this.robot.lireColor();
					this.write();
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

	/**
	 * Methode qui ecrit une couleur dans le fichier
	 */
	private void write() throws InterruptedException {
		this.couleur.setName(this.name);
		this.p.println(this.couleur);
	}

	/**
	 * Fonction permettant de lire dans un fichier
	 */
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

	/**
	 * Fonction permet de supprimer le texte afficher sur l ecran du robot
	 */
	private void delayClearLCD() {
		Delay.msDelay(500);
		LCD.clearDisplay();
	}

	public static void main(String[] args) throws InterruptedException {
		CalibrationCapteur test = new CalibrationCapteur();
		test.start();

	}
}