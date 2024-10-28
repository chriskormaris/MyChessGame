package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class KnightVsBishopMateTest {

	@Test
	void testKnightVsBishopMate() {
		String title = "Knight vs Bishop Mate Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		// Black plays first.
		// If the Black player makes a blunder and moves the Black Bishop to "B8"
		// and the White player moves the White Knight to "B6", then it's checkmate for White!
		String fenPosition = "k7/8/K2b5/8/N7/8/8/8 b - - 0 1";

		buttonsFrame.startNewGame(fenPosition);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
