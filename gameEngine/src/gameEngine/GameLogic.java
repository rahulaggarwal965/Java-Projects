package gameEngine;

import java.awt.Graphics2D;

import threeDimensions.Graphics3D;

public interface GameLogic {
	void init(Graphics2D g) throws Exception;
	
	void input();
	
	void update(float deltaTime);
	
	void render(Graphics2D g);
	
	void render(Graphics3D g);

}
