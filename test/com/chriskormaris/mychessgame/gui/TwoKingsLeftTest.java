package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import org.junit.Test;

import static com.chriskormaris.mychessgame.gui.ChessGUI.checkForGameOver;
import static com.chriskormaris.mychessgame.gui.ChessGUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.ChessGUI.makeChessBoardSquaresEmpty;
import static com.chriskormaris.mychessgame.gui.ChessGUI.placePieceToPosition;
import static org.junit.Assert.assertFalse;


public class TwoKingsLeftTest {

	@Test
	public void testTwoKingsLeft() {
		String title = "Two Kings Left Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);

		makeChessBoardSquaresEmpty();

		placePieceToPosition("A1", new King(Allegiance.WHITE));
		placePieceToPosition("H8", new King(Allegiance.BLACK));
		placePieceToPosition("B2", new Pawn(Allegiance.WHITE));

		System.out.println();
		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		boolean isDraw = chessBoard.checkForInsufficientMaterialDraw();
		assertFalse("The game is not a draw.", isDraw);
		System.out.println("*****************************");
		System.out.println();

		checkForGameOver();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
