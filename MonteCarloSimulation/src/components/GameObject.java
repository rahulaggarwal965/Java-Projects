package components;

import java.awt.Graphics2D;

import threeDimensions.Graphics3D;

public interface GameObject {
	
	public void init();
	
	public void input();
	
	public void update(float deltaTime);
	
	public void render(Graphics2D g);
	
	public void render(Graphics3D g);
	
}
