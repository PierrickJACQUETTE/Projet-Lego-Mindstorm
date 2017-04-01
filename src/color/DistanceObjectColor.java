package color;

import project.DistanceObject;

public class DistanceObjectColor extends DistanceObject<Couleur> {

	private Couleur couleur;

	public DistanceObjectColor(Couleur couleur, float distance) {
		super(distance);
		this.couleur = couleur;
	}

	public Couleur getCouleur() {
		return couleur;
	}
}
