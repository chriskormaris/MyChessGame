package com.chriskormaris.mychessgame.gui.buttons;


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

		ButtonsGui buttonsGui = new ButtonsGui(title);

		buttonsGui.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		buttonsGui.startNewGame();

		buttonsGui.makeChessBoardSquaresEmpty();

		buttonsGui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		buttonsGui.placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		buttonsGui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		buttonsGui.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		buttonsGui.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		buttonsGui.placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
