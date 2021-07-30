package chess_gui_tests;


import chess.ChessBoard;
import chess_gui.ChessGUI;
import enumeration.Allegiance;
import org.junit.jupiter.api.Test;
import piece.Bishop;
import piece.King;
import piece.Knight;

import static org.junit.jupiter.api.Assertions.assertTrue;


class InsufficientMaterialDrawTest {

	@Test
	public void testInsufficientMaterialDraw() {
		
		String title = "Insufficient Material Draw Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		// ChessGUI.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		// ChessGUI.placePieceToPosition("A4", new Knight(Allegiance.WHITE));
		
		ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		// ChessGUI.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("H5", new Knight(Allegiance.BLACK));
		
		System.out.println();
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		boolean isDraw = ChessGUI.chessBoard.checkForInsufficientMaterialDraw();
		assertTrue(isDraw, "The game is not a draw.");
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
