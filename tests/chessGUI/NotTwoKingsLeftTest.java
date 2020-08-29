package chessGUI;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
import enums.Allegiance;
import pieces.King;
import pieces.Pawn;


class NotTwoKingsLeftTest {

	@Test
	public void testNotTwoKingsLeft() {
		
		String title = "Not Two Kings Left Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));
		
		System.out.println();
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		boolean isDraw = ChessGUI.chessBoard.checkForTwoKingsLeftDraw();
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
