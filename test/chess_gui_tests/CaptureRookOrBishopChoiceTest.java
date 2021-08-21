package chess_gui_tests;


import chess_board.ChessBoard;
import gui.ChessGUI;
import enumeration.Allegiance;
import org.junit.jupiter.api.Test;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Rook;
import utility.Constants;
import utility.Utilities;

import static org.junit.jupiter.api.Assertions.assertTrue;


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
		
		assertTrue(Utilities.getChessPieceFromPosition(ChessGUI.chessBoard.getGameBoard(), "A1") instanceof Knight,
				"The Black Knight did NOT capture the White Rook.");

		// Continue playing for a minute.
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
