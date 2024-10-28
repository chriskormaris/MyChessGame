package com.chriskormaris.mychessgame.gui.buttons;


import com.chriskormaris.mychessgame.api.enumeration.GameResult;
import com.chriskormaris.mychessgame.gui.frame.ButtonsFrame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


class BlockedKingsAndPawnsDrawTest {

	@Test
	void testBlockedKingsAndPawnsDraw() {
		String title = "Blocked Kings and Pawns Draw Test";

		ButtonsFrame buttonsFrame = new ButtonsFrame(title);

		// These FEN positions are considered a draw!
		// Each king is stuck on their side.
		// String fenPosition = "4k3/8/8/p2p2p1/P2P2P1/8/8/4K3 w KQ - 0 1";
		// String fenPosition = "4k3/8/1p2p4/pP1pPp1p/P1pP1PpP/2P3P1/8/4K3 w KQ - 0 1";

		// These FEN positions are NOT considered a draw!
		// There is an opening between the Pawns, from where the Kings can pass to the other side.
		// String fenPosition = "4k3/8/8/3p2p1/3P2P1/8/8/4K3 w KQ - 0 1";
		// String fenPosition = "4k3/8/1p2p4/pP1pPp2/P1pP1P2/2P5/8/K7 w KQ - 0 1";
		// String fenPosition = "4k3/8/8/1p2p4/pP1pPp2/P1pP1P2/2P5/K7 w KQ - 0 1";
		// String fenPosition = "4k3/1p6/pP2p4/P2pPp1p/2pP1PpP/2P3P1/8/7K w KQ - 0 1";
		// String fenPosition = "4k3/8/8/8/8/4K3/4P3/8 w KQ - 0 1";  // King vs King and 1 Pawn.

		// The following FEN position is not a draw, but it will end up in a draw if the White Pawn is moved!
		String fenPosition = "4k3/8/8/p2p2p1/3P2P1/P7/8/4K3 w KQ - 0 1";

		buttonsFrame.startNewGame(fenPosition);

		System.out.println("Checking if a draw has occurred...");
		buttonsFrame.checkForGameOver();
		boolean isDraw = buttonsFrame.chessBoard.getGameResult() == GameResult.INSUFFICIENT_MATERIAL_DRAW;
		System.out.println(isDraw ? "The game is a draw!" : "The game is NOT a draw!");
		assertFalse(isDraw, "The game is a draw.");

		/*
		removePieceFromPosition("A3");
		placePieceToPosition("A4", new Pawn(Allegiance.WHITE));
		
		// After the White player has moved the White pawn, it should be a draw!
		System.out.println("Checking if a draw has occurred...");
		checkForGameOver();
		isDraw = chessBoard.checkForInsufficientMatingMaterialDraw();
		assertTrue("The game is a draw.", isDraw == true);
		*/

		while (true) ;
	}

}
