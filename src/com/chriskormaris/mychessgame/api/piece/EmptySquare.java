package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;


public class EmptySquare extends ChessPiece {

	public EmptySquare() {
		super(Allegiance.EMPTY);
	}

	@Override
	public ChessPiece makeCopy() {
		return new EmptySquare();
	}

	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		return new HashSet<>();
	}

}
