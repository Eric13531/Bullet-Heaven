package Package;
/**
 *Name: Riddhiman Paul, Eric Yang, Eric Zhang
 *Date: Jan 25, 2022
 *SpaceShip Class for Bullet Heaven Game
 */

/* 
 * Works Cited:
 * https://docs.oracle.com/javase/7/docs/api/
 * https://mathworld.wolfram.com/EightCurve.html
 * "A Clockwork Orange" game exemplar
 */
import java.awt.Color;
import java.awt.image.BufferedImage;

public class SpaceShip extends GameObject2 {	
	private int HP; //stores the hp of the SpaceShip
	
	//image sprites
	private BufferedImage undamaged = setSprite("spaceship.png");
	private BufferedImage damaged = setSprite("spdmgggg.png");
	private BufferedImage shield = setSprite("shield_spaceship.png");
	private BufferedImage cooldown = setSprite("cd.png");
	
	/**set hp, position, size, sprite*/
	public SpaceShip(int hp, Color n, int xPos, int yPos) {
		super(30, 30, null);
		HP = hp;
		setSize(30, 30);
		sprite = undamaged;
		setColor(n);
		setX(xPos);
		setY(yPos);
		
	}
	
	@Override
	public void act(){
	
	}
	
	/** sets the character to the damaged sprite*/
	public void damage(){
		sprite = damaged;
	}
	/** sets the character to the undamaged sprite*/
	public void undamage(){
		sprite = undamaged;
	}
	/** sets the character to the shield sprite*/
	public void shield(){
		sprite = shield;
	}
	/** sets the character to the shield cooldown sprite*/
	public void cooldown(){
		sprite = cooldown;
	}
	
	/**moves the paddle up*/
	public void moveLeft() {
		setX(getX()-2);
	}
	
	/**moves the paddle down*/
	public void moveRight() {
		setX(getX()+2);
	}

	/**moves the paddle up*/
	public void moveUp() {
		setY(getY()-2);
	
	}

	/**moves the paddle up*/
	public void moveDown() {
		setY(getY()+2);
	
	}
	/**returns the player's current hp*/
	public int getHP() {
		return HP;
	}
	/**sets the player's current hp*/
	public void setHP(int n) {
		HP=n;
	}
}
