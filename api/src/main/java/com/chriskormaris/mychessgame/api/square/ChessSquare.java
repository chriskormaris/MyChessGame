package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class ChessSquare {

	// 3 enumerations: WHITE, BLACK, NONE
	private final Allegiance allegiance;

	public ChessSquare(Allegiance allegiance) {
		this.allegiance = allegiance;
	}

	public boolean isEmpty() {
		return this instanceof EmptySquare;
	}

	public boolean isPiece() {
		return this instanceof ChessPiece;
	}

	public boolean isPawn() {
		return this instanceof Pawn;
	}

	public boolean isKnight() {
		return this instanceof Knight;
	}

	public boolean isBishop() {
		return this instanceof Bishop;
	}

	public boolean isRook() {
		return this instanceof Rook;
	}

	public boolean isQueen() {
		return this instanceof Queen;
	}

	public boolean isKing() {
		return this instanceof King;
	}

	public boolean isWhite() {
		return this.allegiance.isWhite();
	}

	public boolean isBlack() {
		return this.allegiance.isBlack();
	}

	public abstract Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats);

	@Override
	public String toString() {
		if (isEmpty()) {
			return "Empty Square";
		} else {
			String returnString = "";
			if (isWhite()) {
				returnString += "White ";
			} else if (isBlack()) {
				returnString += "Black ";
			}

			if (isKing()) {
				returnString += "King";
			} else if (isQueen()) {
				returnString += "Queen";
			} else if (isRook()) {
				returnString += "Rook";
			} else if (isBishop()) {
				returnString += "Bishop";
			} else if (isKnight()) {
				returnString += "Knight";
			} else if (isPawn()) {
				returnString += "Pawn";
			}

			return returnString;
		}
	}

}
