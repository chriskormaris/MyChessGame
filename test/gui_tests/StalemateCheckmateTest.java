package gui_tests;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.gui.ChessGUI;
import org.junit.Test;
// import enumerations.AiType;


public class StalemateCheckmateTest {

    @Test
    public void testStalemateCheckmate() {
        String title = "Stalemate or Checkmate Test";

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);

        // ChessGUI.gameParameters.getAiType() = AiType.RANDOM_AI;

        // String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
        String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";
        ChessGUI.placePiecesToChessBoard(fenPosition);

        ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

        System.out.println();

        System.out.println("*****************************");
        System.out.println();

        while (true) ;
    }

}
