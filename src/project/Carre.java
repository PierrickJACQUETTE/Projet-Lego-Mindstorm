package project;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class Test {
	
	private static int roueDiam = 6;
	private static int roboDiam = 15;
	
	
	//ATTENTION LES MOTEURS SONT EN 3:1	
	
	/* On calcule le rapport distance avec le diametre de la roue.
	 * Puis en convertit en degree.
	 * */	
	public static void avance(RegulatedMotor x,RegulatedMotor y, int distance){
		int deg = (int) Math.round((distance/roueDiam)*360.0)/3;
		x.rotate(deg, true);
		y.rotate(deg);
	}	
	
	/* roboDiam * PI represente la distance que parcours le robot en faisant un tour complet sur lui meme.
	 * Ici on veux juste 90° donc on divise par 4 [360/4].
	 * On fait ensuite la conversion en degree.
	 * */
	public static void tourneD(RegulatedMotor x, RegulatedMotor y){
		int deg = (int) Math.round((((roboDiam*Math.PI)/4.0)/roueDiam)*360.0)/3;
		x.rotate(-deg,true);
		y.rotate(deg);
	}
	
	
	public static void main(String[] args){
	
		RegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.D);
		
		for(int i = 0; i < 4; i++){
			avance(right, left, 30); //AVANCE SUR UNE DISTANCE DE 30CM
			tourneD(right, left); //TOURNE A 90 DEGRES SUR LA DROITE
		}		
	}
}
