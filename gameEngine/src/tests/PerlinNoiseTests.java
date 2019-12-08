package tests;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import gameEngine.GameEngine;
import gameEngine.IGameLogic;
import libraries.Maths;
import libraries.Noise;
import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class PerlinNoiseTests implements IGameLogic {

	public static void main(String[] args) {
		PerlinNoiseTests pnt = new PerlinNoiseTests();
		GameEngine gameEngine = new GameEngine(60, 60, 3, pnt, 800, 800, "Perlin Noise Tests", true);
	}
	
	private boolean update = false;
	private int[] pixels;
	private double scale = 100;
	private double persistence = 0.5;
	private double lacunarity = 2.0;
	private int octaves = 6;
	
	@Override
	public void init(Graphics g) throws Exception {
		this.pixels = new int[GameEngine.displayHeight * GameEngine.displayWidth];
		this.updatePixels();
	}

	@Override
	public void input() {
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Q]) {this.update = true; persistence += 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_A]) {this.update = true; persistence -= 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_W]) {this.update = true; lacunarity += 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_S]) {this.update = true; lacunarity -= 0.01;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_Z]) {this.update = true; this.scale *= 1.1;}
		if(GameEngine.keyboard.keysPressed[KeyEvent.VK_X]) {this.update = true; this.scale *= 0.9;}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_E]) {GameEngine.keyboard.keysTyped[KeyEvent.VK_E] = false; this.update = true; this.octaves++;}
		if(GameEngine.keyboard.keysTyped[KeyEvent.VK_D]) {GameEngine.keyboard.keysTyped[KeyEvent.VK_D] = false; this.update = true; this.octaves--;}
		
		
	}

	@Override
	public void update(float interval) {
		
		//CLAMP VALUES
		this.persistence = Maths.clamp(this.persistence, 0.0, 1.0);
		if(this.lacunarity < 1) this.lacunarity = 1;
		if(this.octaves < 0) this.octaves = 0;
		
		//GENERATE NOISE
		if(update) {
			System.out.printf("Octaves: %d, Persistence: %1.9f, Lacunarity: %5.5f, Scale: %9.4f\n", this.octaves, this.persistence, this.lacunarity, this.scale);
			this.update = false;
			this.updatePixels();
		}
	}

	@Override
	public void render(Graphics2D g2d) {
	}

	@Override
	public void render3D(Graphics3D g3d) {
		
		g3d.drawPixels(this.pixels);
	}
	
	public int updatePixel(int index) {
		int i = index % GameEngine.displayWidth;
		int j = index / GameEngine.displayHeight;
		double n =  Noise.noise(i/scale, j/scale, 0, this.octaves, this.persistence, this.lacunarity) * 0.5 + 0.5;
		return PackedColor.makeRGB(n, n, n);
	}
	
	public void updatePixels() {
		
		this.pixels = IntStream.range(0, this.pixels.length).parallel().map(x -> updatePixel(x)).toArray();
		/*for (int j = 0; j < GameEngine.displayHeight; j++) {
			for (int i = 0; i < GameEngine.displayWidth; i++) {
				double n = Noise.noise(i/scale, j/scale, 0, this.octaves, this.persistence, this.lacunarity) * 0.5 + 0.5;
				this.pixels[j * GameEngine.displayWidth + i] = PackedColor.makeRGB(n, n, n);
			}
		}*/
	}

}
