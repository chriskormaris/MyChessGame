package chessGUI;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;


class OpeningTest {

	@Test
	public void testOpening() {
		
		String title = "Chess Opening Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		// This is the default Chess starting game state. The moves are taken from Wikipedia: 
		// https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
		
		// String fenPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		// String fenPosition = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
		// String fenPosition = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
		String fenPosition = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		
		ChessGUI.placePiecesToChessBoard(fenPosition);
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
