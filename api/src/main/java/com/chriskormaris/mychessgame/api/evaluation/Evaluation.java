package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;

public abstract class Evaluation {

	static final int PIECE_VALUE_MULTIPLIER = 2;

	public abstract double evaluate(ChessBoard chessBoard);

}
