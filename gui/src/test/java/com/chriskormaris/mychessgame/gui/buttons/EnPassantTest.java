package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;


public class EnPassantTest {

	@Test
	public void testPawnPromotion() {
		String title = "Pawn Promotion Test";

		ButtonsGui buttonsGui = new ButtonsGui(title);

		// newGameParameters.setGameMode(GameMode.AI_VS_AI);
		buttonsGui.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		buttonsGui.startNewGame();

		/* Use these FEN positions, if playing as White. */

		String fenPosition = "4k3/8/8/1pP3p1/8/8/7P/4K3 w - B6 0 1";

		/* Use these FEN positions, if playing as Black. */

		// newGameParameters.setHumanPlayerAllegiance(Allegiance.BLACK);
		// startNewGame();

		// String fenPosition = "4k3/7p/8/8/1Pp3P1/8/8/4K3 b - B3 0 1";

		buttonsGui.startNewGame(fenPosition);

		while (true) ;
	}

}
