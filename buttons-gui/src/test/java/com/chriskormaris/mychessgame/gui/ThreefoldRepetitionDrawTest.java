package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.util.FenUtils;
import org.junit.jupiter.api.Test;


public class ThreefoldRepetitionDrawTest {

	@Test
	public void testThreefoldRepetitionDraw() {
		String title = "Threefold Repetition Draw Test";

		GUI.create(title);

		GUI.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		GUI.startNewGame();

		GUI.makeChessBoardSquaresEmpty();

		GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		GUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		GUI.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		GUI.placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		GUI.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		GUI.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		GUI.placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		// halfMoveGameBoards.clear();
		GUI.undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(GUI.chessBoard));

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
