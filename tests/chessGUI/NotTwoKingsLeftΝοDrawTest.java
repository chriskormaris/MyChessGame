package chessGUI;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import utilities.Constants;


class NotTwoKingsLeftΝοDrawTest {

	@Test
	public void testNotTwoKingsLeftNoDraw() {
		
		String title = "My Chess Test";
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// String stringMessage = "White plays first.";
		// labelMessage.setText(stringMessage);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.chessBoard.getGameBoard()[0][0] = Constants.WHITE_KING;
		ChessGUI.placePieceToPosition("A1", Constants.WHITE_KING);
		ChessGUI.chessBoard.getGameBoard()[7][7] = Constants.BLACK_KING;
		ChessGUI.placePieceToPosition("H8", Constants.BLACK_KING);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.chessBoard.getGameBoard()[0][1] = Constants.WHITE_PAWN;
		ChessGUI.placePieceToPosition("B1", Constants.WHITE_PAWN);
		
		System.out.println();
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		boolean isDraw = ChessGUI.chessBoard.checkForTwoKingsLeftDraw();
		assertTrue("The game is not a draw.", isDraw == false);
		System.out.println("*****************************");
		System.out.println();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
