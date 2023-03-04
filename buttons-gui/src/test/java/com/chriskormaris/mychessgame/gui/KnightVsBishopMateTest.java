package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;


public class KnightVsBishopMateTest {

	@Test
	public void testKnightAndBishopMate() {
		String title = "Knight Vs Bishop Mate Test";

		GUI.create(title);

		GUI.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		// Black plays first.
		// If the Black player makes a blunder and moves the Black Bishop to "B8"
		// and the White player moves the White Knight to "B6", then it's checkmate for White!
		String fenPosition = "k7/8/K2b5/8/N7/8/8/8 b - - 0 1";

		GUI.startNewGame(fenPosition);

		System.out.println();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
