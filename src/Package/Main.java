package Package;
/**
 *Name: Riddhiman Paul, Eric Yang, Eric Zhang
 *Date: Jan 25, 2022
 *Main Class for Bullet Heaven Game
 */

/* 
 * Works Cited:
 * https://docs.oracle.com/javase/7/docs/api/
 * https://mathworld.wolfram.com/EightCurve.html
 * "A Clockwork Orange" game exemplar
 */
import java.lang.Object;
import java.io.File;
import java.io.IOException;
import java.awt.GraphicsEnvironment;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.util.*;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

public class Main extends Game2 {
	// game conditions
	private boolean gameStart = false;
	private String gameWin;

	// current score and high score
	private int highScore = 0;
	private JLabel score = new JLabel();
	private int curScore = 0;
	private JLabel cscore = new JLabel();

	// menu display
	private boolean displayingMenu = false;
	private JLabel menu = new JLabel();
	private JLabel menu2 = new JLabel();

	// background
	private Image bg;
	private boolean bgDisplay;

	// heart image
	private Image heart;

	// player HP
	private SpaceShip ship; // player object
	private JLabel shipHP = new JLabel();
	private static int prevHP = 20; // carries health over between stages

	// player damage and shield
	private int invCoolDownTick; // if invCoolDownTick < invLimit, player is
									// invincible
	private static final int INV_COOLDOWN = 1000; // length of shield cooldown
	private int invTick = 300; // if invTick < invLimit, player is invincible
	private static final int INV_LIMIT = 300;// the max time you can be
												// invincible
	private int playerDamageDisplayTick = 0; //
	private static final int DMG_COOLDOWN = 6;

	// player bullets
	private ArrayList<Bullet> playerBullets = new ArrayList<Bullet>(); // contains
																		// all
																		// player
																		// bullets
																		// in
																		// play
	private int playerBulletTimeCounter = 0;
	private static final int PLAYER_BULLET_COOLDOWN = 20;

	// enemy HP
	private Enemy enemy; // enemy object
	//private JLabel enemyHP = new JLabel();
	private int enemyDamageDisplayTick = 0;
	private static final int ENEMY_DMG_COOLDOWN = 6;

	// player bullets
	private ArrayList<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>(); // contains
																				// all
																				// enemy
																				// bullets
																				// in
																				// play
	private int enemyBulletTimeCounter = 0;
	private static final int ENEMY_BULLET_COOLDOWN_S1 = 20;
	private static final int ENEMY_BULLET_COOLDOWN_S2 = 85;

	// Stage (attack pattern)
	private int stage = 1;

	// JFrame
	private static JFrame f = new JFrame();

	// Stall for stage 2
	private int attackPatternStall = 0;
	private static final int ATK_PATTERN_STALL_COOLDOWN = 2;

	// temporary Ball objects for stage 2
	private EnemyBullet tempBombBullet1 = new EnemyBullet();
	private EnemyBullet tempBombBullet2 = new EnemyBullet();
	private EnemyBullet tempBombBullet3 = new EnemyBullet();

	private static Font PIXEL;
	private static Font NAS;

	private JPanel HPPanel = new JPanel();
	private JProgressBar HPBar = new JProgressBar();

	//debug progress bar
	private int tempTick  = 0;
   

	/**
	 * Initalizes all the EnemyBullet objects of each burst attack for the 3rd stage
	 */
	private void circle(int size, double x, double y, double vx, double vy, double distance, double period,
			boolean direction, Color color, String str) {
		// Initalize each stage 3 bullet individually
		EnemyBullet temp = new EnemyBullet(x, y, distance, 0 * period / 8, direction, size, size, str);
		EnemyBullet temp2 = new EnemyBullet(x, y, distance, 1 * period / 8, direction, size, size, str);
		EnemyBullet temp3 = new EnemyBullet(x, y, distance, 2 * period / 8, direction, size, size, str);
		EnemyBullet temp4 = new EnemyBullet(x, y, distance, 3 * period / 8, direction, size, size, str);
		EnemyBullet temp5 = new EnemyBullet(x, y, distance, 4 * period / 8, direction, size, size, str);
		EnemyBullet temp6 = new EnemyBullet(x, y, distance, 5 * period / 8, direction, size, size, str);
		EnemyBullet temp7 = new EnemyBullet(x, y, distance, 6 * period / 8, direction, size, size, str);
		EnemyBullet temp8 = new EnemyBullet(x, y, distance, 7 * period / 8, direction, size, size, str);
		// Add all bullets to the screen
		add(temp);
		add(temp2);
		add(temp3);
		add(temp4);
		add(temp5);
		add(temp6);
		add(temp7);
		add(temp8);
		// Append bullets to enemyBullets ArrayList
		enemyBullets.add(temp);
		enemyBullets.add(temp2);
		enemyBullets.add(temp3);
		enemyBullets.add(temp4);
		enemyBullets.add(temp5);
		enemyBullets.add(temp6);
		enemyBullets.add(temp7);
		enemyBullets.add(temp8);
	}

