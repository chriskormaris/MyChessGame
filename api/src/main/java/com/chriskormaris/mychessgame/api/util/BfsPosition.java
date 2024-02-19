package com.chriskormaris.mychessgame.api.util;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BfsPosition {

	private final String position;
	private final int row;
	private final int column;
	private final int depth;
	private BfsPosition parentBfsPosition;

	public BfsPosition(String position, int row, int column, int depth) {
		this.position = position;
		this.row = row;
		this.column = column;
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "position: " + position + ", row: " + row + ", column: " + column + ", depth: " + depth;
	}

}
