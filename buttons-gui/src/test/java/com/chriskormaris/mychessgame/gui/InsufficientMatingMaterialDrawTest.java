package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class InsufficientMatingMaterialDrawTest {

	@Test
	public void testInsufficientMatingMaterialDraw() {
		String title = "Insufficient Material Draw Test";

		GUI gui = new GUI(title);

		gui.makeChessBoardSquaresEmpty();
		System.out.println(gui.chessBoard);

		// King Vs King draw.
		// gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// gui.placePieceToPosition("A2", new Knight(Allegiance.WHITE));
		// gui.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Bishop Vs King draw.
		// gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// gui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		// gui.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Knight Vs King draw.
		// gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// gui.placePieceToPosition("A2", new Knight(Allegiance.WHITE));
		// gui.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Bishop Vs King and Bishop on the same color draw.
		gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		gui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		gui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		gui.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));

		System.out.println();
		System.out.println(gui.chessBoard);

		boolean isDraw = gui.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");

		gui.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
