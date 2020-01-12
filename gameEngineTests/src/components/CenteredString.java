package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CenteredString {
	private String s;
	private Font f;
	private int x, y;
	
	public CenteredString(Graphics g, String s, Rectangle r, Font font) {
		this.s = s;
		this.f = font;
		FontMetrics m = g.getFontMetrics(font);
		this.x = r.x + (r.width - m.stringWidth(s)) / 2;
		this.y = r.y + (r.height - m.getHeight()) / 2 + m.getAscent();
	}
	public CenteredString(Graphics g, String s, int x, int y, int width, int height, Font font) {
		this.s = s;
		this.f = font;
		FontMetrics m = g.getFontMetrics(font);
		this.x = x + (width - m.stringWidth(s)) / 2;
		this.y = y + (height - m.getHeight()) / 2 + m.getAscent();
	}
	
	public void render(Graphics g, Color col) {
		g.setColor(col);
		g.setFont(this.f);
		//System.out.println(f.getFontName());
		g.drawString(this.s, this.x, this.y);
	}
}
