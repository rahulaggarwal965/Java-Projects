package shooter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import components.Boss;
import components.CollisionResolver;
import components.EndlessTerrain;
import components.Enemy;
import components.EnemyFactory;
import components.Missile;
import components.Player;
import components.TerrainGenerator;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import shaders.Shader;
import shaders.TextureTintShader;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Texture;
import threeDimensions.Vec3;
import threeDimensions.Vec4;

public class Shooter implements GameLogic {

	private static final float ASPECT_RATIO = 1.777777f; // 16:9
	private static final float FOV = 95.0f;

	private Pipeline pipeline;
	private Shader shader;
	private Matrix projection;
	
	private ShooterGUI gui;
	private ParticleSystem ps;
	private EnemyFactory ef;
	private CollisionResolver cr;
	private int gameState = 0;
	private boolean paused = false;
	private int level = 0;
	
	public final Texture pTexture = Texture.loadImage(getClass().getResource("/images/particle1.png"));
	private TextureTintShader pShader;
	
	private Player player;
	private Boss boss = null;
	private EndlessTerrain e;
	private TerrainGenerator t;

	//test
	
	public void test(int mouseX, int mouseY, Matrix viewMatrix, Matrix projectionMatrix, Vec3 rayOrigin, Vec3 rayDirection) {
		Vec4 rayStart = new Vec4(
				((float) mouseX / GameEngine.displayWidth - 0.5f) * 2.0f,
				((float) mouseY / GameEngine.displayHeight - 0.5f) * -2.0f,
				0.0f,
				1.0f
				);
		Vec4 rayEnd = new Vec4(
				((float) mouseX / GameEngine.displayWidth - 0.5f) * 2.0f,
				((float) mouseY / GameEngine.displayHeight - 0.5f) * -2.0f,
				1.0f,
				1.0f
				);
		
		Matrix projectionInverse = projectionMatrix.inverse();
		Matrix viewInverse = viewMatrix.inverse();
		
		Vec4 cs = projectionInverse._multiply(rayStart); cs.divide(cs.w);
		Vec4 worlds = viewInverse._multiply(cs); worlds.divide(worlds.w);
		Vec4 ce = projectionInverse._multiply(rayEnd); ce.divide(ce.w);
		Vec4 worle = viewInverse._multiply(ce); worle.divide(worle.w);
		
		rayOrigin.set(worlds);
		rayDirection.set(worle._subtract(worlds).getNormalized());
		
		
	}
	
	public static void main(String[] args) {
		Shooter shooter = new Shooter();
		GameEngine e = new GameEngine(60, 60, 3, shooter, 1280, 720, "Shooter");
	}

	@Override
	public void init(Graphics2D g) throws Exception {
		
		ShooterSound.init();
		ShooterSound.BACKGROUND.play(true);
		ShooterSound.THRUSTERS.play(true);
		ShooterSound.WIND.play(true);
		ShooterSound.WIND.setVolume(0f);
		ShooterSound.THRUSTERS.setVolume(0f);
		
		//Initialize Systems
		this.gui = new ShooterGUI();
		this.ps = new ParticleSystem(100);
		this.cr = new CollisionResolver(this, ps);
		
		
		//Initialize Player
		this.player = new Player(ps);
		this.player.init();
		
		//Test Enemy
		this.ef = new EnemyFactory(player, ps, 10);
		//this.enemy = new Enemy(ps, player);
		//this.enemy.init();
		
		//Initialize Shaders and Pipeline
		this.projection = Matrix.ProjectionFOV(4, FOV, ASPECT_RATIO, 0.05f, 1000f);
		this.shader = new Shader();
		this.pShader = new TextureTintShader();
		this.pShader.setTexture(pTexture);
		this.pipeline = new Pipeline(shader);
		
		//Initialize Endless Terrain
		this.t = new TerrainGenerator();
		this.e = new EndlessTerrain(this.player.getPosition(), t, pipeline, shader);
	}

