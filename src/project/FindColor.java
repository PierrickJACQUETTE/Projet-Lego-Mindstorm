package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class FindColor {

	private ArrayList<DistanceColor> distanceColor;
	private int ensemble;

	public FindColor() {
		this.distanceColor = this.readFile();
		this.ensemble = this.distanceColor.size() / nbColour();
	}

	private int nbColour() {
		int cpt = 0;
		ArrayList<Integer> idColor = new ArrayList<Integer>();
		for (DistanceColor d : this.distanceColor) {
			if (!idColor.contains(d.getColor().getName())) {
				cpt++;
				idColor.add(d.getColor().getName());
			}
		}
		return cpt;
	}

	private ArrayList<DistanceColor> readFile() {
		ArrayList<DistanceColor> list = new ArrayList<DistanceColor>();
		try {
			BufferedReader fluxEntree = new BufferedReader(new FileReader(Util.NAMEFILE));
			String ligne;
			while ((ligne = fluxEntree.readLine()) != null) {
				String[] read = ligne.split(" ");
				if (read.length == 4) {
					list.add(new DistanceColor(new Color(Float.parseFloat(read[0]), Float.parseFloat(read[1]),
							Float.parseFloat(read[2]), Integer.parseInt(read[3])), 9999f));
				}
			}
			fluxEntree.close();
		} catch (FileNotFoundException e) {
			System.out.println(" File not found");
			LCD.drawString(" File not found", 0, 4);
			Delay.msDelay(Util.DELAY);
		} catch (IOException e) {
			LCD.drawString(" Error open", 0, 4);
			Delay.msDelay(Util.DELAY);
		}
		return list;
	}

	// affichage 1 quand find, 0 sinon quand on avance
	protected int whatColor(Color c, boolean affichage) {
		for (int i = 0; i < this.distanceColor.size(); i++) {
			this.distanceColor.get(i).setDistance(this.distanceColor.get(i).getColor().euclide(c));
		}
		triFusion(this.distanceColor);
		int index = this.maxOccurence(this.ensemble);

		if (index == this.distanceColor.size()) {
			if (affichage == true) {
				LCD.drawString(" Color not found", 0, 4);
				Delay.msDelay(Util.DELAY);
			}
			return -1;
		} else {
			if (affichage == true) {
				LCD.drawString("color : " + index, 0, 4);
				Delay.msDelay(Util.DELAY);
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
			nbrOccurrence[this.distanceColor.get(i).getColor().getName()]++;
		}
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

	private static void triFusion(ArrayList<DistanceColor> tableau) {
		int longueur = tableau.size();
		if (longueur > 0) {
			triFusion(tableau, 0, longueur - 1);
		}
	}

	private static void triFusion(ArrayList<DistanceColor> tableau, int deb, int fin) {
		if (deb != fin) {
			int milieu = (fin + deb) / 2;
			triFusion(tableau, deb, milieu);
			triFusion(tableau, milieu + 1, fin);
			fusion(tableau, deb, milieu, fin);
		}
	}

	private static void fusion(ArrayList<DistanceColor> tableau, int deb1, int fin1, int fin2) {
		int deb2 = fin1 + 1;

		ArrayList<DistanceColor> table1 = new ArrayList<DistanceColor>();
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
		FindColor find = new FindColor();
		find.whatColor(Util.readColor(), true);
	}

}