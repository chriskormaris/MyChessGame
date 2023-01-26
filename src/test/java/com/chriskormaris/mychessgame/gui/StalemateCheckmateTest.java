package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.GUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.GUI.placePiecesToChessBoard;


public class StalemateCheckmateTest {

	@Test
	public void testStalemateCheckmate() {
		String title = "Stalemate or Checkmate Test";

		@SuppressWarnings("unused")
		GUI cbg = new GUI(title);

		// newGameParameters.setAi1Type(AiType.RANDOM_AI);
		// newGameParameters.setAi1MaxDepth(1);
		// startNewGame();

		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "3R1Knk/8/8/5Q2/8/8/8/8 w - - 3 5";
		String fenPosition = "5Kbk/5R2/8/5Q2/8/8/8/8 w - - 3 5";

		placePiecesToChessBoard(fenPosition);

		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (true) ;
	}

}
