package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class EnPassantTest {

	@Test
	void testPawnPromotion() {
		String title = "Pawn Promotion Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// newGameParameters.setGameMode(GameMode.AI_VS_AI);
		buttonsFrame.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		buttonsFrame.startNewGame();

		/* Use these FEN positions, if playing as White. */

		String fenPosition = "4k3/8/8/1pP3p1/8/8/7P/4K3 w - b6 0 1";

		/* Use these FEN positions, if playing as Black. */

		// newGameParameters.setHumanAllegiance(Allegiance.BLACK);
		// startNewGame();

		// String fenPosition = "4k3/7p/8/8/1Pp3P1/8/8/4K3 b - b3 0 1";

		buttonsFrame.startNewGame(fenPosition);

		while (true) ;
	}

}
