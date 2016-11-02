package project;

public class Color {

	private float red;
	private float green;
	private float blue;
	private final String name;

	public Color(float red, float green, float blue, String name) {
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

	protected String getName() {
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

	public String toString() {
		return this.red + " " + this.green + " " + this.blue + " " + this.name + "\r";
	}
}
