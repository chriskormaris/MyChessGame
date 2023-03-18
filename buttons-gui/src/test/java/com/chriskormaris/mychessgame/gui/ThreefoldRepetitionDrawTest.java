package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import org.junit.jupiter.api.Test;


public class ThreefoldRepetitionDrawTest {

	@Test
	public void testThreefoldRepetitionDraw() {
		String title = "Threefold Repetition Draw Test";

		GUI gui = new GUI(title);

		gui.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		gui.startNewGame();

		gui.makeChessBoardSquaresEmpty();

		gui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		gui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		gui.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		gui.placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		gui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		gui.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		gui.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		gui.placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
