package com.chriskormaris.mychessgame.gui.buttons;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.square.EmptySquare;
import com.chriskormaris.mychessgame.api.square.Queen;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

class EightQueensProblemTest {

	@Test
	void testEightQueensProblem() throws InterruptedException {
		String title = "8 Queens Problem Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);
		buttonsFrame.makeChessBoardSquaresEmpty();
		buttonsFrame.disableChessPanelClicks();
		ChessBoard chessBoard = buttonsFrame.chessBoard;

		for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
			chessBoard.getGameBoard()[0][j] = new Queen(Allegiance.WHITE);
			chessBoard.setThreats();

			placeQueens(1, chessBoard);
			buttonsFrame.redrawChessBoard();
			System.out.println(chessBoard);

			Thread.sleep(1000);

			buttonsFrame.makeChessBoardSquaresEmpty();
			chessBoard.setThreats();
		}
	}

	private static boolean placeQueens(int i, ChessBoard chessBoard) {
		// base case: If all queens are placed
		// then return true
		if (i == chessBoard.getNumOfRows()) return true;

		// Consider the row and try placing
		// queen in all columns one by one
		for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
			// Check if the queen can be placed
			if (chessBoard.getGameBoard()[i][j].isEmpty() && !chessBoard.getSquaresThreatenedByWhite()[i][j]) {
				chessBoard.getGameBoard()[i][j] = new Queen(Allegiance.WHITE);
				chessBoard.setThreats();
				if (placeQueens(i + 1, chessBoard)) {
					return true;
				} else {
					chessBoard.getGameBoard()[i][j] = EmptySquare.getInstance();
					chessBoard.setThreats();
				}
			}
		}
		return false;
	}

}
