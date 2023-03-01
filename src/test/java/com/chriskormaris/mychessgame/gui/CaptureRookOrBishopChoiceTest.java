package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.ButtonsGui.ai;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.aiMove;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.chessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.placePieceToPosition;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CaptureRookOrBishopChoiceTest {

	@Test
	public void testCaptureRookOrBishopChoice() {
		String title = "Capture Rook or Bishop?";

		ButtonsGui.create(title);

		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		placePieceToPosition("E1", new King(Allegiance.WHITE));
		placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		placePieceToPosition("A1", new Rook(Allegiance.WHITE));

		placePieceToPosition("E8", new King(Allegiance.BLACK));
		placePieceToPosition("B3", new Knight(Allegiance.BLACK));

		chessBoard.setPlayer(Constants.BLACK);
		aiMove(ai);

		assertTrue(
				chessBoard.getChessPieceFromPosition("A1") instanceof Knight,
				"The Black Knight did NOT capture the White Rook."
		);

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
