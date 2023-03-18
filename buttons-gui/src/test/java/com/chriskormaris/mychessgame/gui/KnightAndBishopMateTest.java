package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import org.junit.jupiter.api.Test;


public class KnightAndBishopMateTest {

	@Test
	public void testKnightAndBishopMate() {
		String title = "Knight and Bishop Mate Test";

		GUI gui = new GUI(title);

		gui.makeChessBoardSquaresEmpty();

		gui.placePieceToPosition("A8", new King(Allegiance.BLACK));
		gui.placePieceToPosition("A6", new Knight(Allegiance.WHITE));
		gui.placePieceToPosition("B6", new King(Allegiance.WHITE));
		// If Bishop moves to "C6", it's checkmate.
		// Any other move of the Bishop ends in a stalemate.
		gui.placePieceToPosition("D7", new Bishop(Allegiance.WHITE));

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
