package threeDimensions;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import gameEngine.GameEngine;
import math.Maths;
import math.Vec2;

public class Graphics3D {
	
	final int INSIDE = 0; 
	final int LEFT = 1;   
	final int RIGHT = 2;  
	final int BOTTOM = 4; 
	final int TOP = 8;    
	
	private int[] pixels;
	
	public Graphics3D() {
		this.pixels = new int[GameEngine.displayWidth * GameEngine.displayHeight];
	}
	
	public void clear(int color) {
		for (int i = 0; i < this.pixels.length; i++) {
			this.pixels[i] = color;
		}
	}

	public void drawPixel(int x, int y, int color) {
		this.pixels[y * GameEngine.displayWidth + x] = color;
	}
	
	public void drawClippedPixel(int x, int y, int color) {
		if(x >= 0 && x < GameEngine.displayWidth && y >= 0 && y < GameEngine.displayHeight) {
			this.pixels[y * GameEngine.displayWidth + x] = color;
		}
	}
	
	public void drawPixels(int[] pixels) {	
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}
	
	public void drawAlphaPixel(int x, int y, int color) {
		int loc = y * GameEngine.displayWidth + x;
		float a1 = ((this.pixels[loc] >> 24) & 0xff) / 255.0f;
		float a0 = ((color >> 24) & 0xff) / 255.0f;
		
		float aEnd = a0 + a1*(1 - a0);
		int a = (int) (aEnd * 255);
		float af = a0/aEnd;
		
		int r1 = ((this.pixels[loc] >> 16) & 0xff);
		int r0 = ((color >> 16) & 0xff);
		int r = (int) (af * r0 + (1-af) * r1);
		
		int g1 = ((this.pixels[loc] >> 8) & 0xff);
		int g0 = ((color >> 8) & 0xff);
		int g = (int) (af * g0 + (1-af) * g1); 
		
		int b1 = ((this.pixels[loc] & 0xff));
		int b0 = ((color & 0xff));
		int b = (int) (af * b0 + (1-af) * b1); 
		
		this.pixels[loc] = (a & 0xff) << 24  | (r & 0xff) << 16 | ((g & 0xff) << 8) | (b & 0xff);
		
	}
	
	public void drawAdditivePixel(int x, int y, int color) {
		int loc = y * GameEngine.displayWidth + x;
		float a0 = ((color >> 24) & 0xff) / 255.0f;
		int r0 = (int) (((color >> 16) & 0xff) * a0);
		int g0 = (int) (((color >> 8) & 0xff) * a0);
		int b0 = (int) (((color & 0xff)) * a0);
		
		int r = r0 + ((this.pixels[loc] >> 16) & 0xff);
		int g = g0 + ((this.pixels[loc] >> 8) & 0xff);
		int b = b0 + ((this.pixels[loc] & 0xff));
		
		this.pixels[loc] = PackedColor.makeRGB(r, g, b);
	}
	
	int ComputeOutCode(double x, double y)
	{
		int code;

		code = INSIDE;          

		if (x < 0)           
			code |= LEFT;
		else if (x > GameEngine.displayWidth - 1)      
			code |= RIGHT;
		if (y > GameEngine.displayHeight - 1)           
			code |= BOTTOM;
		else if (y < 0)
			code |= TOP;

		return code;
	}

	void clipLine(float x0, float y0, float x1, float y1, int color)
	{
		
		int outcode0 = ComputeOutCode(x0, y0);
		int outcode1 = ComputeOutCode(x1, y1);
		boolean accept = false;

		while (true) {
			if ((outcode0 | outcode1) == 0) {
				accept = true;
				break;
			} else if ((outcode0 & outcode1) != 0) {
				break;
			} else {
				float x = 0, y = 0;
				int outcodeOut = (outcode0 != 0) ? outcode0 : outcode1;

				if ((outcodeOut & TOP) != 0) {           
					x = x0 + (x1 - x0) * (0 - y0) / (y1 - y0);
					y = 0;
				} else if ((outcodeOut & BOTTOM) != 0) { 
					x = x0 + (x1 - x0) * (GameEngine.displayHeight - 1 - y0) / (y1 - y0);
					y = GameEngine.displayHeight - 1;
				} else if ((outcodeOut & RIGHT) != 0) {  
					y = y0 + (y1 - y0) * (GameEngine.displayWidth - 1 - x0) / (x1 - x0);
					x = GameEngine.displayWidth - 1;
				} else if ((outcodeOut & LEFT) != 0) {   
					y = y0 + (y1 - y0) * (0 - x0) / (x1 - x0);
					x = 0;
				}

				if (outcodeOut == outcode0) {
					x0 = x;
					y0 = y;
					outcode0 = ComputeOutCode(x0, y0);
				} else {
					x1 = x;
					y1 = y;
					outcode1 = ComputeOutCode(x1, y1);
				}
			}
		}
		if (accept) {
			drawLine(x0, y0, x1, y1, color);
		}
	}
	
