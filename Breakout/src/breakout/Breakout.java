package breakout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

import components.Ball;
import components.Brick;
import components.CollisionResolver;
import components.Paddle;
import components.ParticleSystem;
import components.PowerUpSystem;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class Breakout implements GameLogic {
	
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
	private int brickCount = 0;
	private int score = 0;
	
	private Color backgroundColor = Color.black;
	
	public static void main(String[] args) {
		
		Breakout breakout = new Breakout();
		GameEngine gameEngine = new GameEngine(60, 60, 3, breakout, 800, 800, "Breakout");
	}

	@Override
	public void init(Graphics2D g) throws Exception {
		// TODO Auto-generated method stub
		
		
		//Initialize Sound, Play Background Music Loop, Normalize Volume Levels
		BreakoutSound.init();
		BreakoutSound.BACKGROUND.play(true);
		BreakoutSound.EXPLOSION.setVolume(0.8f);
		
		//Initialize the Game objects
		this.paddle = new Paddle(400, 760, Paddle.initialWidth, 15, Color.white);
		
		//Create the Game Components
		this.ps = new ParticleSystem(3000);
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
				this.score = 0;
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
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		if(gameState == 1 && !paused) {
			
			//Updating GameObjects
			this.paddle.update(deltaTime);
			for(Ball bl: this.balls) {
				bl.update(deltaTime);
			}
			
			//Updating Particles
			ps.update(deltaTime);
			
			//Update PowerUpSystem
			pus.update(deltaTime);
			
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
			
			if(this.brickCount <= 0) this.newLevel();
			
		}
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
		g.setBackground(backgroundColor);
		g.clearRect(0, 0, GameEngine.displayWidth, GameEngine.displayHeight);
		
		//Render GUI elements
		this.gui.render(this.gameState, this.score, g);

		//Render GameObjects
		if(gameState == 1) {
			
			//Render Particles
			ps.render(g);
			
			for (Brick b : this.bricks) {
				b.render(g);
			}
			this.paddle.render(g);
			for (Ball bl : this.balls) {
				bl.render(g);
			}
			
			//Render PowerUps
			pus.render(g);
		}
	}

	@Override
	public void render(Graphics3D g) {
		
	}
	
	public void newLevel() {
		this.aliveBalls = 1;
		this.brickCount = 0;
		ps.clear();
		
		//Any Hue, High Saturation (75-100), Low Brightness (0 - 30)
		this.backgroundColor = new Color(PackedColor.randomHSB(0, 360, 75, 100, 0, 30));
		
		//Generate Level
		Random rand = new Random();
		int rows = 5 + rand.nextInt(6);
		int cols = 8 + rand.nextInt(8);
		this.bricks = Brick.generate(rows, cols, Math.random()/2);
		
		//Set BrickCount
		for (Brick b: this.bricks) {
			if(b.isAlive()) this.brickCount++;
		}
		
		//Make Ball
		this.balls = new Ball[1];
		this.balls[0] = new Ball(GameEngine.displayWidth/2, 650, 15, 15, Color.white, this.ps);
		this.balls[0].init();
	}
	
	public void handlePowerUp(int type) {
		if(type == 0) { //Paddle Width
			this.paddle.setWidth(this.paddle.getWidth() * 2);
			this.paddle.setPowerUpTimer(4);
		} else if(type == 1) { //Paddle Speed
			this.paddle.setActiveSpeed(Paddle.fastSpeed);
			this.paddle.setPowerUpTimer(4);
		} else if(type == 2) { //Multiple Balls
			Ball[] newBalls = new Ball[10];
			for (int i = 0, j = 0; i < this.aliveBalls; j++) {
				if(this.balls[j].isAlive()) {
					newBalls[i] = this.balls[j];
					i++;
				}
			}
			for (int i = this.aliveBalls; i < 10; i++) {
				newBalls[i] = new Ball(GameEngine.displayWidth/2, 650, 15, 15, Color.white, this.ps);
				newBalls[i].init();
				newBalls[i].setX((float) Math.random() * (GameEngine.displayWidth - 50) + 25);
			}
			this.balls = newBalls;
			this.aliveBalls = 10;
		} else if(type == 3) { //Piercing Ball
			for (int i = 0; i < this.balls.length; i++) {
				if(this.balls[i].isAlive()) {
					this.balls[i].setPiercingTime(3.0);
				}
			}
		}
	}
	
	public int getBrickCount() {
		return brickCount;
	}

	public void setBrickCount(int brickCount) {
		this.brickCount = brickCount;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
