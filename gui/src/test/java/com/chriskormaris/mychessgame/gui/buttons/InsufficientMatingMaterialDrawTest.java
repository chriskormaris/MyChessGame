package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class InsufficientMatingMaterialDrawTest {

	@Test
	void testInsufficientMatingMaterialDraw() {
		String title = "Insufficient Material Draw Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();
		System.out.println(buttonsFrame.chessBoard);

		// King vs King draw.
		// gui.placePieceToPosition("A1", Constants.WHITE_KING);
		// gui.placePieceToPosition("A2", Constants.WHITE_KNIGHT);
		// gui.placePieceToPosition("H8", Constants.BLACK_KING);

		// King and Bishop vs King draw.
		// gui.placePieceToPosition("A1", Constants.WHITE_KING);
		// gui.placePieceToPosition("A2", Constants.WHITE_BISHOP);
		// gui.placePieceToPosition("H8", Constants.BLACK_KING);

		// King and Knight vs King draw.
		// gui.placePieceToPosition("A1", Constants.WHITE_KING);
		// gui.placePieceToPosition("A2", Constants.WHITE_KNIGHT);
		// gui.placePieceToPosition("H8", Constants.BLACK_KING);

		// King and Bishop vs King and Bishop on the same color draw.
		buttonsFrame.placePieceToPosition("A1", Constants.WHITE_KING);
		buttonsFrame.placePieceToPosition("A2", Constants.WHITE_BISHOP);
		buttonsFrame.placePieceToPosition("H8", Constants.BLACK_KING);
		buttonsFrame.placePieceToPosition("H7", Constants.BLACK_BISHOP);

		System.out.println();
		System.out.println(buttonsFrame.chessBoard);

		boolean isDraw = buttonsFrame.chessBoard.checkForInsufficientMatingMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");

		buttonsFrame.checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
