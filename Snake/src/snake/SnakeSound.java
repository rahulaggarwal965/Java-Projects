package snake;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import gameEngine.Sound;

public enum SnakeSound {
	BACKGROUND("/sounds/background.wav"),
	HIT("/sounds/hit.wav"),
	EXPLOSION("/sounds/explosion.wav"),
	GAMEOVER("/sounds/gameover.wav");
	
	private Clip clip;
	private FloatControl gainControl;
	
	SnakeSound(String fileName) {
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
	
	public void stop() {
		Sound.stopClip(this.clip);
	}
	
	public static void init() {
		values();
	}
}
