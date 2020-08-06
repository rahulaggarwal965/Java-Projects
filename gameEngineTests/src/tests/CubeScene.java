package tests;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import shaders.Shader;
import shaders.TextureTintShader;
import threeDimensions.Camera;
import threeDimensions.GameObject;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.Mesh;
import threeDimensions.PackedColor;
import threeDimensions.ParticleSystem;
import threeDimensions.Pipeline;
import threeDimensions.Primitive;
import threeDimensions.Texture;
import threeDimensions.Vec3;


public class CubeScene implements GameLogic {
	
	private static final float ASPECT_RATIO = 1.777777f; //16:9
	private static final float FOV = 95.0f;
	private static final float CAMERA_SPEED = 1.0f;
	
	private boolean paused = false;
	
	private Pipeline pipeline;
	private TextureTintShader s;
	private Shader s1;
	private Camera camera;
	private ParticleSystem ps;
	private final Texture pTexture = Texture.loadImageFile("/Users/infinity/Desktop/particle1.png");
	private Vec3 particleInitialPosition = new Vec3(0, -0.5f, 2);
	private Vec3 particleInitialRotation = new Vec3(0, 0, 0);
	
	//private Skybox sb;
	GameObject cylinder;
	Mesh pQuad = new Mesh(new int[] {0, 1, 3, 1, 2, 3}, new int[] {3, 2}, new float[] {
			-1.0f, -1.0f, 0f,
			-1.0f, 1.0f, 0f,
			1.0f, 1.0f, 0f,
			1.0f, -1.0f, 0f}, new float[] {
					0, 1,
					0, 0,
					1, 0,
					1, 1
			}
	);
	private Matrix projection;
	
	private final float pInterval = 0.5f;
	private float pCooldown = 0f;
	
	public static void main(String[] args) {
		CubeScene cs = new CubeScene();
		GameEngine gameEngine = new GameEngine(60, 60, 3, cs, 1280, 720, "Line");
	}
	
	@Override
	public void init(Graphics2D g) throws Exception {
		// TODO Auto-generated method stub
		
		this.camera = new Camera(0, 0, 0, 0, 0, 0);
		this.projection = Matrix.ProjectionFOV(4, FOV, ASPECT_RATIO, 0.1f, 10f);
		
		this.s1 = new Shader();
		this.s = new TextureTintShader();
		s.setTexture(pTexture);
		this.pipeline = new Pipeline(s);
		this.pipeline.setDrawMode(Pipeline.TRIANGLE_MODE);
		this.ps = new ParticleSystem(100);
		this.cylinder = new GameObject(new Primitive().extrude(40, 40, (float t, float h) -> {
			return 1;
			}).resolve());
		this.cylinder.setPosition(0, -0.5f, 2);
		/*this.sb = new Skybox("/Users/infinity/Desktop/Coding Images/skybox.jpg");
		sb.setScale(5);
		this.s.setTexture(sb.getTexture());*/
	}

	@Override
	public void input() {
		// TODO Auto-generated method stub
		this.camera.velocity.x = 0;
		this.camera.velocity.y = 0;
		this.camera.velocity.z = 0; //TODO: make this a function
		
		//Movement
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			this.camera.velocity.z = CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			this.camera.velocity.z = -CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			this.camera.velocity.x = -CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			this.camera.velocity.x = CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SPACE]) {
			this.camera.velocity.y = CAMERA_SPEED;
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_SHIFT]) {
			this.camera.velocity.y = -CAMERA_SPEED;
		}
		
		//Rotation
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
			this.camera.rotation.y = Maths.wrapAngle(this.camera.rotation.y - 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			this.camera.rotation.y = Maths.wrapAngle(this.camera.rotation.y + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			this.camera.rotation.x = Maths.wrapAngle(this.camera.rotation.x - 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			this.camera.rotation.x = Maths.wrapAngle(this.camera.rotation.x + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			this.camera.rotation.z = Maths.wrapAngle(this.camera.rotation.z + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			this.camera.rotation.z = Maths.wrapAngle(this.camera.rotation.z - 0.05f);
		}
			
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE]) {
			this.paused = !this.paused;
			GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE] = false;
		}
		
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		if(!paused) {
			
			//TODO: fix mouse tracking
			/*float dMouseX = (GameEngine.mouse.position.x - GameEngine.mouse.prevPosition.x)/(float) GameEngine.displayWidth;
			float dMouseY = (GameEngine.mouse.position.y - GameEngine.mouse.prevPosition.y)/(float) GameEngine.displayHeight;
			
			//System.out.printf("dx: %f, dy: %f\n", dMouseX, dMouseY);
			float cx = Maths.clamp(Maths.wrapAngle(dMouseY * senseY), minY, maxY);
			float cy = Maths.clamp(Maths.wrapAngle(dMouseX * senseX), minX, maxX);
			System.out.printf("crotX: %f, crotY: %f\n", cx, cy);*/
			
			this.camera.rotate();
			this.camera.translate(this.camera.velocity._multiply(deltaTime));

			this.pCooldown -= deltaTime;
			if(this.pCooldown <= 0) {
				this.ps.addParticle(pQuad, particleInitialPosition, particleInitialRotation, 1f, new Vec3(0, 1f, 0), 2.0f);
				this.pCooldown = this.pInterval;
			}
			
			this.ps.update(deltaTime);
			
		}
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
//		Color col = new Color(255, 255, 255, 127);
//		g.setColor(col);
//		g.fillRect(100, 100, 50, 50);
	}

	@Override
	public void render(Graphics3D g) {
		g.clear(PackedColor.Black);
		pipeline.beginFrame();
		
		Matrix view = this.camera.getViewMatrix();

		this.s.setView(view);
		this.s.setProjection(this.projection);
		this.s1.setView(view);
		this.s1.setProjection(this.projection);
		Vec3 rotation = this.cylinder.getRotation();
		Matrix world = Matrix.Translation(this.cylinder.getPosition())
				.multiply(Matrix.rotationZ(4, rotation.z))
				.multiply(Matrix.rotationY(4, rotation.x))
				.multiply(Matrix.rotationX(4, rotation.y))
				.multiply(Matrix.Scale(4, this.cylinder.getScale()));
		
		this.s1.setWorld(world);
		
		this.pipeline.setDepthTest(true);
		this.pipeline.setColorMode(Pipeline.RGB);
		this.pipeline.setShader(s1);
		this.pipeline.setDrawMode(Pipeline.LINE_MODE);
		this.s1.setDefaultColor(PackedColor.White);
		
		this.pipeline.draw(g, this.cylinder.getMesh());
		
		this.pipeline.setDrawMode(Pipeline.TRIANGLE_MODE);
		this.pipeline.setShader(s);
		this.pipeline.setColorMode(Pipeline.ADDITIVE);
		this.pipeline.setDepthTest(false);
		
		this.s.setDefaultColor(PackedColor.makeRGBA(240, 140, 53, 200));
		this.ps.render(g, pipeline);
		
	}
}
