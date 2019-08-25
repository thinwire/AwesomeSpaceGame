package engine;

import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public abstract class Application implements ScreenPainter {

	protected Screen screen;
	protected Input input;

	private boolean shouldRun = true;
	private boolean shouldPrintFPS = false;
	private long targetFrameRate = 60;
	private int frameRate = 0;

	private ArrayList<Drawable> drawables = new ArrayList<>();
	
	/**
	 * Constructs an application with a basic screen width and a screen height
	 * 
	 * @param screen_width
	 * @param screen_height
	 */
	protected Application(int screen_width, int screen_height) {
		screen = new Screen(screen_width, screen_height, this);
		input = new Input(screen.getFrame());

		// Attach a windowlistener to the screen frame to be able to
		// respond to the user closing the game window. We want to
		// close the game down _gracefully_ by terminating the game
		// core loop.
		
		screen.getFrame().addWindowListener(new WindowListener() {
			
			// We only care about the windowClosing callback which gets
			// triggered when the user presses the 'close' button on the
			// window.
			@Override
			public void windowClosing(WindowEvent e) {
				// By setting this to false the game main loop will exit
				shouldRun = false;
			}

			// The rest of this junk just needs to be there because we're implementing
			// an interface instead of extending a class...
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}	
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
	}

	/**
	 * Set the target frame rate. Should be between 10 and 240.
	 * This might never be achieved. Also there is no vsync.
	 * 
	 * @param fps target frame rate
	 */
	public void setTargetFrameRate(int fps) {
		targetFrameRate = fps < 10 ? 10 : fps > 240 ? 240 : fps;
	}
	
	/**
	 * Get the current actual FPS count. Value is updated roughly once per second.
	 * 
	 * @return an integer value, usually around 60 or so.
	 */
	public int getFrameRate() {
		return frameRate;
	}
	
	/**
	 * Call this with the parameter 'true' to make the engine print
	 * the current FPS to console. Be aware that this can cause stuttering.
	 * 
	 * @param enable true to print FPS once per second, false to disable. Default: false.
	 */
	public void setPrintFPS(boolean enable) {
		shouldPrintFPS = enable;
	}
	
	/**
	 * Run the main game loop.
	 * Most of the core code is adapted from here:
	 * http://www.java-gaming.org/index.php?topic=24220.0
	 */
	public void run() {
		long lastLoopTime = System.nanoTime();
		long lastFpsTime = 0;
		int fps = 0;

		// keep looping until the game ends
		while (shouldRun) {
			
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long now = System.nanoTime();
			long frameTime = now - lastLoopTime;
			lastLoopTime = now;
			
			// Delta time is the time in seconds it took between the start
			// of last frame and the start of this frame
			double delta = frameTime / 1000000000.0;

			// Update FPS counter
			lastFpsTime += frameTime;
			fps++;

			// Dump FPS to console each second - comment this bit out
			// to get more stable speed in the game; printing to console
			// is slow as balls.
			if (lastFpsTime >= 1000000000l) {
				if(shouldPrintFPS) {
					System.out.println("FPS: " + fps);
				}
				frameRate = fps;
				lastFpsTime = 0;
				fps = 0;
			}
			
			// Update input
			input.update();

			// Update game logic, passing in delta timing value
			// to allow for speed compensation
			update(delta);
			
			// Cause screen to redraw. This will call back to our
			// paint(g) routine.
			screen.update();

			// Make the thread sleep so we don't burn up unnecessary CPU time but
			// still keep our frame rate up.
			try {
				// Figure out how long our frame should ideally be
				long targetDuration = 1000000000l / targetFrameRate;
				
				// Figure out how much time we spent doing updates and painting
				long elapsed = System.nanoTime() - now;
				
				// We need to sleep for roughly the amount of time that we didn't spend
				// Thread.sleep expects a duration in milliseconds, and we're operating in
				// nanoseconds, so we need to divide the time by that amount...
				long sleep_tm = (targetDuration - elapsed) / 1000000l;
				
				// Actually sleep. Add 1 msec to get a somewhat saner sleep time...
				Thread.sleep(sleep_tm);
			} catch (Exception ignore) {
				// We ignore all exceptions, because if this fails, we've got unrecoverable problems
			}
		}

		// Dispose of the screen, we don't need it anymore... 
		screen.getFrame().dispose();
	}

	/**
	 * Exits the application by terminating the game loop
	 */
	public void exit() {
		shouldRun = false;
	}

	/**
	 * Add a drawable to the screen
	 * 
	 * @param s a drawable object
	 */
	public void addDrawable(Drawable d) {
		drawables.add(d);
	}

	/**
	 * Remove a drawable from the screen
	 * 
	 * @param s a drawable object
	 */
	public void removeDrawable(Drawable d) {
		drawables.remove(d);
	}

	/**
	 * Remove all drawables
	 */
	public void clearDrawables() {
		drawables.clear();
	}

	/**
	 * Main game loop. This gets called once per frame.
	 * 
	 * @param delta number of seconds that passed between the start of the last
	 *              frame and the beginning of the current frame
	 */
	public abstract void update(double delta);

	/**
	 * The actual screen painting routine. This gets called by Screen when graphics
	 * need to appear.
	 */
	@Override
	public void paint(Graphics2D g) {
		
		ImageObserver obs = screen.getFrame();

		for(int i = 0, l = drawables.size(); i < l; ++i) {
			drawables.get(i).draw(g, obs);
		}	
		
	}

}
