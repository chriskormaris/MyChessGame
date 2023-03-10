package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;


public class BishopVsBishopMateTest {

	@Test
	public void testBishopVsBishopMate() {
		String title = "Bishop Vs Bishop Mate Test";

		GUI.create(title);

		GUI.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		// Black plays first.
		// If the Black player makes a blunder and moves the Black Bishop to "B8"
		// and the White player moves the White Bishop to "D5", then it's checkmate for White!
		String fenPosition = "k7/8/K2b5/8/2B5/8/8/8 b - - 0 1";

		GUI.startNewGame(fenPosition);

		System.out.println();
		System.out.println(GUI.chessBoard);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
