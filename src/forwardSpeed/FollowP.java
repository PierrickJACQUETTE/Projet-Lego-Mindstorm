package forwardSpeed;

import color.FindColor;

public class FollowP {

	private Robot ev3;
	private FindColor find;
	private int seenColor;
	private final int KP = 150;
	private final int KI = 10;
	private final int KD = 1000;
	private final double OFFSET;
	private final int TP = 300;

	public FollowP() {
		this.ev3 = new Robot();
		this.ev3.setVitesse(TP, TP);
		this.find = new FindColor(this.ev3.NAMEFILE);
		OFFSET = ((double) (this.ev3.BACKGROUND - this.ev3.SUIVRE)) / 2 + this.ev3.SUIVRE;
	}

	private int pivot() {
		this.ev3.stop();
		int vitesseRight = this.ev3.getVitesseRight();
		int vitesseLeft = this.ev3.getVitesseLeft();
		int c = (vitesseLeft < vitesseRight) ? 0 : 1;
		long times2 = 250;
		long times = System.currentTimeMillis();
		this.ev3.setVitesse(300, 300);
		while (seenColor != ev3.SUIVRE) {
			if (System.currentTimeMillis() - times > times2) {
				c++;
				times2 += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				times = System.currentTimeMillis();
			} else {
				if (c % 2 == 0) {
					ev3.rotateD();
				} else {
					ev3.rotateG();
				}
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
		this.ev3.stop();
		this.ev3.setVitesse(vitesseLeft, vitesseRight);
		return c % 2;
	}

	public void avance() {
		long onRTT = Long.MAX_VALUE;
		int calculRTT = 0;
		double lastError = 0;
		double integral = 0;
		int lastSeen = -1;
		long nbBack = System.currentTimeMillis();
		long calculRttREf = System.currentTimeMillis();
		this.seenColor = this.find.whatColor(ev3.lireColor());
		while (this.seenColor != this.ev3.DEPART) {
			if (seenColor == lastSeen && seenColor == this.ev3.SUIVRE) {
				nbBack = System.currentTimeMillis();
			}
			if (calculRTT < 2 && seenColor != lastSeen) {
				calculRTT++;
				if (calculRTT == 2) {
					onRTT = System.currentTimeMillis() - calculRttREf;
					onRTT += onRTT / 2;
					System.out.println(onRTT);
				}
			}
			if (System.currentTimeMillis() - nbBack > onRTT) {
				pivot();
				nbBack = System.currentTimeMillis();
				integral = 0;
				lastError = 0;
			} else {
				double error = this.seenColor - OFFSET;
				integral += error;
				double derivative = error - lastError;
				double turn = KP * error + KI * integral + KD * derivative;
				this.ev3.avance((int) (TP + turn), (int) (TP - turn));

				lastError = error;
			}
			lastSeen = seenColor;
			this.seenColor = this.find.whatColor(ev3.lireColor());
		}
	}

	public void inLineBlack() {
		this.seenColor = this.find.whatColor(ev3.lireColor());
		while (this.seenColor == this.ev3.DEPART) {
			this.ev3.avance(300, 300);
			this.seenColor = this.find.whatColor(ev3.lireColor());
		}
	}

	public static void main(String[] args) {
		FollowP f = new FollowP();
		f.inLineBlack();
		System.out.println("tour1");
		f.avance();
		f.inLineBlack();
		System.out.println("tour2");
		f.avance();
	}

}
