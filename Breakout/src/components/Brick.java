package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import gameEngine.GameEngine;

public class Brick extends GameObject {
	
	private int level;
	private boolean alive = true;
	
	public static final int brickWidth = 80, brickHeight = 40;
	private static final Color[] colors = {Color.red, Color.orange, Color.yellow, Color.cyan, Color.green};
	
	public Brick(int x, int y, int width, int height, int level, boolean alive) {
		super(x, y, width, height, colors[level]);
		this.id = 2;
		this.level = level;
		this.alive = alive;

	}
	
	public static Brick[] generate(int rows, int cols, double random) {
		Brick[] bricks = new Brick[rows * cols];
		int gap = (GameEngine.displayWidth - cols*brickWidth)/(cols + 1);
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				bricks[i + j*cols] = new Brick(gap + i*(brickWidth + gap), gap + j*(brickHeight + gap), brickWidth, brickHeight, j, Math.random() > random);
			}
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
			g2d.setColor(Color.white);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRect((int) this.position.x, (int) this.position.y, (int) this.size.x, (int) this.size.y);
		}
	}


}
