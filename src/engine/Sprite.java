package engine;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Sprite implements Drawable {

	private BufferedImage source;
	private double size_w, size_h;
	private double pos_x, pos_y;

	public Sprite(Image src) {
		source = src.getData();
		size_w = src.getWidth();
		size_h = src.getHeight();
		pos_x = 0;
		pos_y = 0;
	}

	public void setPosition(double x, double y) {
		pos_x = x;
		pos_y = y;
	}

	public void move(double dx, double dy) {
		pos_x += dx;
		pos_y += dy;
	}

	public double getX() {
		return pos_x;
	}

	public double getY() {
		return pos_y;
	}

	public double getWidth() {
		return size_w;
	}

	public double getHeight() {
		return size_h;
	}

	public void draw(Graphics2D g, ImageObserver obs) {

		// Figure out screen coordinates for where to draw the sprite.
		// We want to center the sprite on its position, so we need to
		// subtract half the width and the height of the sprite from
		// the coordinate - otherwise the sprite would show up with its
		// top left corner at the indicated position, and that just looks wrong. :)
		int x = (int) ((pos_x - size_w * 0.5) + 0.5);
		int y = (int) ((pos_y - size_h * 0.5) + 0.5);

		g.drawImage(source, x, y, obs);
	}

}
