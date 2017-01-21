package project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robot {

	RegulatedMotor right, left;
	
	private static int roueDiam = 5;
	private static int roboDiam = 15;
	
	static int direction = 1;
	static int ligne = 1;
	static int courbe = 1;
	static int suivre = 1;
	
	public Robot(){
		right = new EV3LargeRegulatedMotor(MotorPort.B);
		left = new EV3LargeRegulatedMotor(MotorPort.D);
	}
	
	public void begSync() {
		left.synchronizeWith(new RegulatedMotor[] { right });
		right.startSynchronization();
	}

	
	public void avance() { 
		right.forward(); 
		left.forward(); 
	}

	public void endSync() { right.endSynchronization(); Delay.msDelay(100); }
	
	
	//ATTENTION LES MOTEURS SONT EN 3:1	
	//On calcule le rapport distance avec le diametre de la roue. Puis en convertit en degree.	
	public void avance(int distance){
		int deg = (int) Math.round((distance/roueDiam)*360.0)/3;
		right.rotate(deg, true);
		left.rotate(deg);
	}	
	
	/* Tourne a droite
	 * roboDiam * PI represente la distance que parcours le robot en faisant un tour complet sur lui meme.
	 * Ici on veux juste 90% donc on divise par 4 [360/4].
	 * On fait ensuite la conversion en degree.
	 * */
	public void pivotD(int ang){
		int deg = (int) Math.round((((roboDiam*Math.PI)/(360/ang))/roueDiam)*360.0)/3;
		right.rotate(-deg,true);
		left.rotate(deg);
	}
	
	public void pivotG(int ang){
		int deg = (int) Math.round((((roboDiam*Math.PI)/(360/ang))/roueDiam)*360.0)/3;
		left.rotate(-deg,true);
		right.rotate(deg);
	}
	
	public void tourneD(){		
		right.forward();
		Delay.msDelay(100);
	}
	
	public void tourneG(){
		left.forward();
		Delay.msDelay(100);
	}
	
	public void accelerer(){
		right.setSpeed(ligne*100);
		left.setSpeed(ligne*100);
		courbe = 1;
	}
	
	public void ralentir(){
		right.setSpeed(right.getSpeed()/100);
		left.setSpeed(left.getSpeed()/100);
		ligne = 1;
	}
}
