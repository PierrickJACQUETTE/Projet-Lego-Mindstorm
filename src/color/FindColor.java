package color;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import project.Robot;

public class FindColor {

	private final short DELAY = 500;
	private List<DistanceObjectColor> distanceColor;
	private int ensemble;

	/**
	 * Constructeur qui permet de recuperer toutes les couleurs connues
	 * 
	 * @param name
	 *            nom du fichier ou son stocker les couleurs
	 */
	public FindColor(String name) {
		this.distanceColor = this.readFile(name);
		this.ensemble = this.distanceColor.size() / nbColour();
	}

	// permet de connaitre le nombre de couleur connue
	private int nbColour() {
		int cpt = 0;
		List<Short> idColor = new ArrayList<Short>();
		for (DistanceObjectColor d : this.distanceColor) {
			if (!idColor.contains(d.getCouleur().getName())) {
				cpt++;
				idColor.add(d.getCouleur().getName());
			}
		}
		return cpt;
	}

	// permet de lire de file avec les couleurs connues et les stockes dans une
	// list
	private List<DistanceObjectColor> readFile(String name) {
		List<DistanceObjectColor> list = new ArrayList<DistanceObjectColor>();
		try {
			BufferedReader fluxEntree = new BufferedReader(new FileReader(name));
			String ligne;
			while ((ligne = fluxEntree.readLine()) != null) {
				String[] read = ligne.split(" ");
				if (read.length == 4) {
					list.add(new DistanceObjectColor(new Couleur(Float.parseFloat(read[0]), Float.parseFloat(read[1]),
							Float.parseFloat(read[2]), Short.parseShort(read[3])), 9999f));
				}
			}
			fluxEntree.close();
		} catch (FileNotFoundException e) {
			System.out.println(" File not found");
			LCD.drawString(" File not found", 0, 4);
			Delay.msDelay(this.DELAY);
		} catch (IOException e) {
			LCD.drawString(" Error open", 0, 4);
			Delay.msDelay(this.DELAY);
		}
		return list;
	}

	//
	/**
	 * methode permettant de determiner quelle est le nom de la couleur de lon
	 * lit en fonction des couleurs que lon connait affichage false
	 * 
	 * @param c
	 *            couleur lue
	 * @return le nom de la couleur la plus proche
	 */
	public int whatColor(Couleur c) {
		return whatColor(c, false);
	}

	/**
	 * methode permettant de determiner quelle est le nom de la couleur de lon
	 * lit en fonction des couleurs que lon connait, affichage true quand find,
	 * false sinon quand on avance
	 * 
	 * @param c
	 *            couleur lue
	 * @param affichage
	 *            savoir si l on affiche sur l ecran le resultat
	 * @return le nom de la couleur la plus proche
	 */
	protected int whatColor(Couleur c, boolean affichage) {
		// on calcul pour chaque couleur connu la distance par rapport a la
		// couleur lue
		for (int i = 0; i < this.distanceColor.size(); i++) {
			this.distanceColor.get(i).setDistance(this.distanceColor.get(i).getCouleur().euclide(c));
		}
		triFusion(this.distanceColor);
		int index = this.maxOccurence(this.ensemble);
		if (index == this.distanceColor.size()) {
			if (affichage == true) {
				LCD.drawString(" Color not found", 0, 4);
				Delay.msDelay(DELAY);
			}
			return -1;
		} else {
			if (affichage == true) {
				LCD.drawString("color : " + index, 0, 4);
				Delay.msDelay(DELAY);
			}
			return index;
		}
	}

	private int maxOccurence(int ensemble) {
		int max1 = -1;
		int max2 = -1;
		int index1 = this.distanceColor.size();
		int index2 = this.distanceColor.size();
		int[] nbrOccurrence = new int[ensemble + 1];
		for (int i = 0; i < ensemble; i++) {
			// compte le nombre d occurence d une couleur
			nbrOccurrence[this.distanceColor.get(i).getCouleur().getName()]++;
		}
		// permet de connaitre la couleur la plus proche d apres son nombre d
		// occurrence
		for (int i = 0; i < nbrOccurrence.length; i++) {
			if (nbrOccurrence[i] >= max1) {
				max2 = max1;
				max1 = nbrOccurrence[i];
				index2 = i;
				index1 = i;
			}
		}
		// si un max alors return index
		// si deux max egaux alors
		// tant que ensemble peut s'agrandir recalculer
		// sinon au pif entre les deux => index2
		return (max1 > max2) ? index1 : (ensemble++ < this.distanceColor.size()) ? maxOccurence(ensemble++) : index2;
	}

	private static void triFusion(List<DistanceObjectColor> tableau) {
		int longueur = tableau.size();
		if (longueur > 0) {
			triFusion(tableau, 0, longueur - 1);
		}
	}

	private static void triFusion(List<DistanceObjectColor> tableau, int deb, int fin) {
		if (deb != fin) {
			int milieu = (fin + deb) / 2;
			triFusion(tableau, deb, milieu);
			triFusion(tableau, milieu + 1, fin);
			fusion(tableau, deb, milieu, fin);
		}
	}

	private static void fusion(List<DistanceObjectColor> tableau, int deb1, int fin1, int fin2) {
		int deb2 = fin1 + 1;

		ArrayList<DistanceObjectColor> table1 = new ArrayList<DistanceObjectColor>();
		for (int i = deb1; i <= fin1; i++) {
			table1.add(tableau.get(i));
		}
		int compt1 = deb1;
		int compt2 = deb2;
		for (int i = deb1; i <= fin2; i++) {
			if (compt1 == deb2) {
				break;
			} else if (compt2 == (fin2 + 1)) {
				tableau.set(i, table1.get(compt1 - deb1));

				compt1++;
			} else if (table1.get(compt1 - deb1).getDistance() < tableau.get(compt2).getDistance()) {
				tableau.set(i, table1.get(compt1 - deb1));
				compt1++;
			} else {
				tableau.set(i, tableau.get(compt2));
				compt2++;
			}
		}
	}

	public static void main(String[] args) {
		Robot robot = new Robot();
		FindColor find = new FindColor(robot.NAMEFILE);
		find.whatColor(robot.lireColor(), true);
	}

}