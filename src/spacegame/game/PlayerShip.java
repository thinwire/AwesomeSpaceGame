package spacegame.game;

import engine.audio.Sound;
import engine.core.Input;
import engine.graphics.Image;
import engine.graphics.Sprite;
import engine.math.EMath;

public class PlayerShip extends Sprite {

	private double speed_x = 0;
	private double speed_y = 0;

	private BulletSystem bullets;
	
	private Sound laserSound;

	public PlayerShip(BulletSystem b) {
		super(new Image("gfx/ship.png"));
		laserSound = new Sound("sfx/laser1.wav");
		
		bullets = b;
		reset();
	}

	public void reset() {
		// Player ship should always start at the center bottom half
		// of the screen
		setPosition(400, 600);
		speed_x = 0;
		speed_y = 0;
	}

	public void update(Input input, double delta) {

		// Acceleration factor
		final double ACCEL = 1650.0;

		// Maximum speed to move player (pixels per second)
		final double SPEED_MAX = 520.0;

		// Update controls
		double dx = 0;
		double dy = 0;

		if (input.isDown("LEFT")) {
			dx -= ACCEL;
		}

		if (input.isDown("RIGHT")) {
			dx += ACCEL;
		}

		if (input.isDown("UP")) {
			dy -= ACCEL;
		}

		if (input.isDown("DOWN")) {
			dy += ACCEL;
		}

		// Modify speed - make sure that the speed of the ship stays within sane limits
		if (dx == 0.0) {
			speed_x = EMath.reduce(speed_x, ACCEL * delta);
		} else {
			speed_x = EMath.clamp(speed_x + dx * delta, -SPEED_MAX, SPEED_MAX);
		}

		if (dy == 0.0) {
			speed_y = EMath.reduce(speed_y, ACCEL * delta);
		} else {
			speed_y = EMath.clamp(speed_y + dy * delta, -SPEED_MAX, SPEED_MAX);
		}

		// Move the ship sprite
		move(speed_x * delta, speed_y * delta);

		// Make sure we didn't go outside the screen just now
		double x = getX();
		double y = getY();

		x = EMath.clamp(x, 35, 800 - 35);
		y = EMath.clamp(y, 35, 800 - 35);

		// If X or Y changed, it means we've stopped at a screen edge. If so, kill our
		// speed.
		if (x != getX())
			speed_x = 0;
		if (y != getY())
			speed_y = 0;

		// Set our position to possibly new x and y values
		setPosition(x, y);

		// Now that we've moved, see if we want to shoot or not
		if (input.isPressed("FIRE")) {
			bullets.shoot(x, y);
			
			laserSound.play();
		}

	}

}