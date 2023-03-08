package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Utilities;
import lombok.Getter;

import java.util.Set;

@Getter
public abstract class ChessPiece {

	// 3 enumerations: WHITE, BLACK, NONE
	private final Allegiance allegiance;

	// A capital letter if the allegiance is White,
	// a small letter if the allegiance is Black.
	private final char pieceChar;

	// True if the piece was initially a Pawn and got promoted.
	private final boolean isPromoted;

	public ChessPiece(Allegiance allegiance) {
		this.allegiance = allegiance;
		this.isPromoted = false;
		this.pieceChar = Utilities.getPieceChar(this);
	}

	public ChessPiece(Allegiance allegiance, boolean isPromoted) {
		this.allegiance = allegiance;
		this.isPromoted = isPromoted;
		this.pieceChar = Utilities.getPieceChar(this);
	}

	public abstract Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats);

	@Override
	public String toString() {
		if (this instanceof EmptySquare) {
			return "Empty Square";
		} else {
			String returnString = "";
			if (this.allegiance == Allegiance.WHITE) {
				returnString += "White ";
			} else if (this.allegiance == Allegiance.BLACK) {
				returnString += "Black ";
			}

			if (this instanceof King) {
				returnString += "King";
			} else if (this instanceof Queen) {
				returnString += "Queen";
			} else if (this instanceof Rook) {
				returnString += "Rook";
			} else if (this instanceof Bishop) {
				returnString += "Bishop";
			} else if (this instanceof Knight) {
				returnString += "Knight";
			} else if (this instanceof Pawn) {
				returnString += "Pawn";
			}

			return returnString;
		}
	}

}
