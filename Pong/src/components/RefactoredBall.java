package components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import gameEngine.GameEngine;
import libraries.Maths;

public class RefactoredBall extends PongMovable {
	private int radius, speed;
	
	public RefactoredBall(int dx, int dy, int x, int y, Color col, int radius, int speed) {
		super(dx, dy, x, y, col);
		// TODO Auto-generated constructor stub
		this.radius = radius;
		this.speed = speed;
	}
	
	public RefactoredBall() {
		super();
		this.radius = 0;
		this.speed = 0;
	}
	
	public RefactoredBall(Color col, int radius, int speed) {
		super(col);
		this.radius = radius;
		this.speed = speed;
	}
	
	@Override
	public void input() {};
	
	@Override
	public void init() {
		this.x = GameEngine.displayWidth/2;
		this.y = GameEngine.displayHeight/2;
		Random rand = new Random();
		this.dy = (rand.nextInt(this.speed) + 1) * (2 * rand.nextInt(2) - 1);
		this.dx = (2* rand.nextInt(2) - 1) * this.speed;
	}
	
	@Override
	public void update() {
		this.translate(this.dx, this.dy);
		if(this.y - this.radius <= 0 || this.y + this.radius >= GameEngine.displayHeight) {
			this.dy *= -1;
		}
	}
	
	public void collision(Paddle p) {
		int cType = Maths.sideRectangleCircleIntersection(p.getX(), p.getY(), p.getWidth(), p.getHeight(), this.x, this.y, this.radius, this.dx, this.dy);
		if (cType == 0 || cType == 2) {
			this.translate(-this.dx, -this.dy);
			this.dx *= -1;
			if(p.getDy() * this.dy < 0) {
				this.dx *= 2;
			}
		} else if(cType == 1 || cType == 3) {
			this.translate(-this.dx, -this.dy);
			this.dy *= -1;
		}
	}
	
	public boolean touchingEdge(boolean side) {
		if (!side) {
			if (this.x - this.radius <= 0) {
				this.init();
				return true;
			}
		} else {
			if (this.x + this.radius >= GameEngine.displayWidth) {
				this.init();
				return true;
			}
		}
		return false;
	}

	@Override
	public void render(Graphics g) {
		g.setColor(this.col);
		g.fillOval(this.x - this.radius, this.y - this.radius, this.radius*2, this.radius*2);
	}
	
}
