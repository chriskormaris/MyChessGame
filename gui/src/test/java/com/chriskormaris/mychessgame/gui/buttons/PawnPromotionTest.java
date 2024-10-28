package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;


class PawnPromotionTest {

	@Test
	void testPawnPromotion() {
		String title = "Pawn Promotion Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// newGameParameters.setGameMode(GameMode.AI_VS_AI);
		buttonsFrame.newGameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

		buttonsFrame.startNewGame();

		/* Use these FEN positions, if playing as White. */

		String fenPosition = "4k3/2P5/8/8/8/8/2p5/4K3 w - - 0 1";

		// If you use this FEN position, you will mate if you promote the White Pawn to Knight.
		// String fenPosition = "7R/2Ppkp2/3bpn2/8/8/1p6/2p5/4K3 w - - 0 1";

		// If you use this FEN position, you will mate if you promote the White Pawn to Queen or Rook.
		// String fenPosition = "7k/QP6/8/8/8/8/n7/7K w - - 0 1";

		// If you use this FEN position, you will lose if the Black player promotes the Pawn to Knight.
		// String fenPosition = "4k3/2P5/1P6/8/8/3BPN2/2pPKP2/7r w - - 0 1";


		/* Use these FEN positions, if playing as Black. */

		// newGameParameters.setHumanAllegiance(Allegiance.BLACK);
		// startNewGame();

		// String fenPosition = "4k3/2P5/8/8/8/8/2p5/4K3 b - - 0 1";

		// If you use this FEN position, you will lose if the White player promotes the Pawn to Knight.
		// String fenPosition = "7R/2Ppkp2/3bpn2/8/8/1p6/2p5/4K3 b - - 0 1";

		buttonsFrame.startNewGame(fenPosition);

		while (true) ;
	}

}
