package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import engine.Drawable;
import engine.EMath;

/**
 * Animated parallax scrolling starfield.
 */
public class Starfield implements Drawable {

	private static final double YMIN = -25;
	private static final double YMAX = 825;

	private double[] stars;
	private Color color;

	private double speed = 225;

	public Starfield(int size) {

		// For each star we'll want to store the X, Y and Z coordinates, so we'll
		// allocate an array 3 times 'size' long
		stars = new double[size * 3];

		// Let's populate the stars array
		for (int i = 0; i < size * 3; i += 3) {
			// X coordinate
			stars[i + 0] = EMath.rand(0, 800);

			// Y coordinate
			stars[i + 1] = EMath.rand(YMIN, YMAX);

			// Z coordinate. This one determines how close to the screen
			// the star should be (and thus how fast it should move), as
			// well as how long its tail should be. We'll make that a value between
			// 1.5 and 0.2.
			stars[i + 2] = EMath.rand(0.25, 1.5);
		}

		// Set the color to use - we'll go with a brighter gray for starters
		color = Color.GRAY.brighter();

	}

	/**
	 * Set the speed of the starfield
	 * 
	 * @param speed
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * Return the speed of the starfield
	 * 
	 * @return
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * Update (i.e. animate) the starfield
	 * 
	 * @param delta time between frames
	 */
	public void update(double delta) {

		for (int i = 0; i < stars.length; i += 3) {
			// Read in for easier manipulation
			double y = stars[i + 1];
			double z = stars[i + 2];

			// Modify values
			y = EMath.wrap(y + speed * delta * z, YMIN, YMAX);

			// Write back
			stars[i + 1] = y;
		}

	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs) {

		// Set a stretch factor per star...
		double stretch = 4.2;

		// Set the color we'll use to draw the stars
		g.setColor(color);

		for (int i = 0; i < stars.length; i += 3) {
			// Read in for easier manipulation
			double x = stars[i + 0];
			double y = stars[i + 1];
			double z = stars[i + 2];

			// Draw a line whose length depends on Z
			int x0 = (int) (x + 0.5);
			int x1 = (int) (x + 0.5);
			int y0 = (int) (y - stretch * z + 0.5);
			int y1 = (int) (y + stretch * z + 0.5);

			// Draw a line per star
			g.drawLine(x0, y0, x1, y1);
		}

	}

}
