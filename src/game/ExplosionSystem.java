package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import engine.Drawable;

class Explosion implements Drawable {

	private static final double DURATION = 0.9; // seconds

	double x, y, dx, dy;

	boolean active = false;
	double radius = 75;
	double time = 0;

	public void update(double delta) {
		time += delta;
		if (time > DURATION) {
			active = false;
		}
		x += dx * delta;
		y += dy * delta;
	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs) {
		double r0 = time / DURATION;
		double r = Math.sin(r0 * Math.PI);
		r = r * r;

		int w = (int) (r * radius + 0.5);
		g.setColor(Color.WHITE);
		g.fillArc((int) (x + 0.5) - (w >> 1), (int) (y + 0.5) - (w >> 1), w, w, 0, 360);
	}
}

public class ExplosionSystem implements Drawable {

	private ArrayList<Explosion> explosions = new ArrayList<>();

	public ExplosionSystem() {
		for (int i = 0; i < 16; ++i) {
			explosions.add(new Explosion());
		}
	}

	public void update(double delta) {
		for (int i = 0, l = explosions.size(); i < l; ++i) {
			Explosion e = explosions.get(i);
			e.update(delta);
		}
	}

	public void spawn(double x, double y, double radius, double dx, double dy) {

		Explosion exp = null;
		for (int i = 0, l = explosions.size(); i < l; ++i) {
			Explosion e = explosions.get(i);
			if (!e.active) {
				exp = e;
				break;
			}
		}

		if (exp == null) {
			exp = new Explosion();
			explosions.add(exp);
		}

		exp.x = x;
		exp.y = y;
		exp.dx = dx;
		exp.dy = dy;
		exp.radius = radius;
		exp.time = 0;
		exp.active = true;

	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs) {
		for (int i = 0, l = explosions.size(); i < l; ++i) {
			Explosion e = explosions.get(i);
			if (!e.active)
				continue;
			e.draw(g, obs);
		}
	}
}
