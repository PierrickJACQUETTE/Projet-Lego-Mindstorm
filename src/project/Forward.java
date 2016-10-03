package project;

import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Forward {

	public static void main(String[] args) {
		Motor.A.forward();
		Delay.msDelay(5000);
		Motor.A.stop();
		Motor.A.backward();
		Delay.msDelay(5000);
		Motor.A.stop();
	}

}
