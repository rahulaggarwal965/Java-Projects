package threeDimensions;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import math.Matrix;

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
	
	public int[] getPixels() {
		return this.pixels;
	}
	
	public Texture resize(int width, int height) {
		int[] pixels = new int[width*height];
		double fx = (double) this.width / width, fy = (double) this.height / height;
		double px, py;
		for (int j = 0; j < height; j++) {
			py = Math.floor(j * fy);
			for (int i = 0; i < width; i++) {
				px = Math.floor(i * fx);
				pixels[i + j*width] = this.pixels[(int)((py*this.width) + px)];
			}
		}
		return new Texture(pixels, width, height);
	}
	
	public Texture resize(double fx, double fy) {
		return this.resize((int) (this.width * fx), (int) (this.height * fy));
	}
	
	public Texture getSubTexture(int x, int y, int width, int height) {
		int[] pixels = new int[width*height];
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				pixels[i + j*width] = this.getPixel(i + x, j + y);
			}
		}
		return new Texture(pixels, width, height);
	}
	
	public static Texture loadImage(String filepath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filepath));

		} catch (IOException e) {
			System.out.println(e);
		}

		int[] pixels = new int[img.getWidth() * img.getHeight()];
		if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			byte[] src = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for (int i = 0, j = 0; i < pixels.length; i++ ) {
				byte b = src[j++];
				byte g = src[j++];
				byte r = src[j++];
				pixels[i] = ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
		} else if(img.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
			byte[] src = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for (int i = 0, j = 0; i < pixels.length; i++ ) {
				byte a = src[j++];
				byte b = src[j++];
				byte g = src[j++];
				byte r = src[j++];
				pixels[i] = ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
		}
		return new Texture(pixels, img.getWidth(), img.getHeight());
	}
	
	public static Texture loadImage(URL url) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(url);

		} catch (IOException e) {
			System.out.println(e);
		}

		int[] pixels = new int[img.getWidth() * img.getHeight()];
		if (img.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			byte[] src = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for (int i = 0, j = 0; i < pixels.length; i++ ) {
				byte b = src[j++];
				byte g = src[j++];
				byte r = src[j++];
				pixels[i] = ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
		} else if(img.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
			byte[] src = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for (int i = 0, j = 0; i < pixels.length; i++ ) {
				byte a = src[j++];
				byte b = src[j++];
				byte g = src[j++];
				byte r = src[j++];
				pixels[i] = ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
			}
		}
		return new Texture(pixels, img.getWidth(), img.getHeight());
	}
	
	public static Matrix toMatrix(Texture t) {
		Matrix m = new Matrix(t.getHeight(), t.getWidth());
		int[] iPixels = t.getPixels();
		float[] fPixels = m.getData();
		for (int i = 0; i < iPixels.length; i++) {
			fPixels[i] = iPixels[i]/255.0f;
		}
		return m;
	}
}
