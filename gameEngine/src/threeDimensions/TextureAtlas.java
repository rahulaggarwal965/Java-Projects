package threeDimensions;

import java.net.URL;

public class TextureAtlas {
	
	private Texture[] textures; //READ ONLY
	
	public TextureAtlas(int num) {
		this.textures = new Texture[num];
	}
	
	public TextureAtlas(Texture[] textures) {
		this.textures = textures;
	}
	
	public int getLength() {
		return this.textures.length;
	}
	
	public Texture[] getTextures() {
		return this.textures;
	}
	
	public Texture getTexture(int position) {
		return this.textures[position];
	}
	
	public static TextureAtlas loadTextureAtlas(Texture texture, int cols, int rows, int num) {
		Texture[] textures = new Texture[num];
		int width = texture.getWidth()/cols, height = texture.getHeight()/rows;
		int index = 0;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				textures[index++] = texture.getSubTexture(i * width, j * height, width, height);
				if(index >= num) return new TextureAtlas(textures);
			}
		}
		throw new IllegalArgumentException("Number of Sprites cannot be more than the number of columns times the number of rows");
	}
	
	public static TextureAtlas loadTextureAtlas(String filepath, int cols, int rows, int num) {
		return loadTextureAtlas(Texture.loadImage(filepath), cols, rows, num);
	}
	
	public static TextureAtlas loadTextureAtlas(URL url, int cols, int rows, int num) {
		return loadTextureAtlas(Texture.loadImage(url), cols, rows, num);
	}

}
