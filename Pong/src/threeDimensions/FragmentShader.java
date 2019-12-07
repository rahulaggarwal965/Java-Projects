package threeDimensions;

public class FragmentShader {
	
	private Texture texture;
	private float tWidth, tHeight, tXClamp, tYClamp;
	
	public void setTexture(Texture texture) {
		this.texture = texture;
		this.tWidth = (float) texture.getWidth();
		this.tHeight = (float) texture.getHeight();
		this.tXClamp = tWidth - 1.0f;
		this.tYClamp = tHeight - 1.0f;
	}
	
	public int shade(Vertex v) {
		return PackedColor.White;
				
	}
}
