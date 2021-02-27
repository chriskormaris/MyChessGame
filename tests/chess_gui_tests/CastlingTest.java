package chess_gui_tests;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
//import enumerations.Allegiance;
//import minimax_ai.MiniMaxAi;
//import utilities.Constants;


class CastlingTest {

	@Test
	public void testCastling() {
		
		String title = "Castling Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
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
		
		// ChessGUI.gameParameters.humanPlayerAllegiance = Allegiance.BLACK;
		// ChessGUI.ai = new MiniMaxAi(ChessGUI.gameParameters.maxDepth1, Constants.WHITE);
		/*----------------*/
		 
		ChessGUI.placePiecesToChessBoard(fenPosition);		
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
