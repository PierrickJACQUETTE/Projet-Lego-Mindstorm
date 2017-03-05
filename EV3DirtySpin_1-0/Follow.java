package Samuel;

public class Follow {

	private Robot ev3;
	private FindColor find;
	private int seenColor;

	public Follow() {
		ev3 = new Robot();
		find = new FindColor(ev3.NAMEFILE);
	}

	private void pivot() {
		int c = 0;
		long timesPivot = 150;
		long timesPivotCourant = System.currentTimeMillis();

		while (seenColor != ev3.SUIVRE && c < 10) {
			if (c % 2 == 0) {
				ev3.avance(ev3.DROITE);
			} else {
				ev3.avance(ev3.GAUCHE);
			}
			if (System.currentTimeMillis() - timesPivotCourant > timesPivot) {
				c++;
				timesPivot += (c < 3) ? (c % 2 == 0) ? 100 : 110 : (c % 2 == 0) ? 200 : 220;
				timesPivotCourant = System.currentTimeMillis();
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
	}
	
	protected void start()  {
		do {
			seenColor = find.whatColor(ev3.lireColor());
			ev3.avance();
			if(seenColor != ev3.SUIVRE){
				pivot();
			}
		} while (seenColor == ev3.SUIVRE);
		ev3.robotFin();
	}

	public static void main(String[] args) {
		Follow f = new Follow();
		f.start();
		f.ev3.robotFin();
	}

}
