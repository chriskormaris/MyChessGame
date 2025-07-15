package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;


public class EmptySquare extends ChessSquare {

	@Getter
    private static final EmptySquare instance = new EmptySquare();

	public EmptySquare() {
		super(Allegiance.NONE);
	}

    @Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		return new HashSet<>();
	}

}
