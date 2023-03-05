package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;


public class AiVsAiTest {

	@Test
	public void testAiVsAi() {
		String title = "AI Vs AI Test";

		GUI.create(title);

		GUI.newGameParameters.setGameMode(GameMode.AI_VS_AI);
		// newGameParameters.setAiType(AiType.RANDOM_AI);

		GUI.startNewGame();

		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1qpKnk/8/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1rpKnk/8/4R3/8/8/8/8/8 w - - 0 1";

		// If White King is in check position.
		// String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";

		// GUI.startNewGame(fenPosition);
		// System.out.println("White king position: " + chessBoard.getWhiteKingPosition());
		// System.out.println("Black king position: " + chessBoard.getBlackKingPosition());

		// System.out.println(GUI.chessBoard);

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (!GUI.chessBoard.isTerminalState()) ;
	}

}
