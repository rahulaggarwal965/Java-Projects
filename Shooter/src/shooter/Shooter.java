package shooter;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import gameEngine.GameEngine;
import gameEngine.GameLogic;
import math.Maths;
import shaders.GeometryLightShader;
import shaders.TextureShader;
import threeDimensions.Camera;
import threeDimensions.Graphics3D;
import threeDimensions.Matrix;
import threeDimensions.Pipeline;
import threeDimensions.Vec3;

public class Shooter implements GameLogic {

	private static final float ASPECT_RATIO = 1.777777f; // 16:9
	private static final float FOV = 95.0f;
	private static final float CAMERA_SPEED = 30.0f;

	private Pipeline pipeline;
	private GeometryLightShader gls;
	private TextureShader ts;
	private Camera camera;
	private Skybox sb;
	private Matrix projection;

	private EndlessTerrain e;
	private TerrainGenerator t;

	private boolean paused = false;

	public static void main(String[] args) {
		Shooter shooter = new Shooter();
		GameEngine e = new GameEngine(60, 60, 3, shooter, 1280, 720, "Shooter");
	}

	@Override
	public void init(Graphics2D g) throws Exception {
		this.camera = new Camera(0, 50, 0, 0, 0, 0);
		this.projection = Matrix.ProjectionFOV(4, FOV, ASPECT_RATIO, 0.05f, 1000f);
		this.gls = new GeometryLightShader();
		this.ts = new TextureShader();
		this.pipeline = new Pipeline(ts);

		this.sb = new Skybox("/Users/infinity/Desktop/Coding Images/skybox.png");
		sb.setScale(200);
		this.ts.setTexture(sb.getTexture());
		
		this.t = new TerrainGenerator();
		this.e = new EndlessTerrain(camera.position, t, pipeline, gls);
	}

	@Override
	public void input() {
		this.camera.velocity.set(0, 0, 0);

		// Movement
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {
			this.camera.velocity.z = CAMERA_SPEED;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {
			this.camera.velocity.z = -CAMERA_SPEED;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {
			this.camera.velocity.x = -CAMERA_SPEED;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_D]) {
			this.camera.velocity.x = CAMERA_SPEED;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_SPACE]) {
			this.camera.velocity.y = CAMERA_SPEED;
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_SHIFT]) {
			this.camera.velocity.y = -CAMERA_SPEED;
		}

		// Rotation
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_LEFT]) {
			this.camera.rotationAngles.y = Maths.wrapAngle(this.camera.rotationAngles.y - 0.05f);
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_RIGHT]) {
			this.camera.rotationAngles.y = Maths.wrapAngle(this.camera.rotationAngles.y + 0.05f);
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_UP]) {
			this.camera.rotationAngles.x = Maths.wrapAngle(this.camera.rotationAngles.x - 0.05f);
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_DOWN]) {
			this.camera.rotationAngles.x = Maths.wrapAngle(this.camera.rotationAngles.x + 0.05f);
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {
			this.camera.rotationAngles.z = Maths.wrapAngle(this.camera.rotationAngles.z + 0.05f);
		}
		if (GameEngine.keyboard.keysPressed[KeyEvent.VK_E]) {
			this.camera.rotationAngles.z = Maths.wrapAngle(this.camera.rotationAngles.z - 0.05f);
		}

		if (GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE]) {
			this.paused = !this.paused;
			GameEngine.keyboard.keysTyped[KeyEvent.VK_ESCAPE] = false;
		}
	}

	@Override
	public void update(float deltaTime) {
		if (!paused) {
			this.camera.rotate();
			this.camera.translate(this.camera.velocity._multiply(deltaTime));

			e.update(deltaTime);
		}
	}

	@Override
	public void render(Graphics2D g) {

	}

	@Override
	public void render(Graphics3D g) {
		pipeline.beginFrame();
		pipeline.setShader(this.ts);
		Matrix view = this.camera.rotationInverse;
		Vec3 rotation = this.sb.getRotation();
		Matrix world = Matrix.Translation(this.sb.getPosition())
				.multiply(Matrix.rotationZ(4, rotation.z))
				.multiply(Matrix.rotationY(4, rotation.x))
				.multiply(Matrix.rotationX(4, rotation.y))
				.multiply(Matrix.Scale(4, this.sb.getScale()));
		
		this.ts.setWorld(world);
		this.ts.setView(view);
		this.ts.setProjection(this.projection);
		
		this.pipeline.draw(g, sb.getMesh());
		
		this.pipeline.setShader(this.gls);
		
		view = this.camera.rotationInverse.multiply(Matrix.Translation(this.camera.position._negate()));

		this.gls.setView(view);
		this.gls.setProjection(this.projection);

		e.render(g);
	}

}
