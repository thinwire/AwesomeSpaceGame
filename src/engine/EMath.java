package engine;

/**
 * More math routines. EMath stands for "Extended Math", the name is chosen so
 * that a clear difference can be drawn between this class and java.lang.Math
 */
public class EMath {

	/**
	 * Make sure that a value is between a minimum and a maximum value
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static final double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}

	/**
	 * Move a value closer to zero by some amount
	 * 
	 * @param value
	 * @param amount
	 * @return
	 */
	public static final double reduce(double value, double amount) {
		double t = Math.max(Math.abs(value) - amount, 0);
		return Math.signum(value) * t;
	}

	/**
	 * Get a random value between a minimum and a maximum limit
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static final double rand(double min, double max) {
		double range = max - min;
		return Math.random() * range + min;
	}

	/**
	 * Wrap a value between a minimum and a maximum.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static final double wrap(double value, double min, double max) {
		if (min == max) {
			return min;
		}

		// ...you're not expected to understand this

		double v0 = value - min;
		double d = max - min;
		double v1 = v0 - ((int) (v0 / d) * d);
		return min + v1 + (v1 < .0 ? d : .0);
	}

}
