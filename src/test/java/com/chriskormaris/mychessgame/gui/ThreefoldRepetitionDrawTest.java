package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.util.Utilities;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.GUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.GUI.halfMoveGameBoards;
import static com.chriskormaris.mychessgame.gui.GUI.makeChessBoardSquaresEmpty;
import static com.chriskormaris.mychessgame.gui.GUI.newGameParameters;
import static com.chriskormaris.mychessgame.gui.GUI.placePieceToPosition;
import static com.chriskormaris.mychessgame.gui.GUI.startNewGame;


public class ThreefoldRepetitionDrawTest {

	@Test
	public void testThreefoldRepetitionDraw() {
		String title = "Threefold Repetition Draw Test";

		@SuppressWarnings("unused")
		GUI cbg = new GUI(title);

		newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		startNewGame();

		makeChessBoardSquaresEmpty();

		placePieceToPosition("A1", new King(Allegiance.WHITE));
		placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		placePieceToPosition("H8", new King(Allegiance.BLACK));
		placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(chessBoard.getGameBoard());

		// halfMoveGameBoards.clear();
		halfMoveGameBoards.push(Utilities.copyGameBoard(chessBoard.getGameBoard()));

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