	/**
	 * Removes all the bullets from the screens and clears playerBullets and
	 * enemyBullets
	 */
	private void removeBullets() {
		// Loop through playerBullets
		for (int i = playerBullets.size() - 1; i >= 0; i--) {
			// Remove bullet from arrayList and screen
			remove(playerBullets.get(i));
			playerBullets.remove(i);
		}
		// Loop through enemyBullets
		for (int i = enemyBullets.size() - 1; i >= 0; i--) {
			// Remove bullet from arrayList and screen
			remove(enemyBullets.get(i));
			enemyBullets.remove(i);
		}
		// repaint the screen with the bullets removed
		repaint();
	}

	/**
	 * Displays win and lose screens
	 */
	private void displayMenu() {
		removeBullets();
		displayingMenu = true;
		menu.setBounds(getFieldWidth() / 2 - 150, getFieldHeight() / 2 - 200, 300, 200);// setting size
		menu.setHorizontalAlignment(SwingConstants.CENTER);
		if (gameWin == "Win") menu.setForeground(Color.BLUE);// sets text color to blue for win screen
		else menu.setForeground(Color.RED);// sets text color to red for lose screen
		menu.setText("You " + gameWin);// setting the text
		menu.setFont(new Font("Pixeboy", Font.PLAIN, 80));// setting font and font size
		add(menu);
		menu2.setBounds(getFieldWidth()/2-300, getFieldHeight() / 2 - 100, 600, 200);// setting size
		if (gameWin == "Win") menu2.setForeground(Color.BLUE);// sets text color to blue for win screen
		else menu2.setForeground(Color.RED);// sets text color to red for lose screen
		menu2.setHorizontalAlignment(SwingConstants.CENTER);
		menu2.setText("Press ENTER to continue");// setting the text
		menu2.setFont(new Font("Pixeboy", Font.PLAIN, 30));// setting font and font size
		add(menu2);
		repaint();
	}

