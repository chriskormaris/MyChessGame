package chessGUI;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import enums.Allegiance;
import pieces.King;


class TwoKingsLeftDrawTest {

	@Test
	public void testTwoKingsLeftDraw() {
		
		String title = "Two Kings Left Draw Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.chessBoard.getGameBoard()[0][0] = new King(Allegiance.WHITE);
		ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ChessGUI.chessBoard.getGameBoard()[7][7] = new King(Allegiance.BLACK);
		ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		
		System.out.println();
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		boolean isDraw = ChessGUI.chessBoard.checkForTwoKingsLeftDraw();
		assertTrue("The game is not a draw.", isDraw == true);
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
