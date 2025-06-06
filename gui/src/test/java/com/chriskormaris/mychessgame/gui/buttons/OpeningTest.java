package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class OpeningTest {

	@Test
	void testOpening() {
		String title = "Chess Opening Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// This is the default Chess starting game state. The moves are taken from Wikipedia:
		// https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation

		// String fenPosition = Constants.DEFAULT_STARTING_FEN_POSITION;
		// String fenPosition = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";

		// newGameParameters.setHumanAllegiance(Allegiance.BLACK);
		// startNewGame();

		// String fenPosition = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
		String fenPosition = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";

		buttonsFrame.startNewGame(fenPosition);

		while (true) ;
	}

}
