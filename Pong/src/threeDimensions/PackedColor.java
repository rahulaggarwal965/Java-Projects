package threeDimensions;

import libraries.Maths;

public class PackedColor {
	
	public static final int White = 0x00ffffff;
	public static final int Black = 0x00000000;
	public static final int Gray = 0x00808080;
	public static final int LightGray = 0x00D3D3D3;
	public static final int Red = 0x00ff0000;
	public static final int Green = 0x0000ff00;
	public static final int Blue = 0x000000ff;
	public static final int Yellow = 0x00ffff00;
	public static final int Cyan = 0x0000ffff;
	public static final int Magenta = 0x00ff00ff;
	
	public static int makeRGB(byte red, byte green, byte blue) {
		return ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
	}
	
	public static int makeRGB(Vec3 color) {
		int r = Maths.clamp((int) color.x, 0, 255);
		int g = Maths.clamp((int) color.y, 0, 255);
		int b = Maths.clamp((int) color.z, 0, 255);
		return ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	}
	
	public static int makeRGBA(byte red, byte green, byte blue, byte alpha) {
		return alpha << 24 | red << 16 | green << 8 | blue;
	}
}
