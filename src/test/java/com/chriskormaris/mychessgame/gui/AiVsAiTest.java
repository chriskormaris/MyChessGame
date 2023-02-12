package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.GUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.GUI.newGameParameters;
import static com.chriskormaris.mychessgame.gui.GUI.placePiecesToStartingPositions;
import static com.chriskormaris.mychessgame.gui.GUI.playAiVsAi;
import static com.chriskormaris.mychessgame.gui.GUI.startNewGame;


public class AiVsAiTest {

	@Test
	public void testAiVsAi() {
		String title = "AI Vs AI Test";

		GUI.create(title);

		newGameParameters.setGameMode(GameMode.AI_VS_AI);
		// newGameParameters.setAiType(AiType.RANDOM_AI);

		startNewGame();

		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1qpKnk/8/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1rpKnk/8/4R3/8/8/8/8/8 w - - 0 1";

		// If White King is in check, White does not play first,
		// because the "whiteKingInCheckValidPieceMoves" Map<String, Set<String>> is empty.
		// String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";

		// placePiecesToChessBoard(fenPosition);
		// System.out.println("White king position: " + chessBoard.getWhiteKingPosition());
		// System.out.println("Black king position: " + chessBoard.getBlackKingPosition());

		// System.out.println("player: " + chessBoard.getPlayer());

		// ChessBoard.printChessBoard(chessBoard.getGameBoard());

		// Find the "whiteKingInCheckValidPieceMoves" and "blackKingInCheckValidPieceMoves",
		// if the White or Black King respectively is in check.
		// boolean storeKingInCheckMoves = true;
		// chessBoard.checkForBlackCheckmate(storeKingInCheckMoves);
		// chessBoard.checkForWhiteCheckmate(storeKingInCheckMoves);
		// System.out.println(chessBoard.getWhiteKingInCheckValidPieceMoves());

		placePiecesToStartingPositions();
		playAiVsAi();

		System.out.println();

		System.out.println("*****************************");
		System.out.println();

		while (!chessBoard.isTerminalState()) ;
	}

}
