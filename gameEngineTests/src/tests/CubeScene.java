package tests;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import components.Cube;
import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import threeDimensions.Camera;
import threeDimensions.Graphics3D;
import threeDimensions.IndexedTriangleList;
import threeDimensions.Matrix;
import threeDimensions.Pipeline;
import threeDimensions.Vec3;
import threeDimensions.Vertex;


public class CubeScene implements GameLogic {
	
	private static final float ASPECT_RATIO = 1.777777f; //16:9
	private static final float FOV = 95.0f;
	private static final float CAMERA_SPEED = 1.0f;
	
	private boolean paused = false;
	
	private Pipeline pipeline;
	private Camera camera;
	private IndexedTriangleList<Vertex> iTriList;
	private Matrix projection;
	
	private Vec3 modelPosition = new Vec3(0, 0, 2);
	private float tx = 0, ty = 0, tz = 0;
	
	public static void main(String[] args) {
		CubeScene cs = new CubeScene();
		GameEngine gameEngine = new GameEngine(60, 60, 3, cs, 1280, 720, "CubeScene");
	}
	
	@Override
	public void init(Graphics2D g) throws Exception {
		// TODO Auto-generated method stub
		
		this.camera = new Camera(0, 0, 0, 0, 0, 0);
		this.projection = Matrix.ProjectionFOV(4, FOV, ASPECT_RATIO, 0.1f, 10f);
		
		this.pipeline = new Pipeline();
		this.iTriList = Cube.getTexturedTriangles(1.0f, 1);
		pipeline.fragShader.setTexture(GameEngine.loadImage("/Users/infinity/Desktop/SauronEye.png"));
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
			this.camera.rotationAngles.y = Maths.wrapAngle(this.camera.rotationAngles.y - 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			this.camera.rotationAngles.y = Maths.wrapAngle(this.camera.rotationAngles.y + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			this.camera.rotationAngles.x = Maths.wrapAngle(this.camera.rotationAngles.x - 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			this.camera.rotationAngles.x = Maths.wrapAngle(this.camera.rotationAngles.x + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			this.camera.rotationAngles.z = Maths.wrapAngle(this.camera.rotationAngles.z + 0.05f);
		}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			this.camera.rotationAngles.z = Maths.wrapAngle(this.camera.rotationAngles.z - 0.05f);
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
			
		}
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics3D g) {
		pipeline.beginFrame();
		
		Matrix view = this.camera.rotationInverse.multiply(Matrix.Translation(this.camera.position._negate()));
		
		Matrix world = Matrix.Translation(this.modelPosition)
				.multiply(Matrix.rotationZ(4, tz))
				.multiply(Matrix.rotationY(4, ty))
				.multiply(Matrix.rotationX(4, tx));

		pipeline.setWorld(world);
		pipeline.setView(view);
		pipeline.setProjection(this.projection);
		pipeline.draw(g, this.iTriList);
		
	}
}
