package com.chriskormaris.mychessgame.gui;


import org.junit.jupiter.api.Test;


public class MinimaxAiMaxDepthTest {

	@Test
	public void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		GUI.create(title);

		GUI.placePiecesToStartingPositions();

		// newGameParameters.gameMode = GameMode.AI_VS_AI;
		// newGameParameters.aiType = AiType.MINIMAX_AI;

		GUI.newGameParameters.setAi1MaxDepth(2);
		// newGameParameters.setAi1MaxDepth(3);

		// newGameParameters.setAi2MaxDepth(2);

		GUI.startNewGame();

		System.out.println(GUI.chessBoard);

		while (true) ;
	}

}
