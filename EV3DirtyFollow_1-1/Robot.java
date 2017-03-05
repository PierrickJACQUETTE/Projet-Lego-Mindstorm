package Samuel;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.I2CException;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.HiTechnicColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Robot {

	protected final String NAMEFILE = "Couleur.txt";
	protected final short SUIVRE = 1;
	protected final short VITESSEMOYENNE;

	protected final short DROITE = 0;
	protected final short DEVANT = 1;
	protected final short GAUCHE = 2;
	
	RegulatedMotor right, left;
	private HiTechnicColorSensor ecs;
	private SampleProvider colorRGBSensor;

	public Robot() {
		this.right = new EV3LargeRegulatedMotor(MotorPort.B);
		this.left = new EV3LargeRegulatedMotor(MotorPort.D);
		this.VITESSEMOYENNE = (short) ((right.getSpeed() < left.getSpeed()) ? right.getSpeed() : left.getSpeed());
		this.ecs = new HiTechnicColorSensor(LocalEV3.get().getPort("S1"));
		this.colorRGBSensor = ecs.getRGBMode();
	}

	public void robotFin() {
		this.ecs.close();
		this.right.stop();
		this.left.stop();
	}

	protected void avance() {
		this.avance(DEVANT);
	}

	protected void avance(int direction) {
		this.begSync();
		switch (direction) {
		case DEVANT:
			this.right.forward();
			this.left.forward();
			break;
		case GAUCHE:
			this.left.backward();
			this.right.forward();
			break;
		case DROITE:
			this.right.backward();
			this.left.forward();
			break;
		default:
			break;
		}
		this.endSync();
	}

	protected Couleur lireColor()  {
		float[] sample = new float[this.colorRGBSensor.sampleSize()];
		while (true) {
			try {
				this.colorRGBSensor.fetchSample(sample, 0);
				Thread.sleep(10);
				break;
			} catch (Exception e) {
			    LCD.drawString("Une erreur est apparue lors du fetch", 0, 1);
			}
		}
		return new Couleur(this.convertToRGB(sample[0]), this.convertToRGB(sample[1]), this.convertToRGB(sample[2]),
				(short)0);
	}

	private float convertToRGB(float f) {
		return f * 255f;
	}

	public void begSync() {
		this.left.startSynchronization();
		this.right.startSynchronization();
	}

	public void endSync() {
		this.right.endSynchronization();
		this.left.endSynchronization();
		Delay.msDelay(10);
	}
}
