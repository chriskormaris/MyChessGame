package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.Set;

public abstract class ChessPiece {

	// 3 enumerations: WHITE, BLACK, EMPTY
	private Allegiance allegiance;

	// A capital letter if the allegiance is White,
	// a small letter if the allegiance is Black.
	private char chessPieceChar;

	// True if the piece was initially a Pawn and got promoted.
	private boolean isPromoted;

	public ChessPiece() {

	}

	public ChessPiece(Allegiance allegiance) {
		this.allegiance = allegiance;
		this.chessPieceChar = extractChessPieceChar(allegiance);
	}

	public ChessPiece(Allegiance allegiance, boolean isPromoted) {
		this.allegiance = allegiance;
		this.isPromoted = isPromoted;
		this.chessPieceChar = extractChessPieceChar(allegiance);
	}

	private char extractChessPieceChar(Allegiance allegiance) {
		if (allegiance == Allegiance.WHITE) {
			if (this instanceof Pawn) {
				return Constants.WHITE_PAWN;
			} else if (this instanceof Rook) {
				return Constants.WHITE_ROOK;
			} else if (this instanceof Knight) {
				return Constants.WHITE_KNIGHT;
			} else if (this instanceof Bishop) {
				return Constants.WHITE_BISHOP;
			} else if (this instanceof Queen) {
				return Constants.WHITE_QUEEN;
			} else if (this instanceof King) {
				return Constants.WHITE_KING;
			}
		} else if (allegiance == Allegiance.BLACK) {
			if (this instanceof Pawn) {
				return Constants.BLACK_PAWN;
			} else if (this instanceof Rook) {
				return Constants.BLACK_ROOK;
			} else if (this instanceof Knight) {
				return Constants.BLACK_KNIGHT;
			} else if (this instanceof Bishop) {
				return Constants.BLACK_BISHOP;
			} else if (this instanceof Queen) {
				return Constants.BLACK_QUEEN;
			} else if (this instanceof King) {
				return Constants.BLACK_KING;
			}
		}
		return '-';
	}

	public Allegiance getAllegiance() {
		return allegiance;
	}

	public void setAllegiance(Allegiance allegiance) {
		this.allegiance = allegiance;
	}

	// Capital for White, small for Black, '-' for Empty.
	public char getChessPieceChar() {
		return chessPieceChar;
	}

	public boolean isPromoted() {
		return isPromoted;
	}

	public void setPromoted(boolean promoted) {
		isPromoted = promoted;
	}

	public abstract ChessPiece makeCopy();

	public abstract Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats);

	@Override
	public String toString() {
		if (this instanceof EmptyTile) {
			return "Empty Tile";
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
