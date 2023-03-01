package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class EndGameTest {

	@Test
	public void testEndGame() {
		String title = "EndGame Test";

		GUI.create(title);

		GUI.makeChessBoardSquaresEmpty();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		GUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
		GUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		GUI.placePieceToPosition("A3", new Queen(Allegiance.WHITE));
		// placePieceToPosition("A4", new Queen(Allegiance.WHITE));
		// placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Rook(Allegiance.WHITE));
		GUI.placePieceToPosition("A4", new Pawn(Allegiance.WHITE));

		GUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
		// placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		// placePieceToPosition("H7", new Rook(Allegiance.BLACK));
		GUI.placePieceToPosition("H7", new Pawn(Allegiance.BLACK));
		GUI.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		// placePieceToPosition("H5", new Knight(Allegiance.BLACK));
		GUI.placePieceToPosition("H5", new Queen(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(GUI.chessBoard.getGameBoard());

		boolean isEndGame = GUI.chessBoard.isEndGame();
		assertTrue(isEndGame, "It is NOT endgame.");
		// assertFalse(isEndGame, "It is endgame.");
		System.out.println("*****************************");
		System.out.println();

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
