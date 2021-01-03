package main;

public class Utils {
	public static final char[] columns = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };

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

	public static int getX(int index) {
		return index % 8;
	}

	public static int getY(int index) {
		return (index - getX(index)) / 8;
	}

	public static int getIndex(int x, int y) {
		return x + y * 8;
	}
}