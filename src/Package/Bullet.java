package Package;
/**
 *Name: Riddhiman Paul, Eric Yang, Eric Zhang
 *Date: Jan 25, 2022
 *Bullet Class for Bullet Heaven Game
 */

/* 
 * Works Cited:
 * https://docs.oracle.com/javase/7/docs/api/
 * https://mathworld.wolfram.com/EightCurve.html
 * "A Clockwork Orange" game exemplar
 */
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject2 {
	//Speed of bullet and time that bullet was active
	private int velocityY = -4, timer;
	//bullet sprite
	private BufferedImage bullet = setSprite("blueser.png");
	
	
	public Bullet(int x, int y) {
		//set size, coordinates, sprite, timer
		super(10, 20, null);
		sprite = bullet;
		setSize(10, 20);
		setY(y); setX(x);
		timer=0;
	}
	
	@Override
	public void act(){
		//Move bullet forward
		setY(getY()+velocityY);
		timer++;
		
	}
	/**
	 * Called when bullet hits 
	 */
	/**
	 * returns the time that the bullet is on screen
	 */
	public int getTimer() {
		return timer;
	}
}
