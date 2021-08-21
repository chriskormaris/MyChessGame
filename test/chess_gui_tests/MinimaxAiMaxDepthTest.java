package chess_gui_tests;

import gui.ChessGUI;
import org.junit.jupiter.api.Test;

public class MinimaxAiMaxDepthTest {
	
	@Test
	public void testMinimaxAiMaxDepth() {
		String title = "Minimax AI Max Depth Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		ChessGUI.placePiecesToChessBoard();
		

		// ChessGUI.newGameParameters.gameMode = GameMode.AI_VS_AI;
		// ChessGUI.newGameParameters.aiType = AiType.MINIMAX_AI;
		
		ChessGUI.newGameParameters.setAi1MaxDepth(2);
		// ChessGUI.newGameParameters.setAi1MaxDepth(3);

		// ChessGUI.newGameParameters.setAi2MaxDepth(2);
		
		ChessGUI.startNewGame();
		
		System.out.println(ChessGUI.chessBoard);
		
		while (true);
	}

}