	void clipLine(Vec2 v0, Vec2 v1, int color) {
		this.clipLine(v0.x, v0.y, v1.x, v1.y, color);
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
	
	public void drawLine(Vec2 v0, Vec2 v1, int color) {
		this.drawLine(v0.x, v0.y, v1.x, v1.y, color);
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
			int ry = j * GameEngine.displayWidth;
			for (int i = sX; i < eX; i++) {
				this.pixels[ry + i] = color;
			}
		}
	}

	public void fillRectangle(Rectangle r, int color) {
		fillRectangle(r.x, r.y, r.width, r.height, color);
	}
	
	private void drawSymmetricCirclePoint(int xc, int yc, int x, int y, int color) {
		this.drawClippedPixel(xc+x, yc+y, color);
		this.drawClippedPixel(xc-x, yc+y, color);
		this.drawClippedPixel(xc+x, yc-y, color);
		this.drawClippedPixel(xc-x, yc-y, color);
		this.drawClippedPixel(xc+y, yc+x, color);
		this.drawClippedPixel(xc-y, yc+x, color);
		this.drawClippedPixel(xc+y, yc-x, color);
		this.drawClippedPixel(xc-y, yc-x, color);
	}
	
	//Center Based
	public void drawCircle(float x0, float y0, float r, int color) {
		int xc = (int) x0, yc = (int) y0;
		int x = 0;
		int y = (int) r;
		int d = (int) (1 - r);
		this.drawSymmetricCirclePoint(xc, yc, x, y, color);
		
		while(y > x) {
			if(d < 0) d += 2*x + 3;
			else {
				d += 2 * (x - y) + 5;
				y--;
			}
			x++;
			this.drawSymmetricCirclePoint(xc, yc, x, y, color);
 		}
	}
	
	//Center Based
	public void fillCircle(float x0, float y0, float r, int color) {
		int xc = (int) x0, yc = (int) y0;
		int x = 0;
		int y = (int) r;
		int d = (int) (1 - r);
		for (int i = 0; i < y; i++) {
			this.drawSymmetricCirclePoint(xc, yc, x, i, color);
		}
		
		while(y > x) {
			if(d < 0) d += 2*x + 3;
			else {
				d += 2 * (x - y) + 5;
				y--;
			}
			x++;
			for (int i = 0; i < y; i++) {
				this.drawSymmetricCirclePoint(xc, yc, x, i, color);
			}
 		}
	}
	
	public void drawTriangle(Triangle t, int color) {
		this.clipLine(t.v0.position, t.v1.position, color);
		this.clipLine(t.v1.position, t.v2.position, color);
		this.clipLine(t.v2.position, t.v0.position, color);
	}
	
	public void drawTexture(Texture texture, int x, int y) {
		int sY = (int) y; int sX = (int) x; int eY = (int) (y + texture.getHeight()); int eX = (int) (x + texture.getWidth());
		if(sY >= GameEngine.displayHeight || eY <= 0 || sX >= GameEngine.displayWidth || eX <= 0) return;
		else {
			if(sY < 0) sY = 0;
			if(eY > GameEngine.displayHeight) eY = GameEngine.displayHeight;
			if(sX < 0) sX = 0;
			if(eX > GameEngine.displayWidth) eX = GameEngine.displayWidth;
		}
		for (int j = sY; j < eY; j++) {
			int ry = j * GameEngine.displayWidth;
			for (int i = sX; i < eX; i++) {
				this.pixels[ry + i] = texture.getPixel(i - sX, j - sY);
			}
		}
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(GameEngine.displayWidth, GameEngine.displayHeight, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = img.getRaster();
		wr.setDataElements(0, 0, GameEngine.displayWidth, GameEngine.displayHeight, pixels);
		return img;
	}
}
