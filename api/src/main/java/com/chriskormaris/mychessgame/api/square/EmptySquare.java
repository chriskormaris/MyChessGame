package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;


public class EmptySquare extends ChessSquare {

	public EmptySquare() {
		super(Allegiance.NONE);
	}

	@Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		return new HashSet<>();
	}

}
