package pieces;

import java.util.Set;

import chess.ChessBoard;
import enumerations.Allegiance;
import utilities.Constants;

public abstract class ChessPiece {

	// 3 enumerations: WHITE, BLACK, EMPTY
	private Allegiance allegiance;
	
	// Always positive, but the getter returns a negative value, 
	// if the allegiance is Black.  
	private char chessPieceChar;
	
	public ChessPiece() {
		
	}
	
	public ChessPiece(Allegiance allegiance) {
		this.allegiance = allegiance;
		if (allegiance == Allegiance.WHITE) {
			if (this instanceof Pawn) {
				this.chessPieceChar = Constants.WHITE_PAWN;
			} else if (this instanceof Rook) {
				this.chessPieceChar = Constants.WHITE_ROOK;
			} else if (this instanceof Knight) {
				this.chessPieceChar = Constants.WHITE_KNIGHT;
			} else if (this instanceof Bishop) {
				this.chessPieceChar = Constants.WHITE_BISHOP;
			} else if (this instanceof Queen) {
				this.chessPieceChar = Constants.WHITE_QUEEN;
			} else if (this instanceof King) {
				this.chessPieceChar = Constants.WHITE_KING;
			}
		} else if (allegiance == Allegiance.BLACK) {
			if (this instanceof Pawn) {
				this.chessPieceChar = Constants.BLACK_PAWN;
			} else if (this instanceof Rook) {
				this.chessPieceChar = Constants.BLACK_ROOK;
			} else if (this instanceof Knight) {
				this.chessPieceChar = Constants.BLACK_KNIGHT;
			} else if (this instanceof Bishop) {
				this.chessPieceChar = Constants.BLACK_BISHOP;
			} else if (this instanceof Queen) {
				this.chessPieceChar = Constants.BLACK_QUEEN;
			} else if (this instanceof King) {
				this.chessPieceChar = Constants.BLACK_KING;
			}
		} else {
			this.chessPieceChar = '-';
		}
	}
	
	public Allegiance getAllegiance() {
		return allegiance;
	}

	public void setAllegiance(Allegiance allegiance) {
		this.allegiance = allegiance;
	}

	// Positive for White, negative for Black, 0 for Empty.
	public char getChessPieceChar() {
		return chessPieceChar;
	}
	
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
			
			if (this instanceof Pawn) {
				returnString += "Pawn";
			} else if (this instanceof Rook) {
				returnString += "Rook";
			} else if (this instanceof Knight) {
				returnString += "Knight";
			} else if (this instanceof Bishop) {
				returnString += "Bishop";
			}
			
			return returnString;
		}
		
	}
	
}
