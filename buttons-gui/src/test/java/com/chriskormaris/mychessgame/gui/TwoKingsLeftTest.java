package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class TwoKingsLeftTest {

	@Test
	public void testTwoKingsLeft() {
		String title = "Two Kings Left Test";

		GUI.create(title);

		GUI.makeChessBoardSquaresEmpty();

		GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		GUI.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));

		System.out.println();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		boolean isDraw = GUI.chessBoard.checkForInsufficientMaterialDraw();
		assertFalse(isDraw, "The game is not a draw.");
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
