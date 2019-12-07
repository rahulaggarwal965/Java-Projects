package gameEngine;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	
	public static Clip generateClip(String fileName) throws Exception {
		File soundFile = new File(fileName).getAbsoluteFile();
		AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
		Clip clip = AudioSystem.getClip();
		clip.open(audio);
		return clip;
	}
	
	public static FloatControl getFloatControl(Clip clip) throws Exception {
		if(clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
			return (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		else return null;
	}
	
	public static void setVolume(float volume, FloatControl gainControl) {
		assert(volume >= 0.0f && volume <= 1.0f);
		if(gainControl != null) {
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
	
	public static synchronized void playClip(Clip clip, boolean loop) {
		new Thread(new Runnable() {
			
			public void run() {
				if(clip.isRunning()) clip.stop();
				clip.setFramePosition(0);
				clip.start();
				if(loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}).start();
	}
}
