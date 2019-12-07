package libraries;

import java.util.Random;

public class Maths {
	
	public static final float PI = 3.14159265f;
	public static final float PI_2 = 6.28318530f;
	
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
	
	public static double distance(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)); 
	}
	
	public static boolean pointPointIntersection(float x1, float y1, float x2, float y2) {
		if(x1 == x2 && y1 == y2) {
			return true;
		}
		return false;
	}
	
	public static boolean pointCircleIntersection(float x, float y, float cx, float cy, float r) {
		if(Maths.distance(cx, cy, x, y) <= r) {
				return true;
		}
		return false;
	}
	
	public static boolean circleCircleIntersection(float cx1, float cy1, float r1, float cx2, float cy2, float r2) {
		if(Maths.distance(cx1, cy1, cx2, cy2) <= r1 + r2) {
			return true;
		}
		return false;
	}
	
	public static boolean pointRectangleIntersection(float x1, float y1, float x2, float y2, float w, float h) {
		if(x1 >= x2 && x1 <= x2 + w && y1 >= y2 && y1 <= y2 + h) {
			return true;
		}
		return false;
	}
	
	public static boolean rectangleRectangleIntersection(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		if(x1 + w1 >= x2 && x1 <= x2 + w2 && y1 + h1 >= y2 && y1 <= y2 + h2) {
			return true;
		}
		return false;
	}
	
	public static boolean rectangleCircleIntersection(float x, float y, float w, float h, float cx, float cy, float r) {
		float tX = cx, tY = cy;
		if (cx < x) tX = x;
		else if(cx > x + w) tX = x + w;
		if (cy < y) tY = y;
		else if(cy> y + h) tY = y + h;
		if(Maths.distance(cx, cy, tX, tY) <= r) {
			return true;
		}
		return false;
	}
	
	public static int sideRectangleCircleIntersection(float x, float y, float w, float h, float cx, float cy, float r, float dx, float dy) {
		if (!rectangleCircleIntersection(x, y, w, h, cx, cy, r)) return -1;
		//R or L
		if(cy + r - dy > y && cy - r - dy < y + h) {
			//Left
			if(cx + r - dx < x) return 0;
			//Right
			//else if(cx - r - dx > x + w) return 2;
			else return 2;
		// T or B
		} else {
			//Top
			if (cy + r - dy < y) return 1;
			//Bottom
			//else if(cy - r - dy > y + h) return 4;
			else return 3;
		}
		
	}
	
	public static boolean linePointIntersection(float x1, float y1, float x2, float y2, float px, float py) {
		if(Math.abs(Maths.distance(x1, y1, px, py) + Maths.distance(x1, y1, px, py) - Maths.distance(x1, y1, x2, y2)) <= 0.1) {
			return true;
		}
		return false;
	}
	
	public static boolean lineCircleIntersection(float x1, float y1, float x2, float y2, float cx, float cy, float r) {
		if(Maths.pointCircleIntersection(x1, y1, cx, cy, r) || Maths.pointCircleIntersection(x2, y2, cx, cy, r)) {
			return true;
		}
		float projPercent = ((cx - x1)*(x2 - x1) + (cy - y1)*(y2-y1))/((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		float pX = x1 + projPercent*(x2-x1);
		float pY = y1 + projPercent*(y2-y1);
		if(!(Maths.linePointIntersection(x1, y1, x2, y2, pX, pY))) {
			return false;
		}
		if(Maths.distance(pX, pY, cx, cy) <= r) {
			System.out.println("collision");
			return true;
		}
		System.out.println("end");
		return false;
	}
	
	public static boolean lineLineIntersection (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		  float h = ((x4 - x3)*(y1 - y3) - (y4 - y3)*(x1-x3)) / ((y4 - y3)*(x2-  x1) - (x4 - x3)*(y2 - y1));
		  float g = ((x2 - x1)*(y1 - y3) - (y2 - y1)*(x1-x3)) / ((y4 - y3)*(x2 - x1) - (x4 - x3)*(y2 - y1));
		  if (h >=  0 && h <= 1 && g >= 0 && g <= 1) {
			  return true;
		  }
		  return false;
	}
	
	public static boolean lineRectangleIntersection(float x1, float y1, float x2, float y2, float rx, float ry, float w, float h) {
		return Maths.lineLineIntersection(x1, y1, x2, y2, rx, ry, rx+w, ry) || Maths.lineLineIntersection(x1, y1, x2, y2, rx+w, ry, rx+w, ry+h) || Maths.lineLineIntersection(x1, y1, x2, y2, rx+w, ry+h, rx, ry+h) || Maths.lineLineIntersection(x1, y1, x2, y2, rx, ry+h, rx, ry);
	}
}
