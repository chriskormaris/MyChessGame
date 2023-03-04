package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class InsufficientMaterialDrawTest {

	@Test
	public void testInsufficientMaterialDraw() {
		String title = "Insufficient Material Draw Test";

		GUI.create(title);

		GUI.makeChessBoardSquaresEmpty();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		// King Vs King draw.
		// GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// GUI.placePieceToPosition("A2", new Knight(Allegiance.WHITE));
		// GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Bishop Vs King draw.
		// GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// GUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		// GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Knight Vs King draw.
		// GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// GUI.placePieceToPosition("A2", new Knight(Allegiance.WHITE));
		// GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Bishop Vs King and Bishop on the same color draw.
		GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		GUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		GUI.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		boolean isDraw = GUI.chessBoard.checkForInsufficientMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");
		System.out.println("*****************************");
		System.out.println();

		GUI.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
