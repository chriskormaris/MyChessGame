package chessGUI;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;


class StalemateCheckmateTest {

	@Test
	public void testStalemateCheckmatePawnPromotion() {
		
		String title = "My Chess Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";
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
