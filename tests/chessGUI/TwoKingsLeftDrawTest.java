package chessGUI;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.Allegiance;
import chess.ChessBoard;
import pieces.King;


class TwoKingsLeftDrawTest {

	@Test
	public void testTwoKingsLeftDraw() {
		
		String title = "My Chess Test";
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// String stringMessage = "White plays first.";
		// labelMessage.setText(stringMessage);
		
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
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
