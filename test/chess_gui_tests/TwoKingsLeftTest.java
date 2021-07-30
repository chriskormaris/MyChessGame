package chess_gui_tests;


import chess.ChessBoard;
import chess_gui.ChessGUI;
import enumeration.Allegiance;
import org.junit.jupiter.api.Test;
import piece.King;
import piece.Pawn;

import static org.junit.jupiter.api.Assertions.assertFalse;


class TwoKingsLeftTest {

	@Test
	public void testTwoKingsLeft() {
		
		String title = "Two Kings Left Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));
		
		System.out.println();
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		boolean isDraw = ChessGUI.chessBoard.checkForInsufficientMaterialDraw();
		assertFalse(isDraw, "The game is not a draw.");
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
