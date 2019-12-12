package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import gameEngine.GameEngine;
import libraries.Maths;
import threeDimensions.PackedColor;
import threeDimensions.Vec4;

public class Brick extends GameObject {
	
	private int level;
	private boolean alive = true;
	
	private static final int GAP = 5;
	//public static final int brickWidth = 80, brickHeight = 40;
	//private static final Color[] colors = {Color.red, Color.orange, Color.yellow, Color.cyan, Color.green};
	
	public Brick(int x, int y, int width, int height, Color c, int level, boolean alive) {
		super(x, y, width, height, c);
		this.id = 2;
		this.level = level;
		this.alive = alive;

	}
	
	public static Brick[] generate(int rows, int cols, double random) {
		Brick[] bricks = new Brick[rows * cols];
		Random rand = new Random();
		
		//Creating a random gradient (need to optimize all this color sh*t)
		float hue = (float) Math.random();
		Vec4 initialColor = PackedColor.toVector(new Color(
				Color.HSBtoRGB(
						hue,
						0.8f + (float) (Math.random() * 0.05),
						0.85f + (float) (Math.random() * 0.1))));
		Vec4 endColor = PackedColor.toVector(new Color(
				Color.HSBtoRGB(hue + ((float) rand.nextInt(2) * 2 - 1)*0.2f + (float) (Maths.randomBilateral() * 0.02), 
						0.5f + (float) (Math.random() * 0.05), 
						0.85f + (float) (Math.random() * 0.1))));
		Vec4 deltaColor = endColor._subtract(initialColor)._divide(rows);
		
		int brickWidth = (int) ((double) GameEngine.displayWidth - (cols - 1) * GAP)/cols;
		int brickHeight = (int) ((double) 200 - (rows - 1) * GAP)/rows;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				bricks[i + j*cols] = new Brick(i*(brickWidth + GAP), j*(brickHeight + GAP), brickWidth, brickHeight, new Color(PackedColor.makeRGBA(initialColor)), j, Math.random() > random);
			}
			initialColor.add(deltaColor);
		}
		return bricks;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub
		
	}
	
	public void update() {
		
	}
	
	public void render(Graphics2D g2d) {
		if(this.alive) {
			g2d.setColor(this.c);
			g2d.fillRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
			//g2d.setColor(Color.white);
			//g2d.setStroke(new BasicStroke(2));
			//g2d.drawRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
		}
	}


}
