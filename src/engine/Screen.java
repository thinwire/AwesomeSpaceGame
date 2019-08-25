package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * Game screen. Handles the game window and drawing graphics onto it.
 */
public class Screen {

	private Toolkit toolkit;
	private JFrame frame;
	private int width;
	private int height;

	private int yoffset;

	private ScreenPainter painter;
	private BufferStrategy bufstrat;

	Screen(int width, int height, ScreenPainter painter) {
		// Assign painter
		this.painter = painter;

		// Create a frame (the actual visible app window)
		frame = new JFrame("Untitled Game");

		// Set the size of the frame and show it. Our app
		// will run as long as the frame is visible
		this.width = width;
		this.height = height;
		frame.setResizable(false);
		frame.getContentPane().setPreferredSize(new Dimension(width, height));
		frame.pack();
		frame.setVisible(true);

		yoffset = frame.getHeight() - height + 1; // This should be wrong, but is correct. WTF, Java, why the +1 ?

		System.out.println("Screen width " + width + " height " + height + " requested");
		System.out.println("Window width " + frame.getWidth() + " height " + frame.getHeight());

		// We do _not_ want to repaint the panel contents when the operating system
		// wants to do so; we want to repaint when _we_ feel like it.
		// Set up double buffered drawing and store a reference to the buffer strategy
		// to let us control painting
		frame.setIgnoreRepaint(true);
		frame.createBufferStrategy(2);
		bufstrat = frame.getBufferStrategy();

		toolkit = frame.getToolkit();
	}

	public Frame getFrame() {
		return frame;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void update() {

		// Get a fresh graphics object from the buffer strategy
		Graphics g = bufstrat.getDrawGraphics();

		// Translate context origin to compensate for window border
		g.translate(0, yoffset);

		// Set color for clearing the screen
		g.setColor(Color.BLACK);

		// Clear existing stuff on screen
		g.fillRect(0, 0, width, height);

		// Let the client draw graphics to screen
		painter.paint((Graphics2D) g);

		// Apparently we need to dispose of the graphics
		// object so that the garbage collector doesn't
		// get overwhelmed...
		g.dispose();

		// Flip buffers to present a fresh new frame
		bufstrat.show();

		// Request synchronization of OS-side display
		toolkit.sync();
	}

}
