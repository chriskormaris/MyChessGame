package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class AiVsAiTest {

	@Test
	void testAiVsAi() {
		String title = "AI vs AI Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.newGameParameters.setGameMode(GameMode.AI_VS_AI);
		// newGameParameters.setAiType(AiType.RANDOM_AI);

		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1qpKnk/8/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1rpKnk/8/4R3/8/8/8/8/8 w - - 0 1";

		// If White King is in check position.
		// String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";

		buttonsFrame.startNewGame();

		// System.out.println("White king position: " + chessBoard.getWhiteKingPosition());
		// System.out.println("Black king position: " + chessBoard.getBlackKingPosition());

		while (!buttonsFrame.chessBoard.isTerminalState()) ;
	}

}
