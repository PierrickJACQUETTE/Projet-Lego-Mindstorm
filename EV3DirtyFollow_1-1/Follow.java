package Samuel;

import lejos.hardware.Button;

public class Follow {

	private Robot ev3;
	private FindColor find;
	private int seenColor, speedL, speedR;

	public Follow() {
		ev3 = new Robot();
		find = new FindColor(ev3.NAMEFILE);
		speedL = ev3.left.getSpeed();
		speedR = ev3.right.getSpeed();
	}
	
	private void goLeft() {
		ev3.avance(ev3.GAUCHE);
	}
	
	protected void start() {
		int l = 0, r = 0;
		while(true){	
			seenColor = find.whatColor(ev3.lireColor());
			ev3.avance();
			if(seenColor != ev3.SUIVRE){
				ev3.left.setSpeed(speedL);
				ev3.right.setSpeed(speedR/3);
				l = 0;
				r++;
				if(r > 30){
					do{
						Button.LEDPattern(2);
						goLeft();
						seenColor = find.whatColor(ev3.lireColor());
						Button.LEDPattern(0);
					} while(seenColor != ev3.SUIVRE);
				} else if(r > 10){
					Button.LEDPattern(1);
					ev3.left.setSpeed((int)(speedL*1.5));
					ev3.right.setSpeed((int)(speedR/41.5));
					Button.LEDPattern(0);
				}
			} else {
				ev3.right.setSpeed(speedR);
				ev3.left.setSpeed(speedL/3);
				l++;
				r = 0;
				if(l > 10){
					ev3.right.setSpeed((int)(speedR*1.5));
					ev3.left.setSpeed((int)(speedL/4.5));
				}
			}
		}
	}

	public static void main(String[] args) {
		Follow f = new Follow();
		f.start();
		f.ev3.robotFin();
	}

}
