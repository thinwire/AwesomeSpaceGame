package engine;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public interface Drawable {

	public void draw(Graphics2D g, ImageObserver obs);
	
}