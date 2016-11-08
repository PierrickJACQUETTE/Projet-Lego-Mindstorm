package project;

public class Color {

	private float red;
	private float green;
	private float blue;
	private int name;

	public Color(float red, float green, float blue, int name) {
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

	protected int getName() {
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

	protected void setName(int name) {
		this.name = name;
	}

	public String toString() {
		return this.red + " " + this.green + " " + this.blue + " " + this.name + "\r";
	}

	protected float euclide(Color c) {
		float dRed = c.getRed() - this.getRed();
		float dGreen = c.getGreen() - this.getGreen();
		float dBlue = c.getBlue() - this.getBlue();
		return (float) Math.sqrt((dRed * dRed) + (dGreen * dGreen) + (dBlue * dBlue));
	}
}
