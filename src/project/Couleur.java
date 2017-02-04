package project;

public class Couleur {

	private float red;
	private float green;
	private float blue;
	private short name;

	public Couleur(float red, float green, float blue, short name) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.name = name;
	}

	protected float getRed() {
		return red;
	}

	protected float getGreen() {
		return green;
	}

	protected float getBlue() {
		return blue;
	}

	protected short getName() {
		return name;
	}

	protected void setRed(float red) {
		this.red = red;
	}

	protected void setGreen(float green) {
		this.green = green;
	}

	protected void setBlue(float blue) {
		this.blue = blue;
	}

	protected void setName(short name) {
		this.name = name;
	}

	public String toString() {
		return this.red + " " + this.green + " " + this.blue + " " + this.name + "\r";
	}

	protected float euclide(Couleur c) {
		float dRed = c.getRed() - this.getRed();
		float dGreen = c.getGreen() - this.getGreen();
		float dBlue = c.getBlue() - this.getBlue();
		return (float) Math.sqrt((dRed * dRed) + (dGreen * dGreen) + (dBlue * dBlue));
	}
}
