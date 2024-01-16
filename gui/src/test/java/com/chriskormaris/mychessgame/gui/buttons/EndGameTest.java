package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class EndGameTest {

	@Test
	public void testEndGame() {
		String title = "EndGame Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		buttonsFrame.makeChessBoardSquaresEmpty();
		System.out.println(buttonsFrame.chessBoard);

		buttonsFrame.placePieceToPosition("A1", new King(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A3", new Queen(Allegiance.WHITE));
		// placePieceToPosition("A4", new Queen(Allegiance.WHITE));
		// placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Rook(Allegiance.WHITE));
		buttonsFrame.placePieceToPosition("A4", new Pawn(Allegiance.WHITE));

		buttonsFrame.placePieceToPosition("H8", new King(Allegiance.BLACK));
		// placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		// placePieceToPosition("H7", new Rook(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H7", new Pawn(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		// placePieceToPosition("H5", new Knight(Allegiance.BLACK));
		buttonsFrame.placePieceToPosition("H5", new Queen(Allegiance.BLACK));

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
