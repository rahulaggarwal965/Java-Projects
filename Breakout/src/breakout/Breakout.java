package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import components.Ball;
import components.Brick;
import components.CollisionResolver;
import components.Paddle;
import components.ParticleSystem;
import components.PowerUpSystem;
import gameEngine.GameEngine;
import gameEngine.IGameLogic;
import libraries.Noise;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class Breakout implements IGameLogic {
	
	private ParticleSystem ps;
	private CollisionResolver cr;
	private BreakoutGUI gui;
	private PowerUpSystem pus;
	
	private Brick[] bricks;
	private Paddle paddle;
	private Ball[] balls;
	private int aliveBalls = 1;
	
	private int gameState = 0;
	private boolean paused = false;
	private int brickCount;
	
	public static void main(String[] args) {
		
		Breakout breakout = new Breakout();
		GameEngine gameEngine = new GameEngine(60, 60, 3, breakout, 800, 800, "Breakout", true);
	}

	@Override
	public void init(Graphics g) throws Exception {
		// TODO Auto-generated method stub
		
		
		//Initialize Sound, Play Background Music Loop, Normalize Volume Levels
		BreakoutSound.init();
		BreakoutSound.BACKGROUND.play(true);
		BreakoutSound.EXPLOSION.setVolume(0.8f);
		
		//Initialize the Game objects
		this.paddle = new Paddle(400, 760, Paddle.initialWidth, 15, Color.white);
		
		//Create the Game Components
		this.ps = new ParticleSystem(1000);
		this.cr = new CollisionResolver(this, ps);
		this.gui = new BreakoutGUI();
		this.pus = new PowerUpSystem();
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub
		if(gameState == 0) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				
				//Setup new game
				this.newLevel();
				paddle.init();
				gameState = 1;
			}
		} else if(gameState == 1) {
			this.paddle.input();
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_SPACE]) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_SPACE] = false;
				this.paused = !this.paused;
			}
		} else if(gameState == 2) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				gameState = 0;
			}
		}
	}

	@Override
	public void update(float interval) {
		// TODO Auto-generated method stub
		if(gameState == 1 && !paused) {
			
			//Updating GameObjects
			this.paddle.update(interval);
			for(Ball bl: this.balls) {
				bl.update(interval);
			}
			
			//Updating Particles
			ps.update(interval);
			
			//Update PowerUpSystem
			pus.update(interval);
			
			//Resolve All Collisions
			for (Ball bl : this.balls) {
				cr.resolveCollision(this.paddle, bl);
				for (Brick b : this.bricks) {
					cr.resolveCollision(bl, b);
				}
			}
			cr.resolveCollision(this.paddle, this.pus.getActivePowerUp());
			
			
			
			//Ball Death
			for(Ball bl : this.balls) {
				if(bl.isOffBottomEdge() && bl.isAlive()) {
					if(this.aliveBalls == 1) {
						this.paddle.loseLife();
						if(this.paddle.isDead()) {
							BreakoutSound.GAMEOVER.play(false);
							this.gameState = 2;
						} else {
							bl.init();
						}
					} else {
						this.aliveBalls--;
						bl.setAlive(false);
					}
				}
			}
			
			if(this.brickCount == 0) this.newLevel();
			
		}
	}

	@Override
	public void render(Graphics2D g2d) {
		// TODO Auto-generated method stub
		
		//Render GUI elements
		this.gui.render(this.gameState, g2d);

		//Render GameObjects
		if(gameState == 1) {
			
			//Render Particles
			ps.render(g2d);
			
			for (Brick b : this.bricks) {
				b.render(g2d);
			}
			this.paddle.render(g2d);
			for (Ball bl : this.balls) {
				bl.render(g2d);
			}
			
			//Render PowerUps
			pus.render(g2d);
		}
	}

	@Override
	public void render3D(Graphics3D g3d) {
		
	}
	
	public void newLevel() {
		this.aliveBalls = 1;
		this.bricks = Brick.generate(5, 10);
		this.brickCount = 5 * 10;
		this.balls = new Ball[1];
		this.balls[0] = new Ball(GameEngine.displayWidth/2, 650, 15, 15, Color.white);
		this.balls[0].init();
	}

	public int getBrickCount() {
		return brickCount;
	}

	public void setBrickCount(int brickCount) {
		this.brickCount = brickCount;
	}
	
	public void handlePowerUp(int type) {
		if(type == 0) {
			this.paddle.setWidth(this.paddle.getWidth() * 2);
			this.paddle.setPowerUpTimer(4);
		} else if(type == 1) {
			this.paddle.setActiveSpeed(Paddle.fastSpeed);
			this.paddle.setPowerUpTimer(4);
		} else if(type == 2) {
			this.aliveBalls = 10;
			Ball bl = this.balls[0];
			this.balls = new Ball[10];
			this.balls[0] = bl;
			for (int i = 1; i < 10; i++) {
				this.balls[i] = new Ball(GameEngine.displayWidth/2, 650, 15, 15, Color.white);
				this.balls[i].init();
				this.balls[i].setX((float) Math.random() * (GameEngine.displayWidth - 50) + 25); 
			}
		}
	}
}