	@Override
	public void act() {
		repaint();
		
		if(bgDisplay){
			remove(bg);
			remove(heart);
			remove(HPPanel);
			bgDisplay=false;
		} 

		HPBar.setValue(enemy.getHP());
		HPBar.setString("" + enemy.getHP());
		// if a game is not active, displays the menu and waits
		// for the player to press enter to proceed to the next round
		if (gameStart == false) {
			// display the menu if it is not already displayed
			if (displayingMenu == false) {
				displayMenu();
			}
			// if enter key is pressed, proceeds to next round.
			if (EnterKeyPressed()) {
				menu.setText("");
				menu2.setText("");
				displayingMenu = false;
				setup();
			}

		}
		if (gameStart == true) {
			//if enemy is killed, displays the win screen and proceeds to the next round
			if (enemy.getHP() < 1) {
				gameStart = false;
				//removes all objects on the screen(Other than score and high score)
				remove(ship);
				remove(enemy);
				shipHP.setText("");
				//enemyHP.setText("");
				removeBullets();
				gameWin = "Win";
				stage++; //proceeds to the next stage
				if (stage > 3) stage = 1; //resets the stage after stage 3
				curScore++; //increases current score
				attackPatternStall = 0;
				if (curScore > highScore)highScore = curScore;//updates the high score
															  //if current score is 
															  //higher than high score
				prevHP = ship.getHP();//carries the player health over to the next round
			}
			//if player is killed, displays the lose screen and resets the rounds
			if (ship.getHP() < 1) {
				gameStart = false;
				//removes all objects on the screen(Other than score and high score)
				remove(ship);
				remove(enemy);
				shipHP.setText("");
				//enemyHP.setText("");
				removeBullets();
				gameWin = "Lose";
				stage = 1;//resets stage
				curScore = 0;//resets current score
				prevHP = 20;//set hp back to 20(default max hp)
			}
			//sets the enemy sprite back to the normal sprite after 6 milliseconds 
			if (enemyDamageDisplayTick == ENEMY_DMG_COOLDOWN) {
				enemy.undamage();
			}
			//sets the player sprite back to the normal sprite after 6 milliseconds 
			if (playerDamageDisplayTick > DMG_COOLDOWN) {
				//sets the player sprite to undamaged if shield is not active
				if (invTick > INV_LIMIT) {
					ship.undamage();
				}
				//sets the player sprite to the shield cooldown sprite
				if (invTick > INV_LIMIT && invCoolDownTick < INV_COOLDOWN) {
					ship.cooldown();
				}
			}

			// player shooting
			if (NKeyPressed() && playerBulletTimeCounter >= PLAYER_BULLET_COOLDOWN) {
				// Create two bullets in front of the player
				Bullet temp = new Bullet(ship.getX(), ship.getY());
				add(temp);
				playerBullets.add(temp);
				Bullet temp2 = new Bullet(ship.getX() + 20, ship.getY());
				add(temp2);
				playerBullets.add(temp2);
				// reset player bullet timer
				playerBulletTimeCounter = 0;
			}

			// enemy shooting
			// stage 1 -> stage 2 -> stage 3 -> stage 1
			if (stage == 1) {
				// stage 1 fires bullets left, right and down
				// shoot bullets left and right at regular intervals
				if (enemyBulletTimeCounter == ENEMY_BULLET_COOLDOWN_S1
						|| enemyBulletTimeCounter == 2 * ENEMY_BULLET_COOLDOWN_S1
						|| enemyBulletTimeCounter >= 3 * ENEMY_BULLET_COOLDOWN_S1) {
					EnemyBullet temp = new EnemyBullet(enemy.getX()+35, enemy.getY()+35, 1, 1, 10, 10, "bullet5");
					add(temp);
					enemyBullets.add(temp);

					EnemyBullet temp2 = new EnemyBullet(enemy.getX()+35, enemy.getY()+35, -1, 1, 10, 10, "bullet5");
					add(temp2);
					enemyBullets.add(temp2);

					// drop bullet at regular intervals
					if (enemyBulletTimeCounter >= 60) {
						EnemyBullet temp3 = new EnemyBullet(enemy.getX()+35, enemy.getY()+35, 0, 2, 30, 20, "wave");
						add(temp3);
						enemyBullets.add(temp3);
					}

					// reset enemy bullet timer
					if (enemyBulletTimeCounter >= 3 * ENEMY_BULLET_COOLDOWN_S1)
						enemyBulletTimeCounter = 0;

				}
			} else if (stage == 2) {
				// stage 2 fires bullets that burst into smaller bullets
				// Fire big bullets and create smaller bullets at regular intervals
				if (enemyBulletTimeCounter >= 3 * ENEMY_BULLET_COOLDOWN_S1) {
					// stall the bullet burst for the first two iterations
					if (attackPatternStall != ATK_PATTERN_STALL_COOLDOWN) {
						attackPatternStall++;
					} else {
						// Remove the bomb bullet created two iterations ago
						remove(tempBombBullet1);
						enemyBullets.remove(tempBombBullet1);
						EnemyBullet split1 = new EnemyBullet(tempBombBullet1.getX(), tempBombBullet1.getY(), 1, 1, 10,
								10, "cross");
						enemyBullets.add(split1);
						EnemyBullet split2 = new EnemyBullet(tempBombBullet1.getX(), tempBombBullet1.getY(), 1, -1, 10,
								10, "cross");
						enemyBullets.add(split2);
						EnemyBullet split3 = new EnemyBullet(tempBombBullet1.getX(), tempBombBullet1.getY(), -1, -1, 10,
								10, "cross");
						enemyBullets.add(split3);
						EnemyBullet split4 = new EnemyBullet(tempBombBullet1.getX(), tempBombBullet1.getY(), -1, 1, 10,
								10, "cross");
						enemyBullets.add(split4);

						add(split1);
						add(split2);
						add(split3);
						add(split4);

					}

					// Create new bomb bullet
					tempBombBullet3 = new EnemyBullet(enemy.getX()+35, enemy.getY()+35, 0, 2, 30, 30, "rocket");
					add(tempBombBullet3);
					enemyBullets.add(tempBombBullet3);

					// Move bomb bullet down queue
					// when it reaches tempBombBullet1, it gets removed
					tempBombBullet1 = tempBombBullet2;
					tempBombBullet2 = tempBombBullet3;

					// reset enemy bullet timer
					if (enemyBulletTimeCounter >= 3 * ENEMY_BULLET_COOLDOWN_S1)
						enemyBulletTimeCounter = 0;

				}
			} else if (stage == 3) {
				// stage 3 fires bullets in a circular pattern
				// Fire big bullets and create smaller bullets at regular intervals
				if (enemyBulletTimeCounter == ENEMY_BULLET_COOLDOWN_S2) {
					// Call circle to create eight bullets at once
					circle(30, enemy.getX()+35, enemy.getY()+35, 0, 0, 300, tempBombBullet3.getPeriod(), true, Color.PINK,
							"spin");

				}
				if (enemyBulletTimeCounter == 2 * ENEMY_BULLET_COOLDOWN_S2) {
					// Call circle to create eight bullets at once
					circle(30, enemy.getX()+35, enemy.getY()+35, 0, 0, 300, tempBombBullet3.getPeriod(), false, Color.PINK,
							"spin");
				}

				// reset enemy bullet timer
				if (enemyBulletTimeCounter == 2 * ENEMY_BULLET_COOLDOWN_S2) {
					enemyBulletTimeCounter = 0;
				}

			}

			// If M is pressed and shield is available, enable shield
			// and reset shield timer and cooldown
			if (MKeyPressed() && invCoolDownTick > INV_COOLDOWN) {
				ship.shield();
				invTick = 0;
				invCoolDownTick = 0;
			}
			// player movements
			// call player movement methdos when key pressed
			if (AKeyPressed() && ship.getX() >= 0)
				ship.moveLeft();

			if (DKeyPressed() && ship.getX() <= getFieldWidth() - 30)
				ship.moveRight();

			if (WKeyPressed() && ship.getY() >= 0)
				ship.moveUp();

			if (SKeyPressed() && ship.getY() <= getFieldHeight() - 50)
				ship.moveDown();

			// enemy collision
			// check all player bullets
			for (int i = 0; i < playerBullets.size(); i++) {
				// if enemy and player bullet collide
				// remove bullet, decrease hp, change enemy sprite
				// and reset enemy damage display timer
				if (enemy.collides(playerBullets.get(i))) {
					remove(playerBullets.get(i));
					playerBullets.remove(i);
					i--;

					enemy.setHP(enemy.getHP() - 1);
					//enemyHP.setText("ENEMY HEALTH: " + enemy.getHP());
					enemy.damage();
					enemyDamageDisplayTick = 0;
				}
			}
			// remove player bullets after a certain time
			for (int i = 0; i < playerBullets.size(); i++) {
				if (playerBullets.get(i).getTimer() > 200) {
					remove(playerBullets.get(i));
					playerBullets.remove(i);
					i--;
				}

			}

			// player collision
			// check all enemy bullets
			for (int i = 0; i < enemyBullets.size(); i++) {
				// if player and enemy bullet collide
				// remove bullet, decrease hp, change player sprite
				// and reset player damage display timer
				if (ship.collides(enemyBullets.get(i))) {
					if (invTick > INV_LIMIT) {
						ship.setHP(ship.getHP() - 1);
						shipHP.setText("       X " + ship.getHP());
						ship.damage();
						playerDamageDisplayTick = 0;
						EnemyBullet temp = enemyBullets.get(i);
						remove(temp);
						enemyBullets.remove(i);
						i--;
					}

				}
			}

			// bounce and deflect
			for (int i = 0; i < enemyBullets.size(); i++) {
				if (stage != 3) {
					// if enemybullet hits side, reflect x velocity
					if (enemyBullets.get(i).getX() >= getFieldWidth() - 10 || enemyBullets.get(i).getX() <= 0)
						enemyBullets.get(i).deflect();
					// if enemybullet hits top, reflect y velocity
					if (enemyBullets.get(i).getY() <= 0)
						enemyBullets.get(i).bounce();
				}
				// Remove enemybullet when it exits the bottom of the screen
				if (enemyBullets.get(i).getY() >= getFieldHeight()) {
					remove(enemyBullets.get(i));
					enemyBullets.remove(i);

					i--;
				}

			}

			// removes images when not on game screen
			if(bgDisplay){
				remove(HPPanel);
				remove(bg);
				remove(heart);
				bgDisplay=false;
			} 
			// adds images
			if(!displayingMenu){
				add(HPPanel);
				add(bg);
				add(heart);
				bgDisplay=true;
			}

			// debug progress bar
			if(tempTick == 1){
				score.setText("High score. " + highScore);
			}
			if(tempTick == 2){
				score.setText("High score: " + highScore);
			}



			// increment all the timers and cooldowns
			playerDamageDisplayTick++;
			enemyDamageDisplayTick++;
			playerBulletTimeCounter++;
			enemyBulletTimeCounter++;
			invTick++;
			invCoolDownTick++;

			//tempTick only needed at the start
			if(tempTick<4) tempTick++;

		}
	}

