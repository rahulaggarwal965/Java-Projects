package gameEngine;

import java.awt.Graphics;
import java.awt.Graphics2D;

import threeDimensions.Graphics3D;

public interface IGameLogic {
	void init(Graphics g) throws Exception;
	
	void input();
	
	void update(float interval);
	
	void render(Graphics2D g2d);
	
	void render3D(Graphics3D g3d);
}
