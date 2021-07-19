package chess_gui_tests;

import org.junit.jupiter.api.Test;

import chess_gui.ChessGUI;

public class MinimaxAiMaxDepthTest {
	
	@Test
	public void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		ChessGUI.placePiecesToChessBoard();
		

		// ChessGUI.newGameParameters.gameMode = GameMode.AI_VS_AI;
		// ChessGUI.newGameParameters.aiType = AiType.MINIMAX_AI;
		
		ChessGUI.newGameParameters.ai1MaxDepth = 2;
		// ChessGUI.newGameParameters.ai1MaxDepth = 3;
		
		// ChessGUI.newGameParameters.ai2MaxDepth = 2;
		
		
		ChessGUI.startNewGame();
		
		System.out.println(ChessGUI.chessBoard);
		
		while (true);
	}

}
