package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;


public class CastlingTest {

	@Test
	public void testCastling() {
		String title = "Castling Test";

		GUI.create(title);

		GUI.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		/* Play as White. */
		String fenPosition = "4k3/8/8/8/8/8/N7/R3K2R w KQ - 0 1";
		// String fenPosition = "r3k2r/8/8/8/8/8/8/R3K2R w KQ - 0 1";
		// String fenPosition = "3qk3/8/8/8/8/8/8/R2QK2R w KQ - 0 1";
		// String fenPosition = "r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1";
		// String fenPosition = "4k3/8/8/8/7q/8/8/R3K2R w KQkq - 0 1";
		/*----------------*/

		/* Play as Black. */
		// String fenPosition = "r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1";
		// String fenPosition = "r3k2r/8/8/8/8/8/8/4K2R b kq - 0 1";

		// gameParameters.getHumanPlayerAllegiance() = Allegiance.BLACK;
		// GUI.ai = new MinimaxAi(gameParameters.maxDepth1, Constants.WHITE);
		/*----------------*/

		GUI.startNewGame(fenPosition);

		System.out.println(GUI.chessBoard);

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (true) ;
	}

}
