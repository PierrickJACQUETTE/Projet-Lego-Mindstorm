package project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class FindColor {

	ArrayList<Color> colorLearned;
	ArrayList<Float> epsilon;

	public FindColor() {
		this.colorLearned = readFile();
		this.epsilon = new ArrayList<Float>();
	}

	private ArrayList<Color> readFile() {
		ArrayList<Color> list = new ArrayList<Color>();
		try {
			BufferedReader fluxEntree = new BufferedReader(new FileReader(Util.NAMEFILE));
			String ligne;
			while ((ligne = fluxEntree.readLine()) != null) {
				String[] read = ligne.split(" ");
				if (read.length == 4) {
					list.add(new Color(Float.parseFloat(read[0]), Float.parseFloat(read[1]), Float.parseFloat(read[2]),
							read[3]));
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

	public void whatColor(Color c) {
		for (Color learn  : this.colorLearned) {
			this.epsilon.add(euclide(c, learn));
		}
		float min = 99999f;
		int index = this.colorLearned.size();
		for (int i = 0; i < this.epsilon.size(); i++) {
			if (this.epsilon.get(i) <= min) {
				min = this.epsilon.get(i);
				index = i;
			}
		}
		if (index == this.colorLearned.size()) {
			LCD.drawString(" Color not found", 0, 4);
			Delay.msDelay(Util.DELAY);
		} else {
			LCD.drawString("color : " + this.colorLearned.get(index).getName(), 0, 4);
			Delay.msDelay(Util.DELAY);
		}
	}

	private float euclide(Color c, Color ref) {
		float dRed = c.getRed() - ref.getRed();
		float dGreen = c.getGreen() - ref.getGreen();
		float dBlue = c.getBlue() - ref.getBlue();
		return (float) Math.sqrt((dRed * dRed) + (dGreen * dGreen) + (dBlue * dBlue));
	}

	public static void main(String[] args) {
		FindColor find = new FindColor();
		find.whatColor(Util.readColor());
	}

}
