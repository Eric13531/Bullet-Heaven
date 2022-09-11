package Package;
/**
 *Name: Riddhiman Paul, Eric Yang, Eric Zhang
 *Date: Jan 25, 2022
 *Enemy Class for Bullet Heaven Game
 */

/* 
 * Works Cited:
 * https://docs.oracle.com/javase/7/docs/api/
 * https://mathworld.wolfram.com/EightCurve.html
 * "A Clockwork Orange" game exemplar
 */

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.lang.Math.*;

public class Enemy extends GameObject2 {
	// sprites for enemy
	private BufferedImage undamaged = setSprite("u2.png");
	private BufferedImage damaged = setSprite("u1.png");
	// hp
	private int HP;

	private int tick = 0; // determines angle, gets incremented
	private double angle = 0; // angle from center
	private double dist = 100; // distance from center

	/**
	 * set hp, position, size, sprite
	 */
	public Enemy(int hp, int xPos, int yPos) {
		super(60, 54, null);
		HP = hp;
		sprite = undamaged;
		setSize(60, 48);
		setX(xPos);
		setY(yPos);

	}

	/**
	 * Change the sprite when enemy gets hit
	 */
	public void damage() {
		sprite = damaged;
	}

	/**
	 * Revert sprite to normal after time passes
	 */
	public void undamage() {
		sprite = undamaged;
	}

	@Override
	public void act() {
		// reset tick after completing a period
		if (tick == 480) {
			tick = 0;
		}

		// calculate angle from tick
		angle = (tick) * 2 * Math.PI / 480;

		// get position with angle and distance
		setX(280 + (int) (2 * dist * Math.sin(angle)));
		setY(100 + (int) (dist * Math.sin(angle) * Math.cos(angle)));
		// increment tick
		tick++;

	}

	/**
	 * returns enemy HP
	 */
	public int getHP() {
		return HP;

	}

	/**
	 * set enemy HP
	 */
	public void setHP(int n) {

		HP = n;

	}

}
