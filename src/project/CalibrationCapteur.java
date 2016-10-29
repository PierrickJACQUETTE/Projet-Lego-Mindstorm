package project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class CalibrationCapteur {

	public static float convertToRGB(float f) {
		return (float) ((f == 1) ? 255 : f * 256.0);
	}

	public static void main(String[] args) {
		Port p = LocalEV3.get().getPort("S1");
		HiTechnicColorSensor ecs = new HiTechnicColorSensor(p);
		SampleProvider colorRGBSensor = ecs.getRGBMode();
		int sampleSize = colorRGBSensor.sampleSize();
		float[] sample = new float[sampleSize];
		for (int i = 0; i < sample.length; i++) {
			colorRGBSensor.fetchSample(sample, 0);
			LCD.drawString(i + " " + convertToRGB(sample[i]), 0, 4);
			Delay.msDelay(3000);

		}
	}
}