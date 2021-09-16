package ai;
/*
 * Athens 2021
 *
 * Created on : 2021-09-16
 * Author     : Christos Kormaris
 */

import chess_board.Move;
import chess_board.ChessBoard;

public abstract class AI {

    // Variable that holds which player plays.
    private boolean aiPlayer;

    public AI(boolean aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    public boolean getAiPlayer() {
     return aiPlayer;
    }

    public void setAiPlayer(boolean aiPlayer) {
     this.aiPlayer = aiPlayer;
    }

    public abstract Move getNextMove(ChessBoard chessBoard);

}
