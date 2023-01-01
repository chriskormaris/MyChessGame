package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import org.junit.Test;

import static com.chriskormaris.mychessgame.gui.ChessGUI.chessBoard;
import static com.chriskormaris.mychessgame.gui.ChessGUI.gameParameters;
import static com.chriskormaris.mychessgame.gui.ChessGUI.placePiecesToChessBoard;


public class EnPassantTest {

    @Test
    public void testPawnPromotion() {
        String title = "Pawn Promotion Test";

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);

        // gameParameters.setGameMode(GameMode.AI_VS_AI);
        gameParameters.setGameMode(GameMode.HUMAN_VS_HUMAN);

        /* Use these FEN positions, if playing as White. */

        String fenPosition = "4k3/8/8/1pP3p1/8/8/7P/4K3 w - B6 0 1";

        /* Use these FEN positions, if playing as Black. */

        // newGameParameters.setHumanPlayerAllegiance(Allegiance.BLACK);
        // startNewGame();

        // String fenPosition = "4k3/7p/8/8/1Pp3P1/8/8/4K3 b - B3 0 1";

        placePiecesToChessBoard(fenPosition);
        ChessBoard.printChessBoard(chessBoard.getGameBoard());

        // playAiVsAi();

        System.out.println();

        System.out.println("*****************************");
        System.out.println();

        while (true);
    }

}