	@Override
	public void input() {
		if(this.gameState == 0) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				
				//Setup new game
				this.level = 0;
				this.newLevel();
				this.gameState = 1;
				this.player.setHealth(Player.maxHealth);
				ShooterSound.BACKGROUND.stop();
				ShooterSound.THRUSTERS.setVolume(0.6f);
				ShooterSound.WIND.setVolume(0.7f);
			}
		} else if(this.gameState == 1) {
			this.player.input();
		} else if(gameState == 2) {
			if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] || GameEngine.mouse.mouseClicked) {
				GameEngine.keyboard.keysTyped[KeyEvent.VK_ENTER] = false;
				GameEngine.mouse.mouseClicked = false;
				gameState = 0;
			}
		}
		if (GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE]) {
			this.paused = !this.paused;
			GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE] = false;
		}
	}
	
	public void newLevel() {
		this.level++;
		this.player.init();
		this.ps.clear();
		this.ef.clear();
		int numEnemies = (int) Maths.random(this.level) + 1;
		for (int i = 0; i < numEnemies; i++) {
			Vec3 randPosition = this.player.getPosition()._add(Vec3.random(200.0f, 50.0f, 200.0f));
			Vec3 randRotation = Vec3.random(Maths.PI2);
			this.ef.addEnemy(randPosition, randRotation);
		}
	}
	
	public void createBoss(Vec3 position) {
		this.boss = new Boss(ps, player);
		this.boss.init();
		this.boss.setPosition(position.x, position.y, position.z);
	}

	@Override
	public void update(float deltaTime) {
		if (gameState == 1 && !paused) {
			
			//Get Enemies
			Enemy[] enemies = this.ef.getEnemyPool();
			int enemyCount = this.ef.getEnemyCount();
			
			if(enemyCount == 0 && this.boss == null) this.createBoss(this.player.getPosition()._add(Vec3.random(200.0f, 50.0f, 200.0f)));
			if(this.player.getHealth() <= 0) {
				this.boss = null;
				this.gameState = 2;
				ShooterSound.GAMEOVER.play(false);
				ShooterSound.THRUSTERS.setVolume(0f);
				ShooterSound.WIND.setVolume(0f);
				ShooterSound.BACKGROUND.play(true);
				ShooterSound.HIT.setVolume(0.8f);
			}
			//Update Player
			this.player.update(deltaTime);
			
			//Get Player Missiles
			Missile[] playerMissiles = this.player.getMissiles();
			int playerMissileCount = this.player.getMissileCount();
			
			
			if(boss != null) {
				
				//Update Boss
				this.boss.update(deltaTime);
				
				Missile[] bossMissiles = this.boss.getMissiles();
				int bossMissileCount = this.boss.getMissileCount();
				
				for (int i = 0; i < playerMissileCount; i++) {
					playerMissiles[i].setTargetPosition(boss.getPosition());
					//Resolve Missile/Enemy Collision
					this.cr.resolveCollision(playerMissiles[i], boss);
				}
				
				for(int i = 0; i < bossMissileCount; i++) {
					bossMissiles[i].setTargetPosition(player.getPosition());
					this.cr.resolveCollision(bossMissiles[i], player);
				}
				
				this.cr.resolveCollision(player, boss);
				if(boss.getHealth() <= 0) {
					this.boss = null;
					this.newLevel();
				}
			} else {
				//Update Enemies
				ef.update(deltaTime);
				
				//Set Player Missile Target Vectors
				for (int i = 0; i < playerMissileCount; i++) {
					float minDist = Float.MAX_VALUE;
					int target = 0;
					Vec3 missilePosition = playerMissiles[i].getPosition();
					for (int j = 0; j < enemyCount; j++) {
						float dist = enemies[j].getPosition()._subtract(missilePosition).magSq();
						if(dist < minDist) minDist = dist; target = j;
					}
					playerMissiles[i].setTargetPosition(enemies[target].getPosition());
					//Resolve Missile/Enemy Collision
					this.cr.resolveCollision(playerMissiles[i], enemies[target]);
				}
				
				//Resolve Collisions
				for (int i = 0; i < enemyCount; i++) {
					this.cr.resolveCollision(this.player, enemies[i]);
				}	
			}
			
			e.update(deltaTime);
			this.ps.update(deltaTime);
			
		}
	}

	@Override
	public void render(Graphics2D g) {
		this.gui.render(gameState, this.level, g);
		if(gameState == 1) {
			float a = this.player.getHealth() / Player.maxHealth;
			g.setColor(Color.white);
			g.drawRect(10, 700, 200, 10);
			g.setColor(Color.green);
			g.fillRect(10, 700, (int) (a * 200), 10);
		}
	}

	@Override
	public void render(Graphics3D g) {
		if(gameState == 1) {
			
			//Prepare
			pipeline.beginFrame();
			
			//Get View Matrix
			Matrix view = this.player.getCamera().getViewMatrix();
			
			//Draw Line Mode and RGB
			this.pipeline.setShader(shader);
			this.pipeline.setDepthTest(true);
			this.pipeline.setDrawMode(Pipeline.LINE_MODE);
			this.pipeline.setColorMode(Pipeline.RGB);
			this.shader.setView(view);
			this.shader.setProjection(this.projection);
			
			//Draw Endless Terrain
			this.shader.setDefaultColor(PackedColor.Gray);
			e.render(g);
			
			//Draw Player
			this.shader.setDefaultColor(PackedColor.White);
			this.player.render(g, pipeline);
			
			//Draw Enemies
			this.ef.render(g, pipeline);
			if(this.boss != null) {
				this.boss.render(g, pipeline);
			}
			
			//Draw Triangle Mode and ADDITIVE
			this.pipeline.setShader(this.pShader);
			this.pipeline.setDepthTest(false);
			this.pipeline.setDrawMode(Pipeline.TRIANGLE_MODE);
			this.pipeline.setColorMode(Pipeline.ADDITIVE);
			this.pShader.setView(view);
			this.pShader.setProjection(this.projection);

			//Draw Particles
			this.pShader.setDefaultColor(PackedColor.makeRGBA(240, 140, 53, 255));
			this.ps.render(g, pipeline);
			

		}
	}

}
