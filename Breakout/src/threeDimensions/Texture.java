package threeDimensions;

public class Texture {
	private int[] pixels; //READ ONLY
	private int width, height;
	
	public Texture(int[] pixels, int width, int height) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getPixel(int x, int y) {
		return this.pixels[y * this.width + x];
	}
}
