package gui_tests;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.gui.ChessGUI;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class InsufficientMaterialDrawTest {

    @Test
    public void testInsufficientMaterialDraw() {
        String title = "Insufficient Material Draw Test";

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);

        ChessGUI.makeChessBoardSquaresEmpty();
        ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

        ChessGUI.placePieceToPosition("A1", new King(Allegiance.WHITE));
        ChessGUI.placePieceToPosition("A2", new Bishop(Allegiance.WHITE));
        // ChessGUI.placePieceToPosition("A3", new Knight(Allegiance.WHITE));
        // ChessGUI.placePieceToPosition("A4", new Knight(Allegiance.WHITE));

        ChessGUI.placePieceToPosition("H8", new King(Allegiance.BLACK));
        // ChessGUI.placePieceToPosition("H7", new Bishop(Allegiance.BLACK));
        ChessGUI.placePieceToPosition("H6", new Knight(Allegiance.BLACK));
        ChessGUI.placePieceToPosition("H5", new Knight(Allegiance.BLACK));

        System.out.println();
        ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

        boolean isDraw = ChessGUI.chessBoard.checkForInsufficientMaterialDraw();
        assertTrue("The game is not a draw.", isDraw);
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
