package game;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import engine.Drawable;
import engine.Image;
import engine.Sprite;

class Bullet extends Sprite {
	boolean active = false;
	
	public Bullet(Image src) {
		super(src);
	}
}

public class BulletSystem implements Drawable {

	public double BULLET_SPEED = -850;		// negative is up, positive is down
	
	private Image image;
	private ArrayList<Bullet> bullets = new ArrayList<>();
	
	public BulletSystem(String image) {
		Image img = this.image = new Image(image);
		
		for(int i = 0; i < 32; ++i) {
			bullets.add(new Bullet(img));
		}
	}
	
	/**
	 * Mark all bullets as inactive
	 */
	public void reset() {
		for(int i = 0; i < bullets.size(); ++i) {
			bullets.get(i).active = false;
		}
	}
	
	/**
	 * Spawn a projectile at specified position
	 * 
	 * @param x
	 * @param y
	 */
	public void shoot(double x, double y) {
		// Try to find an inactive bullet
		Bullet bullet = null;
		for(int i = 0, l = bullets.size(); i < l; ++i) {
			Bullet b = bullets.get(i);
			if(!b.active) {
				bullet = b;
				break;
			}
		}
		
		// 'bullet' is still null; we didn't find any inactive bullets
		// Therefore, we'll make a new one.
		if(bullet == null) {
			bullet = new Bullet(image);
			bullets.add(bullet);
		}
		
		// Now that we have a bullet, move it to the x/y position specified
		// and mark it as active
		bullet.setPosition(x, y);
		bullet.active = true;
		
		// Bullet is active and away!
	}
	
	/**
	 * Move all bullets by amount indicated by BULLET_SPEED.
	 * 
	 * @param delta time between frames in seconds
	 */
	public void update(double delta) {	
		double dy = BULLET_SPEED * delta;
	
		// Move all bullets, doesn't matter if they're active or not
		for(int i = 0, l = bullets.size(); i < l; ++i) {
			Bullet b = bullets.get(i);
			b.move(0, dy);
			
			// If bullet is well outside the screen, mark it as inactive
			double y = b.getY();
			if(y < -100 || y > 1000) {
				b.active = false;
			}
		}
	}

	/**
	 * See if some bullet could have hit at position x, y inside a given radius.
	 * If a hit is found, mark bullet as inactive and return true.
	 * 
	 * @param x X coordinate to check against
	 * @param y Y coordinate to check against
	 * @param radius radius inside which to consider a hit
	 * @return true if hit. also destroys bullet on hit.
	 */
	public boolean testHit(double x, double y, double radius) {
		for(int i = 0, l = bullets.size(); i < l; ++i) {
			Bullet b = bullets.get(i);
			if(!b.active) continue;
			
			double bx = b.getX(), by = b.getY();
			
			// Circle check
			double dx = bx - x;
			double dy = by - y;
			
			// For an explanation of this, check out the pythagorean theorem
			if(dx * dx + dy * dy < radius * radius) {
				// We have a hit!
				b.active = false;
				return true;
			}
		}
		
		// If we're here, no bullets hit.
		return false;
	}
	
	@Override
	public void draw(Graphics2D g, ImageObserver obs) {

		// We'll draw all bullets inside this custom draw routine,
		// but only if they're marked as inactive
		
		for(int i = 0, l = bullets.size(); i < l; ++i) {
			
			Bullet b = bullets.get(i);
			if(b.active) {
				b.draw(g, obs);
			}
			
		}
		
	}
	
}
