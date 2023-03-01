package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;

public abstract class Evaluation {

	public abstract double evaluate(ChessBoard chessBoard);

}
