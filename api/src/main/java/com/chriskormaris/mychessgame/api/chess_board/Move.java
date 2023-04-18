package com.chriskormaris.mychessgame.api.chess_board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/* A class describing a move in the board
 * Every produced child corresponds to a move,
 * and we need to keep the moves as well as the states. */
@Getter
@Setter
@ToString
public class Move {

	private String positionStart;
	private String positionEnd;

	private double value;

	public Move() {
		this.positionStart = "A1";
		this.positionEnd = "A1";
		this.value = 0;
	}

	public Move(String positionStart, String positionEnd) {
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
		this.value = 0;
	}

	public Move(int value) {
		this.positionStart = "A1";
		this.positionEnd = "A1";
		this.value = value;
	}

	public Move(Move otherMove) {
		this.positionStart = otherMove.getPositionStart();
		this.positionEnd = otherMove.getPositionEnd();
		this.value = otherMove.getValue();
	}

}
