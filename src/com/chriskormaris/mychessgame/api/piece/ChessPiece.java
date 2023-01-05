package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Utilities;

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
		this.chessPieceChar = Utilities.getPieceChar(this);
	}

	public ChessPiece(Allegiance allegiance, boolean isPromoted) {
		this.allegiance = allegiance;
		this.isPromoted = isPromoted;
		this.chessPieceChar = Utilities.getPieceChar(this);
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
