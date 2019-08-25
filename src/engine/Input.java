package engine;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class for handling user input. We only support up/down/left/right directional
 * input, a fire button and a menu/exit button. These are currently mapped to UP
 * ARROW, DOWN ARROW, LEFT ARROW, RIGHT ARROW, SPACE and ESCAPE, respectively.
 */
public class Input {

	// Publicly visible input names
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int FIRE = 4;
	public static final int EXIT = 5;

	// Fucntion to help map Java's virtual keycodes to the symbolic input
	// names that the user cares about
	private static int getInputForKey(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			return UP;
		case KeyEvent.VK_DOWN:
			return DOWN;
		case KeyEvent.VK_LEFT:
			return LEFT;
		case KeyEvent.VK_RIGHT:
			return RIGHT;
		case KeyEvent.VK_SPACE:
			return FIRE;
		case KeyEvent.VK_ESCAPE:
			return EXIT;
		default:
			// This is not a key we care about, so return -1
			return -1;
		}
	}

	// Internal list key of states
	// 'active' is the one that gets manipulated by the key listener,
	// 'current' is the list of keys that was down at the start of the current frame
	// and
	// 'last' is the list of keys that was down during the previous frame
	private final boolean keys_active[] = new boolean[6];
	private final boolean keys_current[] = new boolean[6];
	private final boolean keys_last[] = new boolean[6];

	/**
	 * Create a new Input object that attaches itself to an AWT Frame. This lets us
	 * listen to input events that pass through the Frame.
	 * 
	 * @param f an AWT Frame object; see Screen.java for more on that
	 */
	public Input(Frame f) {

		// Attach a KeyListener to the window to be able to listen
		// to the keyboard being used
		f.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// We do not need to react to this event at all
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// Find out which input the key represents, if any
				int key = getInputForKey(e);

				// If the function we called returns an input of -1,
				// i.e. less than zero, we don't actually care about
				// that key, so we can return early
				if (key < 0)
					return;

				// We know we have a valid input if we're here, so we
				// set the active key input to _false_ (the key was released,
				// so it should no longer be held down)
				keys_active[key] = false;

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// Almost same deal as above...

				int key = getInputForKey(e);

				if (key < 0)
					return;

				// Set the active key input to _true_ (the key was pressed,
				// so it should now be marked as being held down)
				keys_active[key] = true;
			}
		});
	}

	/**
	 * Check if an input is currently down.
	 * 
	 * @param input an input symbol, see static fields of this class
	 * 
	 * @return true if a input is currently held down
	 */
	public boolean isDown(int input) {
		return keys_current[input];
	}

	/**
	 * Check if an input was pressed at the start of this frame, i.e. it is now
	 * down, but was up last frame.
	 * 
	 * @param input an input symbol, see static fields of this class
	 * 
	 * @return true if button was pressed this frame
	 */
	public boolean isPressed(int input) {
		return keys_current[input] && !keys_last[input];
	}

	/**
	 * Check if an input was released at the start of this frame, i.e. if it is now
	 * up, but was down last frame.
	 * 
	 * @param input an input symbol, see static fields of this class
	 * 
	 * @return true if button was released this frame
	 */
	public boolean isReleased(int input) {
		return !keys_current[input] && keys_last[input];
	}

	/**
	 * Update function - this cycles the input states. This function should be
	 * called once every frame.
	 */
	public void update() {
		for (int i = 0; i < keys_active.length; ++i) {
			keys_last[i] = keys_current[i];
			keys_current[i] = keys_active[i];
		}
	}
}
