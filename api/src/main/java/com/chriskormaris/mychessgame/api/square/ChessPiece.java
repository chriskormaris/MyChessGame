package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Utilities;
import lombok.Getter;

@Getter
public abstract class ChessPiece extends ChessSquare {

	// A capital letter if the allegiance is White,
	// a small letter if the allegiance is Black.
	private final char pieceChar;

	// True if the piece was initially a Pawn and got promoted.
	private final boolean isPromoted;

	public ChessPiece(Allegiance allegiance) {
		super(allegiance);
		this.isPromoted = false;
		this.pieceChar = Utilities.getPieceChar(this);
	}

	public ChessPiece(Allegiance allegiance, boolean isPromoted) {
		super(allegiance);
		this.isPromoted = isPromoted;
		this.pieceChar = Utilities.getPieceChar(this);
	}

}
