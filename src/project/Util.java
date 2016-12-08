package project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Util {

	protected static final String NAMEFILE = "Couleur.txt";
	protected static final int DELAY = 500;
	
	private static int roueDiam = 5;
	private static int roboDiam = 15;

	protected static Color readColor() {
		Port p = LocalEV3.get().getPort("S1");
		HiTechnicColorSensor ecs = new HiTechnicColorSensor(p);
		SampleProvider colorRGBSensor = ecs.getRGBMode();
		int sampleSize = colorRGBSensor.sampleSize();
		float[] sample = new float[sampleSize];
		colorRGBSensor.fetchSample(sample, 0);
		ecs.close();
		return new Color(convertToRGB(sample[0]), convertToRGB(sample[1]), convertToRGB(sample[2]), 0);
	}

	private static float convertToRGB(float f) {
		return f * 255f;
	}

	protected static void DelayClearLCD() {
		Delay.msDelay(Util.DELAY);
		LCD.clearDisplay();
	}
	
	//ATTENTION LES MOTEURS SONT EN 3:1	
	
	/* On calcule le rapport distance avec le diametre de la roue.
	 * Puis en convertit en degree.
	 * */	
	public static void avance(RegulatedMotor x,RegulatedMotor y, int distance){
		int deg = (int) Math.round((distance/roueDiam)*360.0)/3;
		x.rotate(deg, true);
		y.rotate(deg);
	}	
	
	/* Tourne à droite
	 * roboDiam * PI represente la distance que parcours le robot en faisant un tour complet sur lui meme.
	 * Ici on veux juste 90% donc on divise par 4 [360/4].
	 * On fait ensuite la conversion en degree.
	 * */
	public static void tourne(RegulatedMotor x, RegulatedMotor y, int ang){
		int deg = (int) Math.round((((roboDiam*Math.PI)/(360/ang))/roueDiam)*360.0)/3;
		x.rotate(-deg,true);
		y.rotate(deg);
	}
}
