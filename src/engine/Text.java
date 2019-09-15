package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Text implements Drawable {

	/**
	 * Load a TrueType font from disk
	 */
	public static void loadFont(String file) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(file)));
		} catch (FontFormatException | IOException e) {
			System.err.println("Failed to load font " + file);
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	public static Font createFont(String family, int style, int size) {
		return new Font(family, style, size);
	}

	private Font font;
	private Color color;
	private double pos_x = 0, pos_y = 0;
	private char[] data = null;
	private int length = 0;
	private boolean visible = true;
		
	public Text(Font font) {
		this.font = font;
		color = Color.WHITE;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean b) {
		visible = b;
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

	public void setText(String text) {
		if (text == null || text.isEmpty()) {
			data = null;
			return;
		}

		length = text.length();

		if (data == null || length >= data.length) {
			data = new char[length + 1];
			data[length] = 0;
		}

		// XXX: bad, stupid, slow.
		for (int i = 0; i < length; ++i) {
			data[i] = text.charAt(i);
		}
	}

	@Override
	public void draw(Graphics2D g, ImageObserver obs) {
		if(!visible) return;
		
		if (data != null) {
			g.setFont(font);
			g.setColor(color);
			g.drawChars(data, 0, length, (int) (pos_x + 0.5), (int) (pos_y + 0.5));
		}
	}
}
