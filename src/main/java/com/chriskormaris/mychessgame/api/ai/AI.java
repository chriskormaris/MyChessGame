package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;

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
