package gui_tests;


import chess_board.ChessBoard;
import gui.ChessGUI;
import org.junit.jupiter.api.Test;
//import enumerations.AiType;


class StalemateCheckmateTest {

	@Test
	public void testStalemateCheckmatePawnPromotion() {
		String title = "Stalemate or Checkmate Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// ChessGUI.gameParameters.getAiType() = AiType.RANDOM_AI;

		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";
		ChessGUI.placePiecesToChessBoard(fenPosition);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
