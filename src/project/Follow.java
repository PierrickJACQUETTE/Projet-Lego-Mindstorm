package project;

public class Follow {

	private Robot ev3;
	private FindColor find;
	private int seenColor;
	private int georges;// connaitre le nombre de fois ou l'on tourne de la meme
						// direction
	private int c;// direction courante
	private int lastC;// derniere direction
	long TEMPSGEORGESMAX = 1500;// temps pour decider quand on reinitialise
	// georges
	int DIRECTIONGAUCHE = 2;
	int DIRECTIONDROITE = 0;
	private boolean virage;// vaut vrai quand on execute tourne pour pouvoir
							// continuer sur la methode suiteTourne et pas
							// ligneDroite => while

	public Follow() {
		ev3 = new Robot();
		find = new FindColor(ev3.NAMEFILE);
		georges = 0;
		virage = false;
		c = 0;
		lastC = 1;
	}

	/*
	 * Methode permettant que le robot avance en ligne droite tant qu il lit la
	 * bonne couleur il avant en accelerant un peu a chaque tour de boucle quand
	 * cela fait 30 fois qu il accelere alors on le ralentit jusqu a 7 puis on
	 * recommence
	 */
	protected void ligneDroite() throws InterruptedException {

		boolean accelerer = true;// permet de savoir si on accelere ou freine
		while (ev3.SUIVRE == seenColor) {
			ev3.begSync();
			ev3.avance();
			ev3.endSync();
			if (ev3.getLigne() < 30 && accelerer) {
				ev3.accelerer();
				ev3.setLigne(ev3.getLigne() + 1);
			} else if (ev3.getLigne() == 30 && accelerer) {
				accelerer = false;
			} else if (ev3.getLigne() == 7) {
				accelerer = true;
			} else if (accelerer == false) {
				ev3.decelerer();
				ev3.setLigne(ev3.getLigne() - 1);
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
	}

	/*
	 * permet de faire un pivot sur place dans un sens puis l autre
	 */
	protected void pivot() throws InterruptedException {
		c = 0; // direction initial
		long timesPivot = 250; // temps du premier pivot
		long timesPivotCourant = System.currentTimeMillis(); // temps courant
		boolean first = true;// premiere fois que l on rentre dans la boucle
		// quand on a essayer 10 fois et que l on a rien trouver on s arrete
		while (seenColor != ev3.SUIVRE && c < 10) {
			if (first) {// on indique la futur direction pour tourne que la
						// premiere fois

				c = lastC % 2;// on recuperere la derniere valeur de c pour
								// d'abord tester cette direction
				switch (c) {
				case 0:
					ev3.setDirection(DIRECTIONDROITE);
					georges++;
					break;
				case 1:
					ev3.setDirection(DIRECTIONGAUCHE);
					break;
				}
				first = false;
			} else {
				if (c % 2 == 0) {// en fonction de la direction je vais à gauche
									// ou droite
					ev3.avance(DIRECTIONDROITE);
				} else {
					ev3.avance(DIRECTIONGAUCHE);
				}
			}
			// si on a effectue un pivot dun certain temps, on alterne la
			// direction et augmente le temps du pivot
			if (System.currentTimeMillis() - timesPivotCourant > timesPivot) {
				c++;// je vais tester la direction opposee
				first = true;// ca sera la prochaine fois la premiere fois
				// pivot de plus en plus grand, donc on augmente le temps d'un
				// pivot
				timesPivot += (c < 3) ? (c % 2 == 0) ? 250 : 260 : (c % 2 == 0) ? 500 : 520;
				// on reprend comme referentiel le temps courant
				timesPivotCourant = System.currentTimeMillis();
			}
			seenColor = find.whatColor(ev3.lireColor());
		}
	}

	/*
	 * cela est appele quand on sort d'un virage pour continuer a tourner et pas
	 * faire une ligne droite je tourne dans la meme direction en augmentant
	 * dans le temps la contre poussee
	 */
	protected void suiteTourne() throws InterruptedException {
		int direction = ev3.getDirection();
		while (seenColor == ev3.SUIVRE) {
			ev3.suiteTourne(direction);
			ev3.setCourbe(ev3.getCourbe() + 1);
			seenColor = find.whatColor(ev3.lireColor());
			if (ev3.getCourbe() % 10 == 0) {
				georges = 0;
				ev3.setDirection(1);
			}
		}
		ev3.setDirection(1);
	}

	/*
	 * permet de tourner : presque la meme fonction que suiteTourne, suiteTourne
	 * code provisoire
	 */

	protected void tourne() throws InterruptedException {
		while (seenColor != ev3.SUIVRE) {
			ev3.tourne(ev3.getDirection());
			ev3.setCourbe(ev3.getCourbe() + 1);
			seenColor = find.whatColor(ev3.lireColor());
			if (ev3.getCourbe() % 10 == 0) {
				georges = 0;
			}
		}
	}

	protected void start() throws InterruptedException {
		long tempsGeorges = System.currentTimeMillis();
		do {
			seenColor = find.whatColor(ev3.lireColor());
			// quand je sors de la fonction tourne je veux continuer à tourner
			// sinon cest que je suis en ligne droite
			if (virage) {
				suiteTourne();
				virage = false;
				ev3.vitesseMoyenne();
			} else {
				ev3.setLigne(1);
				ligneDroite();
			}
			// si jai perdu la ligne alors je cherche ma direction : pivot
			// si je la connais alors je tourne
			if (ev3.getDirection() == 1) {
				ev3.changeVitesse(150, 150);
				pivot();
			} else {
				ev3.vitesseMoyenne();
				tourne();
				virage = true;
			}
			// si je suis en ligne droite depuis longtemps j'annule mon pivot
			if (System.currentTimeMillis() - tempsGeorges > TEMPSGEORGESMAX) {
				georges = 0;
				tempsGeorges = System.currentTimeMillis();
			}
			// si je sors une fois ou pas du meme cote que la derniere fois
			// j'annule la direction
			if (georges == 1 || georges == 2 || c % 2 != lastC % 2) {
				ev3.setDirection(1);
			}
			lastC = c;
		} while (seenColor == ev3.SUIVRE);
		ev3.robotFin();
	}

	public static void main(String[] args) throws InterruptedException {
		Follow f = new Follow();
		f.start();
		f.ev3.robotFin();
	}

}
