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

		GUI.create(title);

		GUI.makeChessBoardSquaresEmpty();
		System.out.println(GUI.chessBoard);

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
		System.out.println(GUI.chessBoard);

		boolean isDraw = GUI.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");
		System.out.println("*****************************");
		System.out.println();

		GUI.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
