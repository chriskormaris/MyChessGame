package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.GUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.GUI.placePiecesToChessBoard;


public class CastlingTest {

	@Test
	public void testCastling() {
		String title = "Castling Test";

		@SuppressWarnings("unused")
		GUI cbg = new GUI(title);

		/* Play as White. */
		String fenPosition = "4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1";
		// String fenPosition = "r3k2r/8/8/8/8/8/8/R3K2R w KQ - 0 1";
		// String fenPosition = "3qk3/8/8/8/8/8/8/R2QK2R w KQ - 0 1";
		// String fenPosition = "r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1";
		// String fenPosition = "4k3/8/8/8/7q/8/8/R3K2R w KQkq - 0 1";
		/*----------------*/

		/* Play as Black. */
		// String fenPosition = "r3k2r/8/8/8/8/8/8/4K3 b kq - 0 1";
		// String fenPosition = "r3k2r/8/8/8/8/8/8/4K2R b kq - 0 1";

		// gameParameters.getHumanPlayerAllegiance() = Allegiance.BLACK;
		// com.chriskormaris.mychessgame.api.ai = new MinimaxAi(gameParameters.maxDepth1, Constants.WHITE);
		/*----------------*/

		placePiecesToChessBoard(fenPosition);

		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (true) ;
	}

}