	public void setup() {
		// set the delay
		setDelay(5);

		// construct and add Spaceship
		ship = new SpaceShip(prevHP, Color.lightGray, getFieldWidth() / 2 - 10, getFieldHeight() - 100);
		add(ship);

		// player health bar
		shipHP.setBounds(7, getFieldHeight()-108, 500, 50);
		shipHP.setForeground(Color.YELLOW);
		shipHP.setFont(PIXEL.deriveFont(Font.PLAIN, 40));
		shipHP.setText("       X " + ship.getHP());
		add(shipHP);

		// construct and add enemy
		enemy = new Enemy(40 + curScore * 10, getFieldWidth() / 2 - 20, 100);
		add(enemy);

		// enemy health bar
		//enemyHP.setBounds(15, 2, 500, 20);
		//enemyHP.setForeground(Color.YELLOW);
		//enemyHP.setFont(new Font("Impact", Font.PLAIN, 20));
		//enemyHP.setText("ENEMY HEALTH: " + enemy.getHP());
		//add(enemyHP);

		// high score text
		score.setBounds(getFieldWidth() - 188, 5, 500, 22);
		score.setForeground(Color.YELLOW);
		score.setFont(NAS.deriveFont(Font.PLAIN, 20));
		score.setText("High score: " + highScore);
		add(score);

		// current score text
		cscore.setBounds(43, 5, 500, 20);
		cscore.setForeground(Color.YELLOW);
		cscore.setFont(NAS.deriveFont(Font.PLAIN, 20));
		cscore.setText("Score: " + curScore);
		add(cscore);

		// add background image, set dimensions equal to window size
		bg = new Image(getFieldWidth(), getFieldHeight(), 0, 0, "space");
		add(bg);

		heart = new Image(30, 30, 16, getFieldHeight()-108, "spaceship");

		HPPanel.setBounds(75,30,getFieldWidth()-150,30);
		HPPanel.setBackground(Color.BLACK);
		HPPanel.setForeground(Color.BLACK);
		//add(HPPanel);

		HPBar.setMinimum(0);
		HPBar.setMaximum(enemy.getHP());
		HPBar.setPreferredSize(new Dimension(getFieldWidth()-150,30));
		HPBar.setForeground(Color.RED);
		HPBar.setBackground(Color.GRAY);
		HPBar.setValue(enemy.getHP());
		HPBar.setStringPainted(false);
		HPBar.setString("" + enemy.getHP());
		HPBar.setVisible(true);
		HPPanel.add(HPBar);
		add(HPPanel);



		// reset cooldown when starting new stage
		invCoolDownTick = INV_COOLDOWN;

		gameStart = true;

	}

	public static void main(String[] args) {
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {

			PIXEL = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/Package/font/PIXEL.ttf"));
			NAS = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/Package/font/Nas.otf"));
			ge.registerFont(PIXEL);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		// Controls
		JOptionPane.showMessageDialog(f, "Welcome to Bullet Heaven"
			    + "\nUse the keys WASD to move"
			    + "\nUse the \"n\" key to shoot"
			    + "\nUse the \"m\" key to shield"
			    + "\nShield takes time to recharge"
			    + "\nPress \"OK\" to start", "Controls", JOptionPane.INFORMATION_MESSAGE);
		// Create new game object
		Main p = new Main();
		p.setSize(600, 800);
		p.setVisible(true);
		p.setResizable(false);
		p.initComponents();

	}

}
