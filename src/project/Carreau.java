import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Carreau {
	public static void main(String[] args) {
		RegulatedMotor b = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor d = new EV3LargeRegulatedMotor(MotorPort.D);
		b.synchronizeWith(new RegulatedMotor[] { d });

		for (int i = 0; i < 4; i++) {
			avance(b, d);

		}
		b.stop();
		d.stop();
	}

	public static void avance(RegulatedMotor b, RegulatedMotor d) {
		b.startSynchronization();
		b.forward();
		d.forward();
		b.endSynchronization();
		Delay.msDelay(1500);
		b.rotateTo(90);
		
	}
}
