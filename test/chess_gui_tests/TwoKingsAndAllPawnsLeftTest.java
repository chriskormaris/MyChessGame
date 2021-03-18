package chess_gui_tests;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
//import enumerations.Allegiance;
//import pieces.Pawn;


class TwoKingsAndAllPawnsLeftTest {

	@Test
	public void testTwoKingsAndAllPawnsLeft() {
		
		String title = "Two Kings And All Pawns Left Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// These FEN positions are considered a draw!
		// Each king is stuck on their side.
		// String fenPosition = "4k3/8/8/p2p2p1/P2P2P1/8/8/4K3 w KQ - 0 1";
		// String fenPosition = "4k3/8/1p2p4/pP1pPp1p/P1pP1PpP/2P3P1/8/4K3 w KQ - 0 1";
		
		// These FEN positions are NOT considered a draw!
		// There is an opening between the pawns, from where the kings can pass to the other side.
		// String fenPosition = "4k3/8/8/3p2p1/3P2P1/8/8/4K3 w KQ - 0 1";
		String fenPosition = "4k3/8/1p2p4/pP1pPp2/P1pP1P2/2P5/8/K7 w KQ - 0 1";
		// String fenPosition = "4k3/1p6/pP2p4/P2pPp1p/2pP1PpP/2P3P1/8/7K w KQ - 0 1";

		// The following FEN position is not a draw, but it will end up in a draw
		// if the White pawn is moved!
		// String fenPosition = "4k3/8/8/p2p2p1/3P2P1/P7/8/4K3 w KQ - 0 1";

		ChessGUI.placePiecesToChessBoard(fenPosition);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println("Checking if a draw has occurred...");
		ChessGUI.checkForGameOver();
		boolean isDraw = ChessGUI.chessBoard.isInsufficientMaterialDraw();
		System.out.println((isDraw) ? "The game is a draw!" : "The game is NOT a draw!");
		assertTrue("The game is NOT a draw.", isDraw == false);
		// System.out.println("*****************************");
		// System.out.println();
		
		/*
		ChessGUI.removePieceFromPosition("A3");
		ChessGUI.placePieceToPosition("A4", new Pawn(Allegiance.WHITE));
		
		// After the White player has moved the White pawn, it should be a draw!
		System.out.println("Checking if a draw has occurred...");
		ChessGUI.checkForGameOver();
		isDraw = ChessGUI.chessBoard.isInsufficientMaterialDraw();
		assertTrue("The game is a draw.", isDraw == true);
		*/

		while (true);
	}

}
