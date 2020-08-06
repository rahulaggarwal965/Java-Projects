package monteCarloSimulation;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import components.Point;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class MonteCarloSimulation implements GameLogic{
	
	private MonteCarloGUI gui = new MonteCarloGUI();
	private ArrayList<Point> points = new ArrayList<Point>();
	
	private int gameState = 0;
	private boolean paused = false;
	
	private float pointInterval = 0.3f;
	private float pointCooldown = 0f;
	
	private int in;
	private int total;

	public static void main(String[] args) {
		MonteCarloSimulation mc = new MonteCarloSimulation();
		GameEngine g = new GameEngine(60, 60, 3, mc, 1280, 1000, "MonteCarloSimulationb");
	}
	
	@Override
	public void init(Graphics2D g) throws Exception {
	}

	@Override
	public void input() {
		if(gameState == 0) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				
				//Setup new game
				this.points.clear();
				this.in = 0;
				this.total = 0;
				gameState = 1;
			}
		} else if(gameState == 1) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_B]) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_B] = false;
				gameState = 0;
			}
		} 
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE]) {
			this.paused = !this.paused;
			GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE] = false;
		}
	}

	@Override
	public void update(float deltaTime) {
		if(gameState == 1 && !this.paused) {
			this.pointCooldown -= deltaTime;
			if(this.pointCooldown <= 0) {
				this.pointCooldown = pointInterval;
				for (int i = 0; i < 100; i++) {
					float x = Maths.random(GameEngine.displayWidth - 1);
					float y = Maths.random(GameEngine.displayHeight - 1);
					int color = PackedColor.White;
					if(Maths.distance(x, y, GameEngine.displayWidth/2, GameEngine.displayHeight/2) <= 200) {
						in++;
						total++;
						color = PackedColor.Red;
					} else {
						total++;
					}
					this.points.add(new Point(x, y, color));
;				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		this.gui.render(this.gameState, g);
		if(gameState == 1) {
			MonteCarloGUI.drawCenteredString(g, String.valueOf(this.in), MonteCarloGUI.labelFont, 50, 60);
			MonteCarloGUI.drawCenteredString(g, String.valueOf(this.total), MonteCarloGUI.labelFont, 50, 100);
			g.drawLine(30, 80, 70, 80);
			g.setFont(MonteCarloGUI.labelFont);
			g.drawString("= " + String.valueOf(((double) this.in)/this.total * GameEngine.displayWidth * GameEngine.displayHeight), 90, 90);
		}
		
	}

	@Override
	public void render(Graphics3D g) {
		if(gameState == 1) {
			for (Point p : this.points) {
				p.render(g);
			}
		}
	}

}
