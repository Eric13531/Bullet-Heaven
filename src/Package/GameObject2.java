package Package;
/**
 *Name: Riddhiman Paul, Eric Yang, Eric Zhang
 *Date: Jan 25, 2022
 *Modified GameObject Class for Bullet Heaven Game
 */

/* 
 * Works Cited:
 * https://docs.oracle.com/javase/7/docs/api/
 * https://mathworld.wolfram.com/EightCurve.html
 * "A Clockwork Orange" game exemplar
 */	
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public abstract class GameObject2 extends JComponent {
	private Color c = Color.white;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected static int windowWidth = (int) screenSize.getWidth();
	protected static int windowHeight = (int) screenSize.getHeight();
	protected BufferedImage sprite;
	protected double spriteAngle;
	protected int width;
	protected int height;
	protected boolean backgroundElement;
	
	public GameObject2(int w, int h, BufferedImage i) {
		width = w;
		height = h;
		sprite = i;
	}

	public void setSize(int w, int h) {
		super.setSize(w, h);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}
	
	public int getX() {
		return getLocation().x;
	}

	public int getY() {
		return getLocation().y;
	}
	
	public int getCX() {
		return getLocation().x + width / 2;
	}

	public int getCY() {
		return getLocation().y + height / 2;
	}

	public void setX(int x) {
		super.setLocation(x, getLocation().y);
	}
	
	public void setX(double x) {
		super.setLocation((int)(x + 0.5), getLocation().y);
	}

	public void setY(int y) {
		super.setLocation(getLocation().x, y);
	}
	
	public void setY(double y) {
		super.setLocation(getLocation().x, (int)(y + 0.5));
	}

	public double getAngle(int x1, int y1, int x2, int y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}
	
	public double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public void pushIn(int xBound, int yBound) {
		if (getLocation().x < 0) {
			super.setLocation(0, getLocation().y);
		}
		if (getLocation().x > xBound - width) {
			super.setLocation(xBound - width, getLocation().y);
		}
		if (getLocation().y < 0) {
			super.setLocation(getLocation().x, 0);
		}
		if (getLocation().y > yBound - height) {
			super.setLocation(getLocation().x, yBound - height);
		}
	}
	
	public boolean outOfBounds() {
		if (getLocation().x < 0 ||
			getLocation().x + width > windowWidth ||
			getLocation().y < 0 ||
			getLocation().y + height > windowHeight) {
			return true;
		}
		return false;
	}
	
	public boolean outOfScreen() {
		if (getLocation().x + width < 0 ||
			getLocation().x > windowWidth ||
			getLocation().y + height < 0 ||
			getLocation().y > windowHeight) {
			return true;
		}
		return false;
	}

	public void setColor(Color c) {
		this.c = c;
	}
	
	public BufferedImage setSprite(String fileName) {
		Image i = null;
		try { i = ImageIO.read(GameObject2.class.getResourceAsStream("img/" + fileName));
        } catch (IOException e) { }
    	ImageFilter filter = new RGBImageFilter() {
    		public final int filterRGB(int x, int y, int rgb) {
    			if (rgb == Color.BLACK.getRGB()) {
    				return 0x00FFFFFF & rgb;
    			}
    			return rgb;
    		}
    	};
		i = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(i.getSource(), filter));
		BufferedImage bufferedImage = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2d = bufferedImage.createGraphics();
    	g2d.drawImage(i, 0, 0, null);
    	g2d.dispose();
    	return bufferedImage;
	}

	public void paintComponent(Graphics g) {
		try {
			AffineTransform at = new AffineTransform();
			if (spriteAngle != 0) {
				at.rotate(spriteAngle, width / 2, height / 2);
			}
			at.scale(width * 1.0 / sprite.getWidth(), height * 1.0 / sprite.getHeight());
			Graphics2D g2d = (Graphics2D) g;
	        g2d.drawImage(sprite, at, null);
		}
		catch (Exception e) {
	        g.drawImage(sprite, 0, 0, width, height, this);
		}
    }

	public boolean collides(GameObject2 o) {
		return getBounds().intersects(o.getBounds());
	}

	public abstract void act();
}
