package Package;
/**
 *Name: Eric Zhang
 *Date: July 5, 2022
 *Background Class for Bullet Heaven Game
 */

import java.awt.image.BufferedImage;

public class Image extends GameObject2{
    // Sprite used for background
    private BufferedImage bg = setSprite("space.png");

    public Image(int w, int h, int x, int y, String str){
        // Set width and height equal to window size]
        super(w, h, null);
        // Set sprite
        sprite = setSprite(str + ".png");
        //Set position to cover whole screen
        setX(x);
        setY(y);
    }

    

    @Override
	public void act(){
	
	}
}
