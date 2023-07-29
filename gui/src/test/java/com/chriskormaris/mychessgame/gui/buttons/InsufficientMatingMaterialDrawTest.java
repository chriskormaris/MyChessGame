package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class InsufficientMatingMaterialDrawTest {

	@Test
	public void testInsufficientMatingMaterialDraw() {
		String title = "Insufficient Material Draw Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();
		System.out.println(buttonsFrame.chessBoard);

		// King vs King draw.
		// gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// gui.placePieceToPosition("A2", new Knight(Allegiance.WHITE));
		// gui.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Bishop vs King draw.
		// gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// gui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		// gui.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Knight vs King draw.
		// gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		// gui.placePieceToPosition("A2", new Knight(Allegiance.WHITE));
		// gui.placePieceToPosition("H8", new King(Allegiance.BLACK));

		// King and Bishop vs King and Bishop on the same color draw.
		buttonsFrame.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("H8", new King(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));

		System.out.println();
		System.out.println(buttonsFrame.chessBoard);

		boolean isDraw = buttonsFrame.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");

		buttonsFrame.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
