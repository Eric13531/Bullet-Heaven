package Package;
/**
 *Name: Riddhiman Paul, Eric Yang, Eric Zhang
 *Date: Jan 25, 2022
 *Enemy Bullet Class for Bullet Heaven Game
 */

/* 
 * Works Cited:
 * https://docs.oracle.com/javase/7/docs/api/
 * https://mathworld.wolfram.com/EightCurve.html
 * "A Clockwork Orange" game exemplar
 */
import java.lang.Math.*;

public class EnemyBullet extends GameObject2 {
	//position and velocity
	private double positionX, positionY;
	private double velocityX = 0, velocityY = 0;

	//circular pattern bullets
	private boolean direction; //clockwise or counterclockwise
	double tick; //determines the angle, gets incremented
	double cycle; //determines the distance, gets incremented

	
	private double distance, angle; //distance and angle for circular bullets
	static int two_pi = 600; //number of ticks for a full rotation

	/**
	 * throwaway constructor
	 */
	public EnemyBullet() {
		super(0, 0, null); 
		sprite = setSprite("red.png");
	}
	/**
	 * constructor for normal bullets
	 * str - type/colour of bullet
	 */
	public EnemyBullet(double positionX, double positionY, double velocityX, double velocityY, int sizeX, int sizeY, String str) {
		super(sizeX, sizeY, null);
		setSize(sizeX,sizeY);
		sprite = setSprite(str + ".png");
		setX((int) positionX);
		setY((int) positionY);
		this.positionX = positionX;
		this.positionY = positionY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}
	/**
	 * constructor for circular pattern bullets
	 * str - type/colour of bullet
	 */
	public EnemyBullet(double positionX, double positionY, double distance, double tick, boolean direction, int sizeX, int sizeY, String str) {
		super(sizeX, sizeY, null);
		setSize(sizeX,sizeY);
		sprite = setSprite(str + ".png");
		setX((int) positionX);
		setY((int) positionY);
		this.positionX = positionX;
		this.positionY = positionY;
		this.distance = distance;
		this.tick = tick;
		this.direction = direction;
	}

	@Override
	public void act() {
		//move bullet each turn
		positionX += velocityX;
		positionY += velocityY;

		//increment tick, cycle
		tick++;
		cycle++;
		
		//calculate angle
		angle = tick * 2 * Math.PI / two_pi;		
		if (direction)
			angle *= -1;
		//set coordinates equal to positionX and positionY
		//circular bullets circle around positionX, positionY
		setX((int) (positionX + cycle * 0.01 * distance * Math.sin(angle)));
		setY((int) (positionY + cycle * 0.01 * distance * Math.cos(angle)));

		//if tick makes a full rotation, reset its value
		if (tick >= two_pi) {
			tick = 0;
		}

	}

	/** changes the x direction when the ball hits a wall */
	public void deflect() {
		velocityX *= -1;
	}

	/** changes the y direction when the ball hits the ceiling */
	public void bounce() {
		velocityY *= -1;
	}

	/** 
	 * Returns the length of a period in ticks
	 */
	public int getPeriod() {
		return two_pi;
	}

}

