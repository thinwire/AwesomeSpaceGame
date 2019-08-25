package game;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import engine.Drawable;
import engine.EMath;
import engine.Image;
import engine.Sprite;

class Enemy extends Sprite {

	private BulletSystem bullets;

	boolean alive = false;

	double timeUntilNextShot = 99;
	double speed_x = 0;
	double speed_y = 100;

	public Enemy(Image src, BulletSystem myBullets) {
		super(src);
		bullets = myBullets;
	}

	public void reset() {
		alive = false;
		timeUntilNextShot = EMath.rand(1.15, 1.95);
		speed_x = EMath.rand(-75, 75);
		speed_y = EMath.rand(170, 225);
	}

	public void update(double delta) {

		// See if we should shoot
		timeUntilNextShot -= delta;
		if (timeUntilNextShot < 0) {
			timeUntilNextShot = EMath.rand(0.95, 1.25);
			bullets.shoot(getX(), getY());
		}

		// Move enemy
		move(speed_x * delta, speed_y * delta);

		// If the enemy is bouncing on the walls,
		// reverse X direction
		if (getX() < 45 || getX() > 800 - 45) {
			speed_x = -speed_x;
		}

		// If enemy has left the screen on the bottom,
		// mark it as dead
		if (getY() > 900) {
			alive = false;
		}

	}

}

public class EnemySystem implements Drawable {

	private Image image;

	private ArrayList<Enemy> enemies = new ArrayList<>();
	private BulletSystem enemyBullets = new BulletSystem("gfx/enemylaser.png");
	private BulletSystem playerBullets;
	private ExplosionSystem explosions;

	private double timeSinceLastSpawn = 0;
	private double spawnInterval = 3.5;

	private int enemiesKilled = 0;

	public EnemySystem(String sprite, BulletSystem playerBullets, ExplosionSystem exp) {
		image = new Image(sprite);

		for (int i = 0; i < 16; ++i) {
			enemies.add(new Enemy(image, enemyBullets));
		}

		explosions = exp;
		this.playerBullets = playerBullets;
		enemyBullets.BULLET_SPEED = 520;
	}

	/**
	 * Get the number of killed enemies
	 */
	public int getKillCount() {
		return enemiesKilled;
	}

	/**
	 * Reset the state for a new game
	 */
	public void reset() {
		for (int i = 0, l = enemies.size(); i < l; ++i) {
			enemies.get(i).reset();
		}
		enemiesKilled = 0;
		spawnInterval = 3.5;
	}

	/**
	 * Spawn an enemy. Usually called internatlly but can be called externally, too,
	 * for some extra fun.
	 */
	public void spawn() {

		// Try to find a dead enemy
		Enemy enemy = null;
		for (int i = 0, l = enemies.size(); i < l; ++i) {
			Enemy e = enemies.get(i);
			if (!e.alive) {
				enemy = e;
				break;
			}
		}

		// 'enemy' is still null; we didn't find a dead enemy
		// Therefore, we'll make a new one.
		if (enemy == null) {
			enemy = new Enemy(image, enemyBullets);
			enemies.add(enemy);
		}

		// Now that we have a bullet, move it to the x/y position specified
		// and mark it as active
		double x = EMath.rand(45, 800 - 45);
		double y = EMath.rand(-35, -85);

		enemy.reset();
		enemy.setPosition(x, y);
		enemy.alive = true;
	}

	public void update(double delta) {
		timeSinceLastSpawn += delta;
		if (timeSinceLastSpawn > spawnInterval) {
			// It's time we spawn another enemy!
			spawn();

			// Set time since last spawn to 0
			timeSinceLastSpawn = 0;

			// Tweak the spawn interval a bit
			spawnInterval = EMath.rand(1.19, 1.99); // in seconds
		}

		// Update all enemies
		for (int i = 0, l = enemies.size(); i < l; ++i) {
			Enemy e = enemies.get(i);

			// Don't bother updating dead enemies...
			if (!e.alive)
				continue;

			// It's alive, we're good
			e.update(delta);

			// Check if player bullets happened to hit an enemy
			if (playerBullets.testHit(e.getX(), e.getY(), 32)) {

				// Create an explosion
				explosions.spawn(e.getX(), e.getY(), 85, e.speed_x * 0.4, e.speed_y * 0.4);

				// Kill enemy
				e.alive = false;

				// Mark another kill
				enemiesKilled++;
			}
		}

		// Update enemy bullets
		enemyBullets.update(delta);
	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs) {
		enemyBullets.draw(g, obs);

		for (int i = 0, l = enemies.size(); i < l; ++i) {
			Enemy e = enemies.get(i);
			if (e.alive) {
				e.draw(g, obs);
			}
		}
	}
}
