package com.chriskormaris.mychessgame.gui.util;

import com.chriskormaris.mychessgame.api.util.Utilities;
import lombok.experimental.UtilityClass;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

@UtilityClass
public class SoundUtils {

	private final String PIECE_MOVE_SOUND = "piece_move.wav";
	private final String CHECKMATE_SOUND = "checkmate.wav";

	public void playMoveSound() {
		playSound(PIECE_MOVE_SOUND);
	}

	public void playCheckmateSound() {
		playSound(CHECKMATE_SOUND);
	}

	private synchronized void playSound(final String path) {
		try {
			URL defaultSound = Utilities.class.getResource("/sounds/" + path);

			// File soundFile = new File(defaultSound.toURI());
			// System.out.println("soundFile: " + soundFile);  // check the URL!

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.err.println(ex.getMessage());
		}
	}

}