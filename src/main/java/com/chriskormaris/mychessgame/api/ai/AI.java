package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;

public abstract class AI {

	// Variable that holds which player plays.
	private final boolean aiPlayer;

	public AI(boolean aiPlayer) {
		this.aiPlayer = aiPlayer;
	}

	public boolean whitePlays() {
		return aiPlayer;
	}

	public boolean blackPlays() {
		return !aiPlayer;
	}

	public abstract Move getNextMove(ChessBoard chessBoard);

}
