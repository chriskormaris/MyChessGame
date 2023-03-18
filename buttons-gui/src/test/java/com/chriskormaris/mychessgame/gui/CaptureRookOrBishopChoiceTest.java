package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CaptureRookOrBishopChoiceTest {

	@Test
	public void testCaptureRookOrBishopChoice() {
		String title = "Capture Rook or Bishop?";

		GUI gui = new GUI(title);

		gui.placePieceToPosition("E1", new King(Allegiance.WHITE));
		gui.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		gui.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		gui.placePieceToPosition("A1", new Rook(Allegiance.WHITE));

		gui.placePieceToPosition("E8", new King(Allegiance.BLACK));
		gui.placePieceToPosition("B3", new Knight(Allegiance.BLACK));

		gui.chessBoard.setPlayer(Constants.BLACK);
		gui.aiMove(gui.ai);

		assertTrue(
				gui.chessBoard.getChessPieceFromPosition("A1") instanceof Knight,
				"The Black Knight did NOT capture the White Rook."
		);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
