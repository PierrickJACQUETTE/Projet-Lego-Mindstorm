package Samuel;

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
	
	protected void start() {
		while(true){	
			seenColor = find.whatColor(ev3.lireColor());
			ev3.avance();
			if(seenColor != ev3.SUIVRE){
				ev3.left.setSpeed(speedL/2);
				ev3.right.setSpeed(speedR);
			} else {
				ev3.right.setSpeed(speedR/2);
				ev3.left.setSpeed(speedL);
			}
		}
	}

	public static void main(String[] args) {
		Follow f = new Follow();
		f.start();
		f.ev3.robotFin();
	}

}
