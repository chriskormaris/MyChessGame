package pieces;

import java.util.Set;

import chess.ChessBoard;
import enums.Allegiance;
import utilities.Constants;

public abstract class ChessPiece {

	// 3 enumerations: WHITE, BLACK, EMPTY
	private Allegiance allegiance;
	
	// Always positive, but the getter returns a negative value, 
	// if the allegiance is Black.  
	private int pieceCode;
	
	public ChessPiece() {
		
	}
	
	public ChessPiece(Allegiance allegiance) {
		this.allegiance = allegiance;
	}
	
	public ChessPiece(int pieceCode) {
		this.pieceCode = pieceCode;
		if (pieceCode > 0) {
			this.allegiance = Allegiance.WHITE;
		} else if (pieceCode < 0) {
			this.allegiance = Allegiance.BLACK;
		} else {
			this.allegiance = Allegiance.EMPTY;
		}
	}
	
	public ChessPiece(Allegiance allegiance, int pieceCode) {
		this.allegiance = allegiance;
		if (allegiance == Allegiance.WHITE)
			this.pieceCode = Math.abs(pieceCode);
		else if (allegiance == Allegiance.BLACK)
			this.pieceCode = -Math.abs(pieceCode);
		else
			this.pieceCode = Constants.EMPTY;
	}
	
	public Allegiance getAllegiance() {
		return allegiance;
	}

	public void setAllegiance(Allegiance allegiance) {
		this.allegiance = allegiance;
	}

	// Positive for White, negative for Black, 0 for Empty.
	public int getPieceCode() {
		if (allegiance == Allegiance.WHITE)
			return Math.abs(pieceCode);
		else if (allegiance == Allegiance.BLACK)
			return -Math.abs(pieceCode);
		else
			return Constants.EMPTY;
	}

	public void setPieceCode(int pieceCode) {
		this.pieceCode = pieceCode;
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
