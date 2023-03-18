package com.chriskormaris.mychessgame.gui.buttons;


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

		ButtonsGui buttonsGui = new ButtonsGui(title);

		buttonsGui.placePieceToPosition("E1", new King(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("A1", new Rook(Allegiance.WHITE));

		buttonsGui.placePieceToPosition("E8", new King(Allegiance.BLACK));
		buttonsGui.placePieceToPosition("B3", new Knight(Allegiance.BLACK));

		buttonsGui.chessBoard.setPlayer(Constants.BLACK);
		buttonsGui.aiMove(buttonsGui.ai);

		assertTrue(
				buttonsGui.chessBoard.getChessPieceFromPosition("A1") instanceof Knight,
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
