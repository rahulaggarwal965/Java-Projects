package threeDimensions;

import java.awt.Color;

import libraries.Maths;

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
		return (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}
	
	public static int makeRGBA(int r, int g, int b, int a) {
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
	
	
	
	
	
	
	
	
}
