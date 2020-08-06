package components;

import java.awt.Graphics2D;

import threeDimensions.Graphics3D;

public class Point implements GameObject {
	
	private float x, y;
	private int color;
	
	public Point(float x, float y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}

	@Override
	public void init() {
	}

	@Override
	public void input() {
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void render(Graphics2D g) {
	}

	@Override
	public void render(Graphics3D g) {
		g.fillRectangle(this.x, this.y, 2, 2, this.color);
	}


}
