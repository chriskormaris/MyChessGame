package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class MinimaxAiMaxDepthTest {

	@Test
	void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// newGameParameters.gameMode = GameMode.AI_VS_AI;
		// newGameParameters.aiType = AiType.MINIMAX_AI;

		buttonsFrame.newGameParameters.setAi1MaxDepth(2);
		// newGameParameters.setAi1MaxDepth(3);

		// newGameParameters.setAi2MaxDepth(2);

		buttonsFrame.startNewGame();

		while (true) ;
	}

}
