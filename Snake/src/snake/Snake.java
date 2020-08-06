package snake;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import components.Bomb;
import components.Snek;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;
import threeDimensions.Texture;
import threeDimensions.TextureAtlas;

public class Snake implements GameLogic {
	
	public Texture flameTexture = Texture.loadImage(getClass().getResource("/images/flame.png")).resize(4.0, 4.0);
	
	public TextureAtlas flame;
	private int flamePosition = 0;
	
	private float snakeCooldown = 0;
	private final float snakeInterval = 0.065f;
	
	private Point food;
	private Bomb bomb;
	
	private boolean paused = false;
	private int gameState = 0;
	
	private SnakeGUI snakegui = new SnakeGUI();
	private Snek snek;
	private int rows, cols;
	
	public static void main(String[] args) {
		Snake snake = new Snake();
		GameEngine g = new GameEngine(60, 60, 3, snake, 1280, 720, "Snake");
	}

	public Snake() {
		this.flame = TextureAtlas.loadTextureAtlas(flameTexture, 10, 6, 60);
	}

	@Override
	public void init(Graphics2D g) throws Exception {
		SnakeSound.init();
		SnakeSound.BACKGROUND.setVolume(2.0f);
		SnakeSound.BACKGROUND.play(true);
		this.snek = new Snek(0);
		snakegui.init();
		snek.init();
		this.rows = GameEngine.displayHeight/Snek.SIZE;
		this.cols = GameEngine.displayWidth/Snek.SIZE;
	}

	@Override
	public void input() {
		if(gameState == 0) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				
				//Setup new game
				snek.init();
				this.snakegui.setLength(0);
				gameState = 1;
			}
		} else if(gameState == 1) {
			snek.input();
		} else if(gameState == 2) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
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
		if(!this.paused && gameState == 0) {
			this.flamePosition++;
			if(this.flamePosition == 60) {
				this.flamePosition = 0;
			}	
		} else if(!this.paused && gameState == 1) {
			this.snakeCooldown -= deltaTime;
			
			if(this.food == null) this.spawnFood();
			if(this.bomb != null) {
				this.bomb.update(deltaTime);
				if(this.bomb.getLife() <= 0) this.bomb = null;
			}
			if(this.bomb == null) {
				this.spawnBomb();
			}
			
			if(this.snakeCooldown <= 0) {
				this.snakeCooldown = this.snakeInterval;
				snek.update(deltaTime);
				if(snek.checkDeathCollisions() || snek.getHeadPosition().equals(this.bomb.getPosition())) {
					SnakeSound.GAMEOVER.play(false);
					this.food = null;
					this.bomb = null;
					this.gameState = 2;				
				}
				if(snek.getHeadPosition().equals(this.food)) {
					SnakeSound.HIT.play(false);
					snek.setGrowth(5);
					this.snakegui.setLength(this.snakegui.getLength() + 5);
					this.food = null;
				}
			}
		}
	}
	
	public void spawnFood() {
		Point p = new Point(((int) Maths.random(this.cols) + 1) * Snek.SIZE - Snek.HALF_SIZE, ((int) Maths.random(this.rows) + 1) * Snek.SIZE - Snek.HALF_SIZE);
		this.food = p;
	}
	
	public void spawnBomb() {
		Bomb b = new Bomb(((int) Maths.random(this.cols) + 1) * Snek.SIZE - Snek.HALF_SIZE, ((int) Maths.random(this.rows) + 1) * Snek.SIZE - Snek.HALF_SIZE);
		this.bomb = b;
	}

	@Override
	public void render(Graphics2D g) {
		snakegui.render(gameState, g);
	}

	@Override
	public void render(Graphics3D g) {
		if(gameState == 0) {
			g.drawTexture(this.flame.getTexture(this.flamePosition), GameEngine.displayWidth/2 - 130, GameEngine.displayHeight/2 - 160);
		} else if(gameState == 1) {
			if(this.food != null) {
				g.fillRectangle(this.food.x - Snek.HALF_SIZE + 1, this.food.y - Snek.HALF_SIZE + 1, Snek.SIZE - 2, Snek.SIZE - 2, PackedColor.Yellow);
			}
			if(this.bomb != null) {
				this.bomb.render(g);
			}
			snek.render(g);
		}
	}

}
