package gui_tests;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.gui.ChessGUI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


class TwoKingsLeftTest {

    @Test
    public void testTwoKingsLeft() {

        String title = "Two Kings Left Test";

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);

        ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
        ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
        ChessGUI.placePieceToPosition("B2", new Pawn(Allegiance.WHITE));

        System.out.println();
        ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

        boolean isDraw = ChessGUI.chessBoard.checkForInsufficientMaterialDraw();
        assertFalse(isDraw, "The game is not a draw.");
        System.out.println("*****************************");
        System.out.println();

        ChessGUI.checkForGameOver();

        // Continue playing for a minute.
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
