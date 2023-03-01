package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.ButtonsGui.chessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.makeChessBoardSquaresEmpty;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.placePieceToPosition;


public class KnightAndBishopMateTest {

	@Test
	public void testKnightAndBishopMate() {
		String title = "Knight and Bishop Mate Test";

		ButtonsGui.create(title);

		makeChessBoardSquaresEmpty();

		placePieceToPosition("A8", new King(Allegiance.BLACK));
		placePieceToPosition("A6", new Knight(Allegiance.WHITE));
		placePieceToPosition("B6", new King(Allegiance.WHITE));
		// If Bishop moves to "C6", it's checkmate.
		// Any other move of the Bishop ends in a stalemate.
		placePieceToPosition("D7", new Bishop(Allegiance.WHITE));

		System.out.println();
		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		// Continue playing for a minute.
		try {
			Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
