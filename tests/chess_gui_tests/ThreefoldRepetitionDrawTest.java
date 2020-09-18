package chess_gui_tests;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
import enumerations.Allegiance;
import enumerations.GameMode;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import utilities.Utilities;


class ThreefoldRepetitionDrawTest {

	@Test
	public void testThreefoldRepetitionDraw() {
		
		String title = "Insufficient Material Draw Rule Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessGUI.gameParameters.gameMode = GameMode.HUMAN_VS_HUMAN;
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		ChessGUI.placePieceToPosition("A4", new Knight(Allegiance.WHITE));
		
		ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		ChessGUI.placePieceToPosition("H5", new Knight(Allegiance.BLACK));
		
		System.out.println();
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		// ChessGUI.halfmoveGameBoards.clear();
		ChessGUI.halfmoveGameBoards.push(Utilities.copyGameBoard(ChessGUI.chessBoard.getGameBoard()));
		
		// Continue playing for 5 minutes.
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
