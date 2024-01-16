package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CaptureRookOrBishopChoiceTest {

	@Test
	public void testCaptureRookOrBishopChoice() {
		String title = "Capture Rook or Bishop?";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.placePieceToPosition("E1", new King(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A1", new Rook(Allegiance.WHITE));

		buttonsFrame.placePieceToPosition("E8", new King(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("B3", new Knight(Allegiance.BLACK));

		buttonsFrame.chessBoard.setPlayer(Constants.BLACK);
		buttonsFrame.aiMove(buttonsFrame.ai);

		assertTrue(
				buttonsFrame.chessBoard.getChessPieceFromPosition("A1") instanceof Knight,
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
