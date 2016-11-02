package project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.SampleProvider;

public class Util {

	protected static final String NAMEFILE = "Couleur.txt";
	protected static final int DELAY = 4000;
	
	protected static Color readColor() {
		Port p = LocalEV3.get().getPort("S1");
		HiTechnicColorSensor ecs = new HiTechnicColorSensor(p);
		SampleProvider colorRGBSensor = ecs.getRGBMode();
		int sampleSize = colorRGBSensor.sampleSize();
		float[] sample = new float[sampleSize];
		colorRGBSensor.fetchSample(sample, 0);
		ecs.close();
		return new Color(convertToRGB(sample[0]),convertToRGB(sample[1]) , convertToRGB(sample[2]), "");
	}
	
	private static float convertToRGB(float f) {
		return (f == 1f) ? 1200f : 1201f * f;
	}
}
