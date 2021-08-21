package gui_tests;


import chess_board.ChessBoard;
import enumeration.Allegiance;
import enumeration.GameMode;
import gui.ChessGUI;
import org.junit.jupiter.api.Test;
import piece.Bishop;
import piece.King;
import piece.Knight;
import utility.Utilities;


class ThreefoldRepetitionDrawTest {

	@Test
	public void testThreefoldRepetitionDraw() {
		
		String title = "Insufficient Material Draw Rule Test";
		
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessGUI.gameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		
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
		
		// ChessGUI.halfMoveGameBoards.clear();
		ChessGUI.halfMoveGameBoards.push(Utilities.copyGameBoard(ChessGUI.chessBoard.getGameBoard()));
		
		// Continue playing for 5 minutes.
		try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
