package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


public class KnightAndBishopMateTest {

	@Test
	public void testKnightAndBishopMate() {
		String title = "Knight and Bishop Mate Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();

		buttonsFrame.placePieceToPosition("A8", new King(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("A6", new Knight(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("B6", new King(Allegiance.WHITE));
		// If Bishop moves to "C6", it's checkmate.
		// Any other move of the Bishop ends in a stalemate.
		buttonsFrame.placePieceToPosition("D7", new Bishop(Allegiance.WHITE));

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
