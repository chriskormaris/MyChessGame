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

		ChessBoard.printChessBoard(ButtonsGui.chessBoard.getGameBoard());

		ButtonsGui.placePieceToPosition("E1", new King(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A1", new Rook(Allegiance.WHITE));

		ButtonsGui.placePieceToPosition("E8", new King(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("B3", new Knight(Allegiance.BLACK));

		ButtonsGui.chessBoard.setPlayer(Constants.BLACK);
		ButtonsGui.aiMove(ButtonsGui.ai);

		assertTrue(
				ButtonsGui.chessBoard.getChessPieceFromPosition("A1") instanceof Knight,
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
