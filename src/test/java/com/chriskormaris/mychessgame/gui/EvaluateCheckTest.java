package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.GUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.GUI.placePiecesToChessBoard;


public class EvaluateCheckTest {

	@Test
	public void testEvaluateCheck() {
		String title = "Evaluate Check Test";

		// GameParameters.gameMode = GameMode.HUMAN_VS_HUMAN;

		@SuppressWarnings("unused")
		GUI gui = new GUI(title);

		// The Black Queen should capture the White Queen instead of going for a Check.
		String fenPosition = "4k3/8/3q4/8/5Q2/8/8/R3K2R w KQkq - 0 1";
		placePiecesToChessBoard(fenPosition);

		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (true) ;
	}

}
