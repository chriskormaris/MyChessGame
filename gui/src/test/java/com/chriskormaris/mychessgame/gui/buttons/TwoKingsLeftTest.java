package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


class TwoKingsLeftTest {

	@Test
	void testTwoKingsLeft() {
		String title = "Two Kings Left Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();

		buttonsFrame.placePieceToPosition("A1", Constants.WHITE_KING);
		buttonsFrame.placePieceToPosition("H8", Constants.BLACK_KING);
		buttonsFrame.placePieceToPosition("B2", Constants.WHITE_PAWN);

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
