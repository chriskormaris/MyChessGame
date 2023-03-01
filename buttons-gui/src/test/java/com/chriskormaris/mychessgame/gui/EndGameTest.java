package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.ButtonsGui.chessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.makeChessBoardSquaresEmpty;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.placePieceToPosition;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class EndGameTest {

	@Test
	public void testEndGame() {
		String title = "EndGame Test";

		ButtonsGui.create(title);

		ButtonsGui.makeChessBoardSquaresEmpty();
		ChessBoard.printChessBoard(ButtonsGui.chessBoard.getGameBoard());

		ButtonsGui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A3", new Queen(Allegiance.WHITE));
		// placePieceToPosition("A4", new Queen(Allegiance.WHITE));
		// placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Knight(Allegiance.WHITE));
		// placePieceToPosition("A4", new Rook(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A4", new Pawn(Allegiance.WHITE));

		ButtonsGui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		// placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		// placePieceToPosition("H7", new Rook(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("H7", new Pawn(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		// placePieceToPosition("H5", new Knight(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("H5", new Queen(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(ButtonsGui.chessBoard.getGameBoard());

		boolean isEndGame = ButtonsGui.chessBoard.isEndGame();
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
