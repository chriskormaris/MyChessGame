package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


public class EvaluateCheckTest {

	@Test
	public void testEvaluateCheck() {
		String title = "Evaluate Check Test";

		// GameParameters.gameMode = GameMode.HUMAN_VS_HUMAN;

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// The Black Queen should capture the White Queen instead of going for a Check.
		String fenPosition = "4k3/8/3q4/8/5Q2/8/8/R3K2R w KQkq - 0 1";
		buttonsFrame.startNewGame(fenPosition);

		while (true) ;
	}

}
