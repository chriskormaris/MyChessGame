package com.chriskormaris.mychessgame.gui.buttons;


import org.junit.jupiter.api.Test;


public class MinimaxAiMaxDepthTest {

	@Test
	public void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		ButtonsGui buttonsGui = new ButtonsGui(title);

		// newGameParameters.gameMode = GameMode.AI_VS_AI;
		// newGameParameters.aiType = AiType.MINIMAX_AI;

		buttonsGui.newGameParameters.setAi1MaxDepth(2);
		// newGameParameters.setAi1MaxDepth(3);

		// newGameParameters.setAi2MaxDepth(2);

		buttonsGui.startNewGame();

		while (true) ;
	}

}
