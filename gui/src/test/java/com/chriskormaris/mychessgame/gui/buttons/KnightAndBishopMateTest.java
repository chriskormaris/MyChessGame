package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class KnightAndBishopMateTest {

	@Test
	void testKnightAndBishopMate() {
		String title = "Knight and Bishop Mate Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();

		buttonsFrame.placePieceToPosition("A8", Constants.BLACK_KING);
		buttonsFrame.placePieceToPosition("A6", Constants.WHITE_KNIGHT);
		buttonsFrame.placePieceToPosition("B6", Constants.WHITE_KING);
		// If Bishop moves to "C6", it's checkmate.
		// Any other move of the Bishop ends in a stalemate.
		buttonsFrame.placePieceToPosition("D7", Constants.WHITE_BISHOP);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
