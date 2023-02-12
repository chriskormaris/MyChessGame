package com.chriskormaris.mychessgame.gui;


import org.junit.jupiter.api.Test;

import static com.chriskormaris.mychessgame.gui.GUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.GUI.newGameParameters;
import static com.chriskormaris.mychessgame.gui.GUI.placePiecesToStartingPositions;
import static com.chriskormaris.mychessgame.gui.GUI.startNewGame;


public class MinimaxAiMaxDepthTest {

	@Test
	public void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		GUI.create(title);

		placePiecesToStartingPositions();

		// newGameParameters.gameMode = GameMode.AI_VS_AI;
		// newGameParameters.aiType = AiType.MINIMAX_AI;

		newGameParameters.setAi1MaxDepth(2);
		// newGameParameters.setAi1MaxDepth(3);

		// newGameParameters.setAi2MaxDepth(2);

		startNewGame();

		System.out.println(chessBoard);

		while (true) ;
	}

}
