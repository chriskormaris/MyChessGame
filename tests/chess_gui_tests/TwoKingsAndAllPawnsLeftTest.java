package chess_gui_tests;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;


class TwoKingsAndAllPawnsLeftTest {

	@Test
	public void testTwoKingsAndAllPawnsLeft() {
		
		String title = "Two Kings And All Pawns Left Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// This position is considered a draw!
		// Each king is stuck in his side.
		// String fenPosition = "4k3/8/1p2p4/pP1pPp1p/P1pP1PpP/2P3P1/8/4K3 w KQ - 0 1";
		
		// This position is NOT considered a draw!
		// There is an opening between the pawns, from where the kings can pass to the other side.
		String fenPosition = "4k3/1p6/pP2p4/P2pPp1p/2pP1PpP/2P3P1/8/4K3 w KQ - 0 1";
		
		ChessGUI.placePiecesToChessBoard(fenPosition);

		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		boolean isDraw = ChessGUI.chessBoard.checkForInsufficientMaterialDraw();
		assertTrue("The game is not a draw.", isDraw == false);
		System.out.println("*****************************");
		System.out.println();
		
		ChessGUI.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
