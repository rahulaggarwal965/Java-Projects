package gameEngine;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
	
	public static Clip generateClip(URL url) throws Exception {
		AudioInputStream audio = AudioSystem.getAudioInputStream(url);
		Clip clip = AudioSystem.getClip();
		clip.open(audio);
		return clip;
	}
	
	public static FloatControl getFloatControl(Clip clip) {
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
	
	public static synchronized void playClip(Clip clip) {
		clip.stop();
	}
}
