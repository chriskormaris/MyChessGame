package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.ButtonsGui.checkForGameOver;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.chessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.makeChessBoardSquaresEmpty;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.placePieceToPosition;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InsufficientMaterialDrawTest {

	@Test
	public void testInsufficientMaterialDraw() {
		String title = "Insufficient Material Draw Test";

		ButtonsGui.create(title);

		makeChessBoardSquaresEmpty();
		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		placePieceToPosition("A1", new King(Allegiance.WHITE));
		placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		// placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		placePieceToPosition("H8", new King(Allegiance.BLACK));
		// placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		boolean isDraw = chessBoard.checkForInsufficientMaterialDraw();
		assertTrue(isDraw, "The game is NOT a draw.");
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
