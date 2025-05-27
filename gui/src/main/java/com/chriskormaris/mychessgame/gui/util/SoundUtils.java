package com.chriskormaris.mychessgame.gui.util;

import com.chriskormaris.mychessgame.api.util.Utilities;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SoundUtils {

	private static final String MOVE_SOUND = "move.wav";
	private static final String CAPTURE_SOUND = "capture.wav";
	private static final String CHECKMATE_SOUND = "checkmate.wav";

	public static void playMoveSound() {
		playSound(MOVE_SOUND);
	}

	public static void playCaptureSound() {
		playSound(CAPTURE_SOUND);
	}

	public static void playCheckmateSound() {
		playSound(CHECKMATE_SOUND);
	}

	private static synchronized void playSound(final String path) {
		try {
			URL defaultSound = Utilities.class.getResource("/sounds/" + path);

			// File soundFile = new File(defaultSound.toURI());
			// System.out.println("soundFile: " + soundFile);  // check the URL!

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
