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
		int c = (vitesseLeft < vitesseRight) ? 0 : 1;						// je recupere la direction dans lequel le robot tourne
		long times2 = 250;
		long times = System.currentTimeMillis();
		this.ev3.setVitesse(TP, TP); 										// je remet une vitesse constante
		while (seenColor != ev3.SUIVRE) { 									// tant quil a pas retrouve la ligne
			if (System.currentTimeMillis() - times > times2) { 				// swap direction
				c++;														// je change la direction
				times2 += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				times = System.currentTimeMillis();
			} else {
				if (c % 2 == 0) { 											// choisis la direction dans laquel je tourne
					ev3.rotateD();											// a droite
				} else {													// ou 
					ev3.rotateG();											// a gauche
				}
			}
			seenColor = find.whatColor(ev3.lireColor());					// lire la couleur
		}
		this.ev3.stop();
		this.ev3.setVitesse(vitesseLeft, vitesseRight);						// j'inverse la vitesse des roues par rapport à la vitesse initiale
		return c % 2;
	}

	public void avance() {
		int calculRTT = 0, lastSeen = -1;
		double lastError = 0, integral = 0;
		long nbBack = System.currentTimeMillis(), calculRttREf = System.currentTimeMillis();
		long onRTT = Long.MAX_VALUE;
		while ((this.seenColor = this.find.whatColor(ev3.lireColor())) != this.ev3.DEPART) {
			if (seenColor == lastSeen && seenColor == this.ev3.SUIVRE) {
				nbBack = System.currentTimeMillis();
			}
			// calcul du RTT des le debut
			if (calculRTT < 2 && seenColor != lastSeen) {
				calculRTT++;
				if (calculRTT == 2) {
					onRTT = System.currentTimeMillis() - calculRttREf;
					onRTT += onRTT / 2;
				}
			}
			// si on est sorti depuis > RTT alors pivot
			if (System.currentTimeMillis() - nbBack > onRTT) {
				pivot();
				nbBack = System.currentTimeMillis();
				integral = 0;
				lastError = 0;
			} else {// sinon j'ajuste la valeur de mes roues
				double error = this.seenColor - OFFSET;
				integral += error;
				double derivative = error - lastError;
				double turn = KP * error + KI * integral + KD * derivative;
				this.ev3.avance((int) (TP + turn), (int) (TP - turn));
				lastError = error;
			}
			lastSeen = seenColor;
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
