import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

class HelloWorld{
	public static void main(String [] args){
		LCD.drawString("Hello World !", 1, 4);
		Delay.msDelay(5000);
	}
}