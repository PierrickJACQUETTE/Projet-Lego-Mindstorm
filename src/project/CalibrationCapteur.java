package project;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CalibrationCapteur {

	/*
	 * Mettre le jar dans samples sur ev3 : 
	 * scp CalibrationCapteur.jar root@10.0.1.1:lejos/samples
	 * 
	 * Pour récupérer le fichier de sortie :
	 * scp root@10.0.1.1:lejos/samples/Couleur.txt ~
	 * 
	 * le mot de passe en vide (presser entree)
	 */

	private final short SAMPLE = 10;
	private PrintWriter p;
	private Color couleur;

	public CalibrationCapteur(String name) {
		this.p = initFile();
		this.couleur = new Color(0f, 0f, 0f, name);
	}

	public void start() {
		this.medium();
		this.p.println(this.couleur.toString());
		this.p.flush();
		this.p.close();
	}

	private void medium() {
		for (int i = SAMPLE; i > 0; i--) {
			Color c = Util.readColor();
			this.couleur.setRed(this.couleur.getRed() + c.getRed());
			this.couleur.setGreen(this.couleur.getGreen() + c.getGreen());
			this.couleur.setBlue(this.couleur.getBlue() + c.getBlue());
		}
		this.couleur.setRed(this.couleur.getRed() / SAMPLE);
		this.couleur.setGreen(this.couleur.getGreen() / SAMPLE);
		this.couleur.setBlue(this.couleur.getBlue() / SAMPLE);
	}

	private PrintWriter initFile() {
		PrintWriter p = null;
		try {
			p = new PrintWriter(new FileWriter(Util.NAMEFILE, true));
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
		CalibrationCapteur test = new CalibrationCapteur("bleu");
		test.start();

	}
}