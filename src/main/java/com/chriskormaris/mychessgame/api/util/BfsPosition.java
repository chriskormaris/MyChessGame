package com.chriskormaris.mychessgame.api.util;


public class BfsPosition {

	private String position;
	private int row;
	private int column;
	private int depth;
	private BfsPosition parentBfsPosition;

	public BfsPosition() {
		this.position = "A1";
		this.row = 0;
		this.column = 0;
		this.depth = 0;
	}

	public BfsPosition(String position, int row, int column, int depth) {
		this.position = position;
		this.row = row;
		this.column = column;
		this.depth = depth;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getRow() {
		return this.row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return this.column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getDepth() {
		return this.depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public BfsPosition getParentBfsPosition() {
		return this.parentBfsPosition;
	}

	public void setParentBfsPosition(BfsPosition parentBfsPosition) {
		this.parentBfsPosition = parentBfsPosition;
	}

	@Override
	public String toString() {
		return "position: " + position + ", row: " + row + ", column: " + column + ", depth: " + depth;
		// return "position: " + position + ", row: " + row + ", column: " + column;
	}

}

