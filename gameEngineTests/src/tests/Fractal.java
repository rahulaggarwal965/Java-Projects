package tests;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import components.CenteredString;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.Triangle;
import threeDimensions.Vec2;

public class Fractal implements GameLogic {
	
	//All
	private final int MAX_LAYERS = 10;
	private final int DELAY_FRAMES = 20;
	
	//Perpendicular Lines
	private final float LINE_MULTIPLIER = 0.72f;
	
	//Tree
	private final float TREE_MULTIPLIER = 0.85f;
	private int angleShift = 15;
	private int deltaAngle = 1;
	
	//Mandelbrot
	//TODO: Implement Arbitrary Precision
	private final int COLOR_GRADIENT = 500;
	private final int COLOR_OFFSET = 350;
	private final int MIN_ITERATIONS = 100;
	private final int MAX_ITERATIONS = 10000;
	private int currentIterations = MIN_ITERATIONS;
	private int[] mandelbrot;
	private int[] colors;
	private double[] view = new double[4]; //left X Bound, right xBound, lower Y Bound, Upper Y Bound
	private double zoom = 1;
	private boolean zoomState = false;
	private boolean shouldUpdate = false;
	
	//Koch Snowflake
	private final float SIN_60 = (float) Math.sin(Math.PI/3);
	
	private int gameState = 0;
	private CenteredString[] text;
	
	private int currentDelay;
	private int currentLayer = 1;
	
	
	public static void main(String[] args) {
		Fractal fractal = new Fractal();
		GameEngine gameEngine = new GameEngine(60, 60, 3, fractal, 800, 800, "Fractal");
	}
	
