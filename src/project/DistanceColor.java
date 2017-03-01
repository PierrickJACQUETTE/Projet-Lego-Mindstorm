package project;

/**
 * @author pierrick
 *
 */
public class DistanceColor {

	private Couleur color;
	private float distance;

	/**
	 * Constructeur permettant de stocker une distance d'une couleur en fonction
	 * d'une couleur par rapport à une autre couleur
	 * 
	 * @param color
	 *            la couleur connue
	 * @param distance
	 *            la distance entre la couleur connue et la couleur courante
	 */
	public DistanceColor(Couleur color, float distance) {
		this.color = color;
		this.distance = distance;
	}

	/**
	 * Retourne la couleur
	 * 
	 * @return the color
	 */
	protected Couleur getColor() {
		return color;
	}

	/**
	 * Retourne la distance
	 * 
	 * @return the distance
	 */
	protected float getDistance() {
		return distance;
	}

	/**
	 * Met a jour la distance d'une couleur
	 * 
	 * @param distance
	 *            the distance to set
	 */
	protected void setDistance(float distance) {
		this.distance = distance;
	}

}
