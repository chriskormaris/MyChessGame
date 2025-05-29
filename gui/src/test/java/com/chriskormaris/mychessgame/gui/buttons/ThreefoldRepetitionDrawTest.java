package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class ThreefoldRepetitionDrawTest {

	@Test
	void testThreefoldRepetitionDraw() {
		String title = "Threefold Repetition Draw Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		buttonsFrame.startNewGame();

		buttonsFrame.makeChessBoardSquaresEmpty();

		buttonsFrame.placePieceToPosition("A1", Constants.WHITE_KING);
		buttonsFrame.placePieceToPosition("A2", Constants.WHITE_BISHOP);
		buttonsFrame.placePieceToPosition("A3", Constants.WHITE_KNIGHT);
		buttonsFrame.placePieceToPosition("A4", Constants.WHITE_KNIGHT);

		buttonsFrame.placePieceToPosition("H8", Constants.BLACK_KING);
		buttonsFrame.placePieceToPosition("H7", Constants.BLACK_BISHOP);
		buttonsFrame.placePieceToPosition("H6", Constants.BLACK_KNIGHT);
		buttonsFrame.placePieceToPosition("H5", Constants.BLACK_KNIGHT);

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
