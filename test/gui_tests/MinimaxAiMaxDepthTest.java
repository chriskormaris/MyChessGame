package gui_tests;

import com.chriskormaris.mychessgame.gui.ChessGUI;
import org.junit.Test;

public class MinimaxAiMaxDepthTest {

    @Test
    public void testMinimaxAiMaxDepth() {
        String title = "Minimax AI Max Depth Test";

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);
        ChessGUI.placePiecesToStartingPositions();


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
