package breakout;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import gameEngine.Sound;

public enum BreakoutSound {
	BACKGROUND("../sounds/background.wav"),
	EXPLOSION("../sounds/explosion.wav"),
	HIT("../sounds/hit.wav"),
	GAMEOVER("../sounds/gameover.wav"),
	POWERUP("../sounds/powerup.wav");
	
	private Clip clip;
	private FloatControl gainControl;
	
	BreakoutSound(String fileName) {
		try {
			this.clip = Sound.generateClip(this.getClass().getResource(fileName));
			this.gainControl = Sound.getFloatControl(clip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setVolume(float volume) {
		Sound.setVolume(volume, this.gainControl);
		
	}
	
	public void play(boolean loop) {
		Sound.playClip(this.clip, loop);
	}
	
	public static void init() {
		values();
	}
}
