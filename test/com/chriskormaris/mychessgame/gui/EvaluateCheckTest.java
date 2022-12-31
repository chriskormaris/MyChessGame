package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import org.junit.Test;


public class EvaluateCheckTest {

    @Test
    public void testEvaluateCheck() {
        String title = "Evaluate Check";

        // GameParameters.gameMode = GameMode.HUMAN_VS_HUMAN;

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);

        // The Black Queen should capture the White Queen instead of going for a Check.
        String fenPosition = "4k3/8/3q4/8/5Q2/8/8/R3K2R w KQkq - 0 1";
        ChessGUI.placePiecesToChessBoard(fenPosition);

        ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

        System.out.println();

        System.out.println("*****************************");
        System.out.println();

        while (true);
    }

}
