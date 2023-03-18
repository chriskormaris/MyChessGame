package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class TwoKingsLeftTest {

	@Test
	public void testTwoKingsLeft() {
		String title = "Two Kings Left Test";

		ButtonsGui buttonsGui = new ButtonsGui(title);

		buttonsGui.makeChessBoardSquaresEmpty();

		buttonsGui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		buttonsGui.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));

		System.out.println();
		System.out.println(buttonsGui.chessBoard);

		boolean isDraw = buttonsGui.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertFalse(isDraw, "The game is not a draw.");

		buttonsGui.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
