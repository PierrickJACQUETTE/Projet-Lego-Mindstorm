package forwardSpeed;

import color.FindColor;

public class FollowNotLearn {

	private Robot ev3;

	private FindColor find;
	private int seenColor;

	public FollowNotLearn() {
		this.ev3 = new Robot();
		this.find = new FindColor(this.ev3.NAMEFILE);
	}

	private void pivot() {
		int c = this.ev3.getDirection();
		long times2 = 250, times = System.currentTimeMillis();
		this.ev3.setVitesse(150, 150);
		boolean end = false, endP = false;
		while (seenColor != ev3.SUIVRE) {
			if (System.currentTimeMillis() - times > times2) {
				c++;
				times2 += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				times = System.currentTimeMillis();
				end = true;
				endP = true;
			} else {
				if (c % 2 == 0) {
					ev3.rotateD();
				} else {
					ev3.rotateG();
				}
				endP = (end) ? true : false;
				end = false;
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
		this.ev3.stop();
		if (!(end == false && endP == false)) {
			c--;
		}
		this.ev3.setDirection(c % 2);
		this.ev3.setVitesse(400, 400);
	}

	private void tour1() {
		do {
			if (this.ev3.getLastDirection() == this.ev3.getDirection()) {
				if (this.ev3.getLastDirection() == 1) {
					this.ev3.setVitesse(700, 300);
				} else {
					this.ev3.setVitesse(300, 700);
				}
			}
			this.ev3.arc();
			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.ev3.setLastDirection(this.ev3.getDirection());
				this.pivot();
			}
		} while (this.seenColor == this.ev3.SUIVRE && this.seenColor != this.ev3.DEPART);
		this.ev3.stop();
	}

	private void inLineBlack() {
		do {
			this.ev3.arc();
			this.seenColor = this.find.whatColor(ev3.lireColor());
			if (this.seenColor == this.ev3.BACKGROUND) {
				this.pivot();
			}
		} while (this.seenColor == this.ev3.DEPART);
		this.ev3.stop();
	}

	public static void main(String[] args) {
		FollowNotLearn f = new FollowNotLearn();
		f.inLineBlack();
		f.tour1();
		f.inLineBlack();
		f.tour1();
		f.inLineBlack();
	}

}