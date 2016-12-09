package project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Follow {

	public static void begSync(RegulatedMotor a, RegulatedMotor b) {
		b.synchronizeWith(new RegulatedMotor[] { a });
		a.startSynchronization();
	}

	public static void avance(RegulatedMotor a, RegulatedMotor b) {
		a.forward();
		b.forward();
	}

	public static void endSync(RegulatedMotor a, RegulatedMotor b) {
		a.endSynchronization();
		Delay.msDelay(300);
	}

	public static void main(String[] args) {

		RegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.D);

		FindColor find = new FindColor();
		int followedColor = find.whatColor(Util.readColor(), false);
		int seenColor;

		do {
			begSync(right, left);
			avance(right, left);
			endSync(right, left);
			seenColor = find.whatColor(Util.readColor(), false);
			int c = 0;
			while (seenColor != followedColor && c < 10) {
				switch (c % 2) {
				case 0:
					Util.tourne(right, left, 5 * (++c));
					seenColor = find.whatColor(Util.readColor(), false);
					break;
				case 1:
					Util.tourne(left, right, 5 * (++c));
					seenColor = find.whatColor(Util.readColor(), false);
					break;
				}
			}
		} while (seenColor == followedColor);
	}
}
