import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Carreau {

	public static void main(String[] args) {

		EV3LargeRegulatedMotor b = new EV3LargeRegulatedMotor(MotorPort.B);
		EV3LargeRegulatedMotor d = new EV3LargeRegulatedMotor(MotorPort.D);
		b.synchronizeWith(new RegulatedMotor[] { d });

		b.startSynchronization();
		for (int i = 0; i < 4; i++) {
			b.forward();
			d.forward();
			Delay.msDelay(3000);
			b.rotate(90, true);
			d.rotate(90, true);
		}
		b.endSynchronization();

		b.waitComplete();
		d.waitComplete();
	}

}
