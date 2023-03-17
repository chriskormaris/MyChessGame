package com.chriskormaris.mychessgame.gui;


import org.junit.jupiter.api.Test;


public class EvaluateCheckTest {

	@Test
	public void testEvaluateCheck() {
		String title = "Evaluate Check Test";

		// GameParameters.gameMode = GameMode.HUMAN_VS_HUMAN;

		GUI.create(title);

		// The Black Queen should capture the White Queen instead of going for a Check.
		String fenPosition = "4k3/8/3q4/8/5Q2/8/8/R3K2R w KQkq - 0 1";
		GUI.startNewGame(fenPosition);

		while (true) ;
	}

}
