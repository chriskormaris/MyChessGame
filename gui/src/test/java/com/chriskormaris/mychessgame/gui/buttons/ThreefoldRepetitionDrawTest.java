package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.square.Bishop;
import com.chriskormaris.mychessgame.api.square.King;
import com.chriskormaris.mychessgame.api.square.Knight;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class ThreefoldRepetitionDrawTest {

	@Test
	void testThreefoldRepetitionDraw() {
		String title = "Threefold Repetition Draw Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		buttonsFrame.newGameParameters.setEnableTimeLimit(false);
		buttonsFrame.startNewGame();

		buttonsFrame.makeChessBoardSquaresEmpty();

		buttonsFrame.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		buttonsFrame.placePieceToPosition("H8", new King(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		System.out.println();
		System.out.println(buttonsFrame.chessBoard);

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

}
