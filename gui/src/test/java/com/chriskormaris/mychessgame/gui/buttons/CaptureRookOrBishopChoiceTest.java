package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CaptureRookOrBishopChoiceTest {

	@Test
	void testCaptureRookOrBishopChoice() {
		String title = "Capture Rook or Bishop?";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.placePieceToPosition("E1", Constants.WHITE_KING);
		buttonsFrame.placePieceToPosition("C1", Constants.WHITE_BISHOP);
		buttonsFrame.placePieceToPosition("F1", Constants.WHITE_BISHOP);
		buttonsFrame.placePieceToPosition("A1", Constants.WHITE_ROOK);

		buttonsFrame.placePieceToPosition("E8", Constants.BLACK_KING);
		buttonsFrame.placePieceToPosition("B3", Constants.BLACK_KNIGHT);

		buttonsFrame.chessBoard.setPlayer(Constants.BLACK);
		buttonsFrame.aiMove(buttonsFrame.ai);

        assertEquals(
				Constants.KNIGHT,
				Math.abs(buttonsFrame.chessBoard.getChessSquareFromPosition("A1")),
				"The Black Knight did NOT capture the White Rook."
		);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
