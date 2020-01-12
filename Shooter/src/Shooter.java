import java.awt.Color;
import java.awt.Graphics2D;

import gameEngine.GameEngine;
import gameEngine.GameLogic;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class Shooter implements GameLogic{
	
	public static void main(String[] args) {
		Shooter shooter = new Shooter();
		GameEngine e = new GameEngine(60, 60, 3, shooter, 800, 800, "Shooter");
	}

	public Shooter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(Graphics2D g) throws Exception {
	}

	@Override
	public void input() {
	}

	@Override
	public void update(float deltaTime) {
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.white);
		g.drawOval(200, 200, 50, 50);
	}

	@Override
	public void render(Graphics3D g) {
		g.drawCircle(200, 200, 50, PackedColor.White);
	}


	

}
