package chess_gui_tests;


import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
import enumerations.Allegiance;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Rook;
import utilities.Constants;
import utilities.Utilities;


class CaptureRookOrBishopChoiceTest {

	@Test
	public void testCaptureRookOrBishopChoiceTest() {
		
		String title = "Capture Rook or Bishop?";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.placePieceToPosition("E1", new King(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("A1", new Rook(Allegiance.WHITE));
		
		ChessGUI.placePieceToPosition("E8", new King(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("B3", new Knight(Allegiance.BLACK));
		
		ChessGUI.chessBoard.setPlayer(Constants.BLACK);
		ChessGUI.minimaxAiMove(ChessGUI.ai);
		
		assertTrue("The Black Knight did NOT capture the White Rook.", 
				Utilities.getChessPieceFromPosition(ChessGUI.chessBoard.getGameBoard(), "A1") instanceof Knight);

		// Continue playing for a minute.
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}