package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class InsufficientMatingMaterialDrawTest {

	@Test
	public void testInsufficientMatingMaterialDraw() {
		String title = "Insufficient Material Draw Test";

		ButtonsGui buttonsGui = new ButtonsGui(title);

		buttonsGui.makeChessBoardSquaresEmpty();
		System.out.println(buttonsGui.chessBoard);

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
		buttonsGui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		buttonsGui.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));

		System.out.println();
		System.out.println(buttonsGui.chessBoard);

		boolean isDraw = buttonsGui.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");

		buttonsGui.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
