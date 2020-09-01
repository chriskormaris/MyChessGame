package chess_gui_tests;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;


class CastlingTest {

	@Test
	public void testCastling() {
		
		String title = "Castling Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
//		String fenPosition = "4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1";
		String fenPosition = "r3k2r/8/8/8/8/8/8/R3K2R w KQ - 0 1";
//		String fenPosition = "3qk3/8/8/8/8/8/8/R2QK2R w KQ - 0 1";
//		String fenPosition = "r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1";
		
		ChessGUI.placePiecesToChessBoard(fenPosition);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
