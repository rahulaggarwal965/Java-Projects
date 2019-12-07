package gameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
	public boolean[] keysPressed;
	public boolean[] keysTyped;
	
	public Keyboard() {
		this.keysPressed = new boolean[KeyEvent.KEY_LAST];
		this.keysTyped = new boolean[KeyEvent.KEY_LAST];
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		this.keysPressed[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		this.keysPressed[e.getKeyCode()] = false;
		this.keysTyped[e.getKeyCode()] = true;
	}
}
