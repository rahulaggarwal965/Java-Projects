package components;

import java.awt.Color;
import java.awt.Graphics;

public abstract class PongMovable {
	protected int dx, dy, x, y;
	
	protected Color col;
	
	public PongMovable(int dx, int dy, int x, int y, Color col) {
		this.dx = dx;
		this.dy = dy;
		this.x = x;
		this.y = y;
		this.col = col;
	}
	
	public PongMovable() {
		this(0, 0, 0, 0, Color.cyan);
	}
	
	public PongMovable(Color col) {
		this(0, 0, 0, 0, col);
	}
	
	public abstract void init();
	
	public abstract void input();
	
	public abstract void update();
	
	public abstract void render(Graphics g);

	public void translateX(int dx) {
		this.x += dx;
	}
	
	public void translateY(int dy) {
		this.y += dy;
	}
	
	public void translate(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getCol() {
		return col;
	}

	public void setCol(Color col) {
		this.col = col;
	}
	
}
