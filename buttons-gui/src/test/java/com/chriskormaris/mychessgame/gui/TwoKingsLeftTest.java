package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class TwoKingsLeftTest {

	@Test
	public void testTwoKingsLeft() {
		String title = "Two Kings Left Test";

		GUI gui = new GUI(title);

		gui.makeChessBoardSquaresEmpty();

		gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		gui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		gui.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));

		System.out.println();
		System.out.println(gui.chessBoard);

		boolean isDraw = gui.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertFalse(isDraw, "The game is not a draw.");

		gui.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
