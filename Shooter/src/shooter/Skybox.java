package shooter;

import gameEngine.GameEngine;
import threeDimensions.GameObject;
import threeDimensions.Mesh;
import threeDimensions.Texture;

public class Skybox extends GameObject{
	
	private Texture texture;

	public Skybox(String textureFile) {
		this.texture = GameEngine.loadImage(textureFile);
		this.setMesh(Skybox.generateMesh());
		
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	private static Mesh generateMesh() {
		float[] positions = {
				//Front
				1, -1, -1,
				1, 1, -1,
				-1, 1, -1,
				-1, -1, -1,
				
				//Left
				-1, -1, -1,
				-1, 1, -1,
				-1, 1, 1,
				-1, -1, 1,
				
				//Right
				1, -1, 1,
				1, 1, 1,
				1, 1, -1,
				1, -1, -1,
				
				//Back
				-1, -1, 1,
				-1, 1, 1,
				1, 1, 1,
				1, -1, 1,
				
				//Top
				-1, 1, 1,
				-1, 1, -1,
				1, 1, -1,
				1, 1, 1,
				
				//Bottom
				-1, -1, -1,
				-1, -1, 1,
				1, -1, 1,
				1, -1, -1	
		};
		
		float[] textures = {
				//Front
				0.333333f, 0.5f,
				0.333333f, 0f,
				0.666666f, 0f,
				0.666666f, 0.5f,
				
				//Left
				0, 1,
				0, 0.5f,
				0.333333f, 0.5f,
				0.333333f, 1,
				
				//Right
				0.666666f, 0.5f,
				0.666666f, 0,
				1, 0,
				1, 0.5f,
				
				//Back
				0.666666f, 1,
				0.666666f, 0.5f,
				1, 0.5f,
				1, 1,
				
				//Top
				0.333333f, 0.5f,
				0.666666f, 0.5f,
				0.666666f, 1,
				0.333333f, 1,
				
				//Bottom
				0, 0.5f,
				0, 0,
				0.333333f, 0,
				0.333333f, 0.5f
		};
		
		int[] indices = {
				0, 1, 3,  1, 2, 3,
				4, 5, 7,  5, 6, 7,
				8, 9, 11, 9, 10, 11,
				12, 13, 15, 13, 14, 15, 
				16, 17, 19, 17, 18, 19,
				20, 21, 23, 21, 22, 23,
		};
		
		return new Mesh(indices, new int[] {3, 2}, positions, textures);
	}
}
