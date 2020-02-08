package math;

import java.util.Random;

public final class Maths {
	
	public static final float PI = 3.14159265f;
	public static final float PI_2 = 6.28318530f;
	
	public static float map(float x, float a, float b, float c, float d) {
		return (x - a) / (b - a) * (d - c) + c;
	}
	
	public static double map(double x, double a, double b, double c, double d) {
		return (x - a) / (b - a) * (d - c) + c;
	}
	
	public static float interpolate(float a, float b, float t) {
		return a + (b - a) * t;
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
		float m = theta % Maths.PI_2;
		return (m > Maths.PI) ? (m - Maths.PI_2) : m;
	}
	
	public static double randomGaussian() {
		Random r = new Random();
		return r.nextGaussian();
	}
	
	public static double randomBilateral() {
		return Math.random() * 2 -1;
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
