package threeDimensions;

import java.awt.Color;

import math.Maths;

public class PackedColor {
	
	public static final int White = 0xffffffff;
	public static final int Black = 0xff000000;
	public static final int Gray = 0xff808080;
	public static final int LightGray = 0xffD3D3D3;
	public static final int Red = 0xffff0000;
	public static final int Green = 0xff00ff00;
	public static final int Blue = 0xff0000ff;
	public static final int Yellow = 0xffffff00;
	public static final int Cyan = 0xff00ffff;
	public static final int Magenta = 0xffff00ff;
	
	
	public static int makeRGB(int r, int g, int b) {
		r = Maths.clamp(r, 0, 255);
		g = Maths.clamp(g, 0, 255);
		b = Maths.clamp(b, 0, 255);
		return (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}
	
	public static int makeRGBA(int r, int g, int b, int a) {
		r = Maths.clamp(r, 0, 255);
		g = Maths.clamp(g, 0, 255);
		b = Maths.clamp(b, 0, 255);
		a = Maths.clamp(a, 0, 255);
		return (a & 0xff) << 24  | (r & 0xff) << 16 | ((g & 0xff) << 8) | (b & 0xff);
	}
	
	public static int makeRGB(double r, double g, double b) {
		return makeRGB((int) (r*255), (int) (g * 255), (int) (b*255));
	}
	
	public static int makeRGBA(double r, double g, double b, double a) {
		return makeRGBA((int) (r*255), (int) (g * 255), (int) (b*255), (int) (a*255));
	}
	
	public static int makeRGB(Vec3 color) {
		return makeRGB((int) color.x, (int) color.y, (int) color.z);
	}
	
	public static int makeRGBA(Vec4 color) {
		return makeRGBA((int) color.x, (int) color.y, (int) color.z, (int) color.w);
	}
	
	public static Vec4 toVector(Color color) {
		return new Vec4(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	//h0 - h1 []0 - 360] s0 - s1 [0-100] b0 - b1 [0-100]
	public static int randomHSB(int h0, int h1, int s0, int s1, int b0, int b1) {
		h0 = Maths.clamp(h0, 0, 360);
		h1 = Maths.clamp(h1, 0, 360);
		s0 = Maths.clamp(s0, 0, 100);
		s1 = Maths.clamp(s1, 0, 100);
		b0 = Maths.clamp(b0, 0, 100);
		b1 = Maths.clamp(b1, 0, 100);
		float h = (float) (h0 + Math.random() * (h1 - h0))/360;
		float s = (float) (s0 + Math.random() * (s1 - s0))/100;
		float b = (float) (b0 + Math.random() * (b1 - b0))/100;
		return Color.HSBtoRGB(h, s, b);
	
	}
	
	
	
}