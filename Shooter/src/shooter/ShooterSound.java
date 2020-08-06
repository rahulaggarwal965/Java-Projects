package shooter;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import gameEngine.Sound;

public enum ShooterSound {
	BACKGROUND("/sounds/background.wav"),
	EXPLOSION("/sounds/explosion.wav"),
	HIT("/sounds/hit.wav"),
	GAMEOVER("/sounds/gameover.wav"),
	BULLET("/sounds/bullet.wav"),
	MISSILE("/sounds/missile.wav"),
	THRUSTERS("/sounds/thrusters.wav"),
	WIND("/sounds/wind.wav");
	
	private Clip clip;
	private FloatControl gainControl;
	
	ShooterSound(String fileName) {
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
