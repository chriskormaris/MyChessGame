package chess_gui_tests;


import org.junit.jupiter.api.Test;

import chess.ChessBoard;
import chess_gui.ChessGUI;
//import enumerations.AiType;
import enumerations.GameMode;


class AiVsAiTest {

	@Test
	public void testAiVsAi() {
		String title = "AI Vs AI Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessGUI.gameParameters.gameMode = GameMode.AI_VS_AI;
		// ChessGUI.gameParameters.aiType = AiType.RANDOM_AI;

		ChessGUI.restoreDefaultValues();
		
		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1qpKnk/8/4Q3/8/8/8/8/8 w - - 0 1";
		String fenPosition = "1R1rpKnk/8/4R3/8/8/8/8/8 w - - 0 1";

		// If White King is in check, White does not plays first, 
		// because the "whiteKingInCheckValidPieceMoves" Map<String, Set<String>> is empty. 
		// String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";
		
		ChessGUI.placePiecesToChessBoard(fenPosition);
		
		// System.out.println("player: " + ChessGUI.chessBoard.getPlayer());
		
		ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());
		
		ChessGUI.playAiVsAi();
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
