package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.square.King;
import com.chriskormaris.mychessgame.api.square.Pawn;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


class TwoKingsLeftTest {

	@Test
	void testTwoKingsLeft() {
		String title = "Two Kings Left Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();

		buttonsFrame.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("H8", new King(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));

		System.out.println();
		System.out.println(buttonsFrame.chessBoard);

		boolean isDraw = buttonsFrame.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertFalse(isDraw, "The game is not a draw.");

		buttonsFrame.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
