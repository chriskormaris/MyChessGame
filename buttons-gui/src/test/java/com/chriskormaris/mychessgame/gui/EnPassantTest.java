package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.ButtonsGui.chessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.newGameParameters;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.placePiecesToChessBoard;
import static com.chriskormaris.mychessgame.gui.ButtonsGui.startNewGame;


public class EnPassantTest {

	@Test
	public void testPawnPromotion() {
		String title = "Pawn Promotion Test";

		ButtonsGui.create(title);

		// newGameParameters.setGameMode(GameMode.AI_VS_AI);
		ButtonsGui.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		ButtonsGui.startNewGame();

		/* Use these FEN positions, if playing as White. */

		String fenPosition = "4k3/8/8/1pP3p1/8/8/7P/4K3 w - B6 0 1";

		/* Use these FEN positions, if playing as Black. */

		// newGameParameters.setHumanPlayerAllegiance(Allegiance.BLACK);
		// startNewGame();

		// String fenPosition = "4k3/7p/8/8/1Pp3P1/8/8/4K3 b - B3 0 1";

		ButtonsGui.placePiecesToChessBoard(fenPosition);
		ChessBoard.printChessBoard(ButtonsGui.chessBoard.getGameBoard());

		// disableChessBoardSquares();
		// playAiVsAi();

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (true) ;
	}

}
