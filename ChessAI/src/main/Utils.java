package main;

public class Utils {
	public static boolean inRange(int var, int min, int max) {
		return var >= min && var <= max;
	}

	public static int clamp(int var, int min, int max) {
		if (var <= min)
			return min;
		else if (var >= max)
			return max;
		else
			return var;
	}
}