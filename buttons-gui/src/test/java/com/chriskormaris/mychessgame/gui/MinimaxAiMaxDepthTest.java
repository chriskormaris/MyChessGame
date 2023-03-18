package com.chriskormaris.mychessgame.gui;


import org.junit.jupiter.api.Test;


public class MinimaxAiMaxDepthTest {

	@Test
	public void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		GUI gui = new GUI(title);

		// newGameParameters.gameMode = GameMode.AI_VS_AI;
		// newGameParameters.aiType = AiType.MINIMAX_AI;

		gui.newGameParameters.setAi1MaxDepth(2);
		// newGameParameters.setAi1MaxDepth(3);

		// newGameParameters.setAi2MaxDepth(2);

		gui.startNewGame();

		while (true) ;
	}

}
