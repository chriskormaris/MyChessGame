package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
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

		GUI.create(title);

		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		GUI.placePieceToPosition("E1", new King(Allegiance.WHITE));
		GUI.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		GUI.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		GUI.placePieceToPosition("A1", new Rook(Allegiance.WHITE));

		GUI.placePieceToPosition("E8", new King(Allegiance.BLACK));
		GUI.placePieceToPosition("B3", new Knight(Allegiance.BLACK));

		GUI.chessBoard.setPlayer(Constants.BLACK);
		GUI.aiMove(GUI.ai);

		assertTrue(
				GUI.chessBoard.getChessPieceFromPosition("A1") instanceof Knight,
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
