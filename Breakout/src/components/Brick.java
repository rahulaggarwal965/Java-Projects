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
		int startHue = rand.nextInt(361);
		int endHue = startHue + (rand.nextInt(2) * 2 - 1)*72 + (int) (Maths.randomBilateral() * 8);
		Vec4 initialColor = PackedColor.toVector(new Color(PackedColor.randomHSB(startHue, startHue, 80, 85, 85, 95)));
		Vec4 endColor = PackedColor.toVector(new Color(PackedColor.randomHSB(endHue, endHue, 50, 55, 85, 95)));
		Vec4 deltaColor = endColor._subtract(initialColor)._divide(rows);
		
		int brickWidth = (GameEngine.displayWidth - (cols - 1) * GAP)/cols;
		int brickHeight = (300 - (rows - 1) * GAP)/rows;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				bricks[i + j*cols] = new Brick(i*(brickWidth + GAP), j*(brickHeight + GAP), brickWidth, brickHeight, new Color(PackedColor.makeRGBA(initialColor)), rows - 1 - j, Math.random() > random);
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
