package math;

import java.util.Random;

public final class Maths {
	
	public static final float PI = (float) Math.PI;
	public static final float PI2 = (float) (Math.PI * 2);
	public static final float EPSILON = 0.00000000001f;
	
	public static float map(float x, float a, float b, float c, float d) {
		return (x - a) / (b - a) * (d - c) + c;
	}
	
	public static double map(double x, double a, double b, double c, double d) {
		return (x - a) / (b - a) * (d - c) + c;
	}
	
	public static float interpolate(float a, float b, float t) {
		return a + (b - a) * t;
	}
	
	public static float inverseLerp(float a, float b, float value) {
		return (value - a) / (b - a);
	}
	
	public static float sqrt(float x) {
		return (float) Math.sqrt(x);
	}
	
	public static float random(float val) {
		return (float) (Math.random() * val);
	}
	
	public static float pow(float a, float b) {
		return (float) Math.pow(a, b);
	}
	
	public static float clamp(float value, float min, float max) {
		if(value < min) return min;
		else if(value > max) return max;
		return value;
	}
	
	public static int clamp(int value, int min, int max) {
		if(value < min) return min;
		else if(value > max) return max;
		return value;
	}
	
	public static float pointTowards(float a, float b, float a1, float b1) {
		return (float) Math.atan2(b1 - b, a1 - a);
	}
	
	public static float atan2(float y, float x) {
		return (float) Math.atan2(y, x);
	}
	
	public static <T extends Comparable<T>> T clamp(T value, T min, T max) {
		if(value.compareTo(min) < 0) {
			return min;
		} else if (value.compareTo(max) > 0) {
			return max;
		}
		return value;
	}
	
	//for large angles and preserving accuracy
	public static float wrapAngle(float theta) {
		float m = theta % Maths.PI2;
		return (m > Maths.PI) ? (m - Maths.PI2) : m;
	}
	
	public static double randomGaussian() {
		Random r = new Random();
		return r.nextGaussian();
	}
	
	public static double randomBilateral() {
		return Math.random() * 2 -1;
	}
	
	public static float randomBilateral(float val) {
		return Maths.random(val) * 2 - val;
	}
	
	public static double distance(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)); 
	}
	
	public static float squaredDistanceToRect(float x, float y, float rx, float ry, float w, float h) {
		float dx = Math.max(Math.abs(x - rx) - w/2, 0);
		float dy = Math.max(Math.abs(y - ry) - h/2, 0);
		return dx * dx + dy * dy;
	}
	
	
}
