package threeDimensions;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import gameEngine.GameEngine;

public class Graphics3D {
	
	private int[] pixels;
	
	public Graphics3D() {
		this.pixels = new int[GameEngine.displayWidth * GameEngine.displayHeight];
	}
	
	public void clear(int color) {
		for (int j = 0; j < GameEngine.displayHeight; j++) {
			for (int i = 0; i < GameEngine.displayWidth; i++) {
				this.pixels[j * GameEngine.displayWidth + i] = color;
			}
		}
	}
	
	public void drawPixel(int x, int y, int color) {
		this.pixels[y * GameEngine.displayWidth + x] = color;
	}
	
	public void drawPixels(int[] pixels) {	
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, int color) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		if(dy == 0.0f && dx == 0.0f) {
			this.drawPixel((int) x1, (int) y1, color);
		} else if (Math.abs(dy) > Math.abs(dx)) {
			if(dy < 0.0f) {
				float temp = x1;
				x1 = x2;
				x2 = temp;
				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			
			float m = dx/dy;
			float y = y1;
			int lastIntY = (int) y;
			for (float x = x1; y < y2; y += 1.0f, x+=m) {
				lastIntY = (int) y;
				this.drawPixel((int) x, lastIntY, color);
			}
			if((int) y2 > lastIntY) {
				this.drawPixel((int) x2, (int) y2, color);
			}
 		} else {
 			if(dx < 0.0f) {
				float temp = x1;
				x1 = x2;
				x2 = temp;
				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			
			float m = dy/dx;
			float x = x1;
			int lastIntX = (int) x;
			for (float y = y1; x < x2; x += 1.0f, y+=m) {
				lastIntX = (int) x;
				this.drawPixel(lastIntX, (int) y, color);
			}
			if((int) x2 > lastIntX) {
				this.drawPixel((int) x2, (int) y2, color);
			}
 		}
	}
	
	public void drawLine(Vec2 v1, Vec2 v2, int color) {
		this.drawLine(v1.x, v1.y, v2.x, v2.y, color);
	}
	
	public void fillRectangle(float x, float y, float width, float height, int color) {
		int sY = (int) y; int sX = (int) x; int eY = (int) (y + height); int eX = (int) (x + width);
		if(sY >= GameEngine.displayHeight || eY <= 0 || sX >= GameEngine.displayWidth || eX <= 0) return;
		else {
			if(sY < 0) sY = 0;
			if(eY > GameEngine.displayHeight) eY = GameEngine.displayHeight;
			if(sX < 0) sX = 0;
			if(eX > GameEngine.displayWidth) eX = GameEngine.displayWidth;
		}
		for (int j = sY; j < eY; j++) {
			for (int i = sX; i < eX; i++) {
				this.pixels[j * GameEngine.displayWidth + i] = color;
			}
		}
	}

	public void fillRectangle(Rectangle r, int color) {
		fillRectangle(r.x, r.y, r.width, r.height, color);
	}
	
	public void fillEllipse(float x, float y, float width, float height, int color) {
		
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(GameEngine.displayWidth, GameEngine.displayHeight, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = img.getRaster();
		wr.setDataElements(0, 0, GameEngine.displayWidth, GameEngine.displayHeight, pixels);
		return img;
	}
}
