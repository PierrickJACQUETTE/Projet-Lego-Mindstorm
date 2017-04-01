package project;

public abstract class DistanceObject<T> {

	private float distance;

	/**
	 * Constructeur permettant de stocker une distance d'un Objet en fonction
	 * d'un autre Objet
	 * 
	 * @param distance
	 *            la distance entre la couleur connue et la couleur courante
	 */
	public DistanceObject(float distance) {
		this.distance = distance;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

}
