package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class EndGameTest {

	@Test
	void testEndGame() {
		String title = "EndGame Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();
		System.out.println(buttonsFrame.chessBoard);

		buttonsFrame.placePieceToPosition("A1", Constants.WHITE_KING);
		buttonsFrame.placePieceToPosition("A2", Constants.WHITE_BISHOP);
		buttonsFrame.placePieceToPosition("A3", Constants.WHITE_QUEEN);
		// placePieceToPosition("A4", Constants.WHITE_QUEEN);
		// placePieceToPosition("A3", Constants.WHITE_KNIGHT);
		// placePieceToPosition("A4", Constants.WHITE_KNIGHT);
		// placePieceToPosition("A4", Constants.WHITE_ROOK);
		buttonsFrame.placePieceToPosition("A4", Constants.WHITE_PAWN);

		buttonsFrame.placePieceToPosition("H8", Constants.BLACK_KING);
		// placePieceToPosition("H7", Constants.BLACK_BISHOP);
		// placePieceToPosition("H7", Constants.BLACK_ROOK);
		buttonsFrame.placePieceToPosition("H7", Constants.BLACK_PAWN);
		buttonsFrame.placePieceToPosition("H6", Constants.BLACK_KNIGHT);
		// placePieceToPosition("H5", Constants.BLACK_KNIGHT);
		buttonsFrame.placePieceToPosition("H5", Constants.BLACK_QUEEN);

		System.out.println();
		System.out.println(buttonsFrame.chessBoard);

		boolean isEndGame = buttonsFrame.chessBoard.isEndGame();
		assertTrue(isEndGame, "It is NOT endgame.");
		// assertFalse(isEndGame, "It is endgame.");

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
