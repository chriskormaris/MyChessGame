package gui;
/*
 * Athens 2022
 *
 * Created on : 2022-01-15
 * Author     : Christos Kormaris
 */

import utility.Utilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundUtilities {

	public static synchronized void playSound(final String path) {

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
