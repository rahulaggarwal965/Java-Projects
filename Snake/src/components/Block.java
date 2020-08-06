package components;

import java.awt.Point;

import threeDimensions.Graphics3D;
import threeDimensions.PackedColor;

public class Block{

	private Block prev;
	private Point position;
	
	public Block(Block prev, Point position) {
		this.prev = prev;
		this.position = position;
	}
	
	public Block(Block prev, int x, int y) {
		this.prev = prev;
		this.position = new Point(x, y);
	}
	
	public void set(Block prev, Point position) {
		this.prev = prev;
		this.position = position;
	}
	
	public void set(Block prev, int x, int y) {
		this.prev = prev;
		this.position = new Point(x, y);
	}
	
	public void setPrev(Block prev) {
		this.prev = prev;
	}
	
	public Block getPrev() {
		return this.prev;
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public void setPosition(Point p) {
		this.position = p;
	}
	
	public void setPosition(int x, int y) {
		this.position.setLocation(x, y);
	}
	
	public void render(Graphics3D g) {
		g.fillRectangle(this.position.x - Snek.HALF_SIZE + 1,  this.position.y - Snek.HALF_SIZE + 1, Snek.SIZE - 2, Snek.SIZE - 2, PackedColor.Red);
	}
	

}
