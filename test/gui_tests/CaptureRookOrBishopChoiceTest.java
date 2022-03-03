package gui_tests;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.api.util.Utilities;
import com.chriskormaris.mychessgame.gui.ChessGUI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class CaptureRookOrBishopChoiceTest {

    @Test
    public void testCaptureRookOrBishopChoiceTest() {

        String title = "Capture Rook or Bishop?";

        @SuppressWarnings("unused")
        ChessGUI cbg = new ChessGUI(title);

        ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

        ChessGUI.placePieceToPosition("E1", new King(Allegiance.WHITE));
        ChessGUI.placePieceToPosition("C1", new Bishop(Allegiance.WHITE));
        ChessGUI.placePieceToPosition("F1", new Bishop(Allegiance.WHITE));
        ChessGUI.placePieceToPosition("A1", new Rook(Allegiance.WHITE));

        ChessGUI.placePieceToPosition("E8", new King(Allegiance.BLACK));
        ChessGUI.placePieceToPosition("B3", new Knight(Allegiance.BLACK));

        ChessGUI.chessBoard.setPlayer(Constants.BLACK);
        ChessGUI.aiMove(ChessGUI.miniMaxAI);

        assertTrue(Utilities.getChessPieceFromPosition(ChessGUI.chessBoard.getGameBoard(), "A1") instanceof Knight,
                "The Black Knight did NOT capture the White Rook.");

        // Continue playing for a minute.
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
