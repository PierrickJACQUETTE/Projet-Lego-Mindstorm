package project;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;

public class TestMovePilot2 {
	
	public static void main(String[] args) {
		
		boolean choice = true;
		RegulatedMotor b = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor d = new EV3LargeRegulatedMotor(MotorPort.D);
		MovePilot pilot = new MovePilot(81.6, 5, b, d);
		
		OdometryPoseProvider opp;
		if(choice){
			Navigator nav = new Navigator(pilot);
			opp = (OdometryPoseProvider) nav.getPoseProvider();
		} else{
			opp = new OdometryPoseProvider(pilot);
		}
		Pose pose = opp.getPose();
		float x = pose.getX();
		float y = pose.getY();
		LCD.drawString("X:"+x, 0, 1); //X: 0 0 
		LCD.drawString("Y:"+y, 0, 2); //Y: 0 0
		LCD.drawString("Avance", 0, 3);
		long time = System.currentTimeMillis();
		while(System.currentTimeMillis() - time < 5000 ){
			b.forward();
			d.forward();
		}
		pose = opp.getPose();
		x = pose.getX();
		y = pose.getY();
		LCD.drawString("X:"+x, 0, 4); //X: 0 0
		LCD.drawString("Y:"+y, 0, 5); //Y: 0 0
		Delay.msDelay(5000);
	}
}
