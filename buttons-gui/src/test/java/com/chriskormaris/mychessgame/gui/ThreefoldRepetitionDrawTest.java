package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.util.FenUtils;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.ButtonsGui.chessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.makeChessBoardSquaresEmpty;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.newGameParameters;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.placePieceToPosition;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.startNewGame;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.undoHalfMoveFenPositions;


public class ThreefoldRepetitionDrawTest {

	@Test
	public void testThreefoldRepetitionDraw() {
		String title = "Threefold Repetition Draw Test";

		ButtonsGui.create(title);

		ButtonsGui.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);
		ButtonsGui.startNewGame();

		ButtonsGui.makeChessBoardSquaresEmpty();

		ButtonsGui.placePieceToPosition("A1", new King(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
		ButtonsGui.placePieceToPosition("A4", new Knight(Allegiance.WHITE));

		ButtonsGui.placePieceToPosition("H8", new King(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
		ButtonsGui.placePieceToPosition("H5", new Knight(Allegiance.BLACK));

		System.out.println();
		ChessBoard.printChessBoard(ButtonsGui.chessBoard.getGameBoard());

		// halfMoveGameBoards.clear();
		ButtonsGui.undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(ButtonsGui.chessBoard));

		// Continue playing for 5 minutes.
		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