	public void drawSquares(Graphics2D g2d, Rectangle r, int layer) {
		if(layer != this.currentLayer) {
			g2d.setColor(Color.red);
			g2d.fill(r);
			g2d.setColor(Color.black);
			g2d.draw(r);
			
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					drawSquares(g2d, new Rectangle(r.x + r.width*i - r.width/4, r.y + r.height*j - r.height/4, r.width/2, r.height/2), layer+1);
				}
			}
		}
	}

	public void perpendicularLines(Graphics2D g2d, int x, int y, int length, int dir, int layer) {
		if(layer != this.currentLayer) {
			g2d.setColor(Color.red);
			int lX = x - length/2*(1-dir);
			int lY = y - length/2*dir;
			g2d.drawLine(lX, lY, lX + length*(1-dir), lY + length*dir);
			
			for (int i = 0; i < 2; i++) {
				perpendicularLines(g2d, lX + length*(1-dir)*i, lY + length*dir*i, (int) (length * LINE_MULTIPLIER), (1-dir), layer+1);
			}
		}
	}
	
	public void tree(Graphics2D g2d, int x, int y, int length, int angle, int layer) {
		if(layer != this.currentLayer) {
			g2d.setColor(Color.red);
			
			double rad = Math.toRadians(angle - 90);
			int xOffset = (int) (length*Math.sin(rad));
			int yOffset = (int) (length*Math.cos(rad));
			g2d.drawLine(x, y, x - xOffset, y - yOffset);

			for (int i = 0; i < 2; i++) {
				tree(g2d, x - xOffset, y - yOffset, (int) (length*TREE_MULTIPLIER), angle + (2*i - 1)*angleShift, layer+1);
			}
		}
	}
	
	public void serpinskiCarpet(Graphics2D g2d, Rectangle r, int layer) {
		if(layer != this.currentLayer) {
			g2d.setColor(Color.white);
			g2d.fill(r);
			
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if(i == 0 && j == 0) continue;
					serpinskiCarpet(g2d, new Rectangle(r.x + r.width*i + r.width/3, r.y+r.height*j + r.height/3, r.width/3, r.height/3), layer+1);
				}
			}
			
			
		}
	}
	
	public void serpinskiGasket(Graphics2D g2d, Triangle<Vec2> t, int layer) {
		if(layer != this.currentLayer) {
			g2d.setColor(Color.white);
			int[] xPoints = {(int) t.v0.x, (int) t.v1.x, (int) t.v2.x};
			int[] yPoints = {(int) t.v0.y, (int) t.v1.y, (int) t.v2.y};
			g2d.drawPolygon(xPoints, yPoints, 3);
			
			Vec2 lMid = t.v0.interpolateTo(t.v2, 0.5f);
			Vec2 rMid = t.v1.interpolateTo(t.v2, 0.5f);
			Vec2 bMid = t.v0.interpolateTo(t.v1, 0.5f);
			
			serpinskiGasket(g2d, new Triangle<Vec2>(t.v0, bMid, lMid), layer+1);
			serpinskiGasket(g2d, new Triangle<Vec2>(bMid, t.v1, rMid), layer+1);
			serpinskiGasket(g2d, new Triangle<Vec2>(lMid, rMid, t.v2), layer+1);
		}
	}
	
	public void cantorSet(Graphics2D g2d, int x, int y, int length, int layer) {
		if(layer != this.currentLayer) {
			g2d.setColor(Color.red);
			g2d.drawLine(x, y, x + length, y);
			
			int nLength = length/3;
			for (int i = 0; i < 2; i++) {
				cantorSet(g2d, x + 2*i*nLength, y + 20, nLength, layer+1);
			}
		}
	}
	
	private void kochSnowflake(Graphics2D g2d, Vec2 s, Vec2 e, int layer) {
		if(layer == this.currentLayer) {
			g2d.setColor(Color.white);
			g2d.drawLine((int) s.x, (int) s.y, (int) e.x, (int) e.y);
		} else {
			Vec2 length = e._subtract(s)._divide(3);
			
			Vec2 a = s._add(length);
			Vec2 b = s.interpolateTo(e, 0.5f)._add(new Vec2(-length.y*SIN_60, length.x*SIN_60));
			Vec2 c = e._subtract(length);
			
			kochSnowflake(g2d, s, a, layer+1);
			kochSnowflake(g2d, a, b, layer+1);
			kochSnowflake(g2d, b, c, layer+1);
			kochSnowflake(g2d, c, e, layer+1);
		}
	}
	
	private int mandelbrotPixel(int val) {
		int i = val % GameEngine.displayWidth;
		int j = val / GameEngine.displayWidth;
		
		final double x0 = Maths.map(i, 0, GameEngine.displayWidth - 1, this.view[0], this.view[1]);
		final double y0 = Maths.map(j, 0, GameEngine.displayHeight - 1, this.view[3], this.view[2]);
		
		double x = 0;
		double y = 0;
		
		int iterations = 0;
		while(x*x + y*y <= 4 && iterations < currentIterations) {
			double temp = x*x - y*y + x0;
			y = 2*x*y + y0;
			x = temp;
			iterations++;
		}
		
		 if(iterations < currentIterations) { 
			 double smooth = (iterations - Math.log(Math.log(Math.sqrt(x*x + y*y))/Math.log(2))/Math.log(2))/currentIterations; //0 to 1
			 int col = Maths.clamp(((int) (smooth * (COLOR_GRADIENT - 1)) + COLOR_OFFSET) % COLOR_GRADIENT, 0, COLOR_GRADIENT-1);
			 
			 return this.colors[col];
		 } else return PackedColor.Black;
	}
	
	public int[] mandelbrot() {
		return IntStream.range(0, GameEngine.displayWidth*GameEngine.displayHeight).parallel().map(val -> mandelbrotPixel(val)).toArray();
	}
 	
	public int[] createColorArray(int n, Color ... colors ) {
		int[] colArray = new int[n];
		if(colors.length == 1) {
			for (int i = 0; i < n; i++) {
				colArray[i] = colors[0].getRGB();	
			}
			return colArray;
		}
		
		double dColor = 1.0 / (colors.length - 1);
		for (int i = 0; i < n; i++) {
			double gr = (double) i / (n - 1);
			int i0 = (int) (gr / dColor);
			int i1 = Math.min(colors.length - 1, i0 + 1);
			double lr = (gr - i0 * dColor) / dColor;
			
			Color c0 = colors[i0];
            int r0 = c0.getRed();
            int g0 = c0.getGreen();
            int b0 = c0.getBlue();
            int a0 = c0.getAlpha();

            Color c1 = colors[i1];
            int r1 = c1.getRed();
            int g1 = c1.getGreen();
            int b1 = c1.getBlue();
            int a1 = c1.getAlpha();

            int dr = r1-r0;
            int dg = g1-g0;
            int db = b1-b0;
            int da = a1-a0;

            int r = (int)(r0 + lr * dr);
            int g = (int)(g0 + lr * dg);
            int b = (int)(b0 + lr * db);
            int a = (int)(a0 + lr * da);
            
            colArray[i] = (a << 24) | (r << 16) | (g << 8) | b;
		}
		return colArray;
	}
	
	public void setView(double x1, double x2, double y1, double y2) {
		this.view[0] = x1;
		this.view[1] = x2;
		this.view[2] = y1;
		this.view[3] = y2;
	}
	
	@Override
	public void init(Graphics2D g) throws Exception {
		// TODO Auto-generated method stub
		currentDelay = DELAY_FRAMES;
		text = new CenteredString[9];
		text[0] = new CenteredString(g, "Fractals", new Rectangle(0, 0, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.BOLD, 100));
		text[1] = new CenteredString(g, "1 - Squares", new Rectangle(0, 250, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[2] = new CenteredString(g, "2 - Lines", new Rectangle(0, 275, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[3] = new CenteredString(g, "3 - Tree", new Rectangle(0, 300, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[4] = new CenteredString(g, "4 - Mandelbrot", new Rectangle(0, 325, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[5] = new CenteredString(g, "5 - Serpinski Carpet", new Rectangle(0, 350, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[6] = new CenteredString(g, "6 - Serpinski Gasket", new Rectangle(0, 375, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[7] = new CenteredString(g, "7 - Cantor Set", new Rectangle(0, 400, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		text[8] = new CenteredString(g, "8 - Koch Snowflake", new Rectangle(0, 425, GameEngine.displayWidth, GameEngine.displayHeight/3), new Font("SansSerif", Font.ITALIC, 15));
		
		this.colors = createColorArray(COLOR_GRADIENT,
				Color.white,
				new Color(255, 204, 0),
				new Color(138, 31, 19),
				new Color(0, 0, 153),
				new Color(0, 102, 255),
				Color.white
		    );
		setView(-2, 1, -1, 1);
		this.mandelbrot = mandelbrot();
	}
	
	private boolean handleGameStateChange(int keyEvent) {
		if(GameEngine.keyboard.keysTyped[keyEvent]) {
			this.gameState = keyEvent - 48;
			GameEngine.keyboard.keysTyped[keyEvent] = false;
			this.currentDelay = DELAY_FRAMES;
			return true;
		}
		return false;
	}
	
	@Override
	public void input() {
		// TODO Auto-generated method stub
		
		//Menu
		if (gameState == 0) {
			for (int i = KeyEvent.VK_1; i <= KeyEvent.VK_8; i++) {
				if (handleGameStateChange(i)) break;
			}
			
		//Escaping
		} else if (gameState > 0) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER])  {
				this.gameState = 0;
				this.currentLayer = 0;
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				
			}
		}
		//Mandelbrot Zoom and Panning with Arrow Keys
		//TODO: implement mouse dragging panning
		if(gameState == 4) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_S]) {
				zoomState = !zoomState;
				GameEngine.keyboard.keysTyped[KeyEvent.VK_S] = false;
			}
			if(!zoomState) {
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
					this.view[0] -= 0.1*zoom;
					this.view[1] -= 0.1*zoom;
				}
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
					this.view[0] += 0.1*zoom;
					this.view[1] += 0.1*zoom;
				}
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
					this.view[2] += 0.1*zoom;
					this.view[3] += 0.1*zoom;
				}
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
					this.view[2] -= 0.1*zoom;
					this.view[3] -= 0.1*zoom;
				}
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Z]) {
					double mX = Maths.map(GameEngine.mouse.position.x, 0, GameEngine.displayWidth-1, this.view[0], this.view[1]);
					double mY = Maths.map(GameEngine.mouse.position.y, 0, GameEngine.displayHeight-1, this.view[3], this.view[2]);
					
					for (int i = 0; i < 2; i++) {
						this.view[i] += 0.1*(mX - this.view[i]);
					}
					for (int i = 2; i < 4; i++) {
						this.view[i] += 0.1*(mY - this.view[i]);
					}
					zoom *= 0.9;
				}
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_X]) {
					double mX = Maths.map(GameEngine.mouse.position.x, 0, GameEngine.displayWidth-1, this.view[0], this.view[1]);
					double mY = Maths.map(GameEngine.mouse.position.y, 0, GameEngine.displayHeight-1, this.view[3], this.view[2]);
					
					for (int i = 0; i < 2; i++) {
						this.view[i] -= 0.1*(mX - this.view[i]);
					}
					for (int i = 2; i < 4; i++) {
						this.view[i] -= 0.1*(mY - this.view[i]);
					}
					zoom *= 1.1;
				}
			} else {
				if(GameEngine.keyboard.keysTyped[KeyEvent.VK_Z]) {
					GameEngine.keyboard.keysTyped[KeyEvent.VK_Z] = false;
					double mX = Maths.map(GameEngine.mouse.position.x, 0, GameEngine.displayWidth-1, this.view[0], this.view[1]);
					double mY = Maths.map(GameEngine.mouse.position.y, 0, GameEngine.displayHeight-1, this.view[3], this.view[2]);
					
					for (int i = 0; i < 2; i++) {
						this.view[i] += 0.2*(mX - this.view[i]);
					}
					for (int i = 2; i < 4; i++) {
						this.view[i] += 0.2*(mY - this.view[i]);
					}
					zoom *= 0.8;
					shouldUpdate = true;
				}
				if(GameEngine.keyboard.keysPressed[KeyEvent.VK_X]) {
					GameEngine.keyboard.keysPressed[KeyEvent.VK_X] = false;
					double mX = Maths.map(GameEngine.mouse.position.x, 0, GameEngine.displayWidth-1, this.view[0], this.view[1]);
					double mY = Maths.map(GameEngine.mouse.position.y, 0, GameEngine.displayHeight-1, this.view[3], this.view[2]);
					
					for (int i = 0; i < 2; i++) {
						this.view[i] -= 0.2*(mX - this.view[i]);
					}
					for (int i = 2; i < 4; i++) {
						this.view[i] -= 0.2*(mY - this.view[i]);
					}
					zoom *= 1.2;
					shouldUpdate = true;
				}
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
		//Go through Layers
		if(gameState == 1 || gameState == 2 || gameState == 5 || gameState == 6 || gameState == 7 || gameState == 8) {
			if (currentDelay == 0) {
				currentDelay = DELAY_FRAMES;
				this.currentLayer++;
				this.currentLayer = this.currentLayer % MAX_LAYERS;
			}
			currentDelay--;
		}
		
		//Go through layers, then change angle
		if(gameState == 3) {
			if(currentLayer == MAX_LAYERS) {
				angleShift += deltaAngle;
			} else {
				if (currentDelay == 0) {
					currentDelay = DELAY_FRAMES;
					this.currentLayer++;
				}
				currentDelay--;
			}
		}
		
		//Adjust allowed max iterations according to zoom for higher level of detail
		if(gameState == 4) {
			if (!zoomState) {
				this.currentIterations = (int) (MIN_ITERATIONS/Math.max(zoom, 0.2));
			} else {
				this.currentIterations = Math.min(MAX_ITERATIONS, (int) (MIN_ITERATIONS/zoom));
			}
		}
 	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
		//Menu Screen
		if (gameState == 0) {
			for (CenteredString cs: text) {
				cs.render(g, Color.white);
			}		
		//Squares
		} else if (gameState == 1) {
			Rectangle r1 = new Rectangle(250, 150, 300, 300);
			drawSquares(g, r1, 0);
		//PerpendicularLines
		} else if (gameState == 2) {
			perpendicularLines(g, 400, 300, 400, 0, 0);
		//Tree
		} else if (gameState == 3) {
			tree(g, GameEngine.displayWidth/2, GameEngine.displayHeight, 100, 90, 0);
		//SerpinskiCarpet
		} else if (gameState == 5) {
			Rectangle r1 = new Rectangle(300, 200, 200, 200);
			serpinskiCarpet(g, r1, 0);
		//SerpinskiGasket (Triangle)
		} else if (gameState == 6) {
			Triangle<Vec2> t = new Triangle<Vec2>(new Vec2(150, 525), new Vec2(650, 525), new Vec2(400, 75));
			serpinskiGasket(g, t, 0);
		//Cantor Set
		} else if(gameState == 7 ) {
			cantorSet(g, 100, 100, 600, 0);
		//KochSnowflake
		} else if(gameState == 8 ) {
			float sL = 400;
			float t = (float) (410 - sL*SIN_60);
			kochSnowflake(g, new Vec2(sL/2, 410), new Vec2(3*sL/2, 410), 0);
			kochSnowflake(g, new Vec2(3*sL/2, 410), new Vec2(sL, t), 0);
			kochSnowflake(g, new Vec2(sL, t), new Vec2(sL/2, 410), 0);
		}
	}

	@Override
	public void render(Graphics3D g) {
		// TODO Auto-generated method stub
		
		//Mandelbrot (Need to access pixels)
		if(gameState == 4) {
			
			if(!zoomState || shouldUpdate) {
				this.mandelbrot = mandelbrot();
				shouldUpdate = false;
			}
			g.drawPixels(this.mandelbrot);
		}
		
	}

}
