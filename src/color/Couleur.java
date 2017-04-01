package color;

public class Couleur {

	private float red;
	private float green;
	private float blue;
	private short name;

	/**
	 * Constructeur on fixe le nom d une couleur, sa coloration en rouge, bleu
	 * et vert
	 * 
	 * @param red
	 *            le codage du rouge
	 * @param green
	 *            le codage du vert
	 * @param blue
	 *            le codage du bleu
	 * @param name
	 *            le nom de la couleur
	 */
	public Couleur(float red, float green, float blue, short name) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.name = name;
	}

	/**
	 * Retourne la coloration rouge de la couleur
	 * 
	 * @return the red
	 */
	protected float getRed() {
		return red;
	}

	/**
	 * Retourne la coloration verte de la couleur
	 * 
	 * @return the green
	 */
	protected float getGreen() {
		return green;
	}

	/**
	 * Retourne la coloration bleue de la couleur
	 * 
	 * @return the blue
	 */
	protected float getBlue() {
		return blue;
	}

	/**
	 * Retourne le nom de la couleur
	 * 
	 * @return l identifiant de la couleur
	 */
	protected short getName() {
		return name;
	}

	/**
	 * Met a jour le nom de la couleur
	 * 
	 * @param name
	 *            le nouveau nom
	 */
	protected void setName(short name) {
		this.name = name;
	}

	/**
	 * @return texte representant une couleur
	 */
	public String toString() {
		return this.red + " " + this.green + " " + this.blue + " " + this.name + "\r";
	}

	/**
	 * Fonction permettant de calculer la distance d euclide entre deux couleurs
	 * 
	 * @param c
	 *            la couleur de comparaison avec la couleur courante
	 * @return la distance entre les deux couleurs
	 */
	protected float euclide(Couleur c) {
		float dRed = c.red - this.red;
		float dGreen = c.green - this.green;
		float dBlue = c.blue - this.blue;
		return (float) Math.sqrt((dRed * dRed) + (dGreen * dGreen) + (dBlue * dBlue));
	}
}
