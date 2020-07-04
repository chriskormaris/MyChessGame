package chessGUI;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;


class CastlingTest {

	@Test
	public void castlingTest() {
		
		String title = "My Chess Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// String fenPosition = "4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1";
		// String fenPosition = "3qk3/8/8/8/8/8/8/R2QK2R w KQ - 0 1";
		String fenPosition = "r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1";
		
		ChessGUI.placePiecesToChessBoard(fenPosition);
		
		// String stringMessage = "White plays first.";
		// labelMessage.setText(stringMessage);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
