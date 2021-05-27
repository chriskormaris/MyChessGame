package chess_gui_tests;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
import enumerations.Allegiance;


class PawnPromotionTest {

	@Test
	public void testStalemateCheckmatePawnPromotion() {
		
		String title = "Pawn Promotion Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		// ChessGUI.gameParameters.gameMode = GameMode.HUMAN_VS_HUMAN;
		ChessGUI.newGameParameters.humanPlayerAllegiance = Allegiance.BLACK;
		ChessGUI.startNewGame();
		// ChessGUI.restoreDefaultValues();
		
		// String fenPosition = "4k3/2P5/8/8/8/8/2p5/4K3 w - - 0 1";
		String fenPosition = "4k3/2P5/8/8/8/8/2p5/4K3 b - - 0 1";
		ChessGUI.placePiecesToChessBoard(fenPosition);

		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}