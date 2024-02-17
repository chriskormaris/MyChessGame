package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


public class SmotheredMateTest {

	@Test
	public void testSmotheredMate() {
		String title = "Smothered Mate Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// Mate in 2! Sacrifice the White Queen on G8, to mate with the White Knight on F7.
		// String fenPosition = "rnb2r1k/2qp2pp/p6N/2p5/4Pp2/1Q1P4/PPP2PPP/R4RK1 w - - 0 1";

		// Mate in 3! Move the White Knight to H6.
		// Then, sacrifice the White Queen on G8, to mate with the White Knight on F7.
		String fenPosition = "rnb2rk1/2qp1Npp/p7/2p5/4Pp2/1Q1P4/PPP2PPP/R4RK1 w - - 0 1";

		buttonsFrame.startNewGame(fenPosition);

		System.out.println();
		System.out.println(buttonsFrame.chessBoard);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
