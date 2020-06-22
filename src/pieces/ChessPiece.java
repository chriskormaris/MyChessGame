package pieces;

import java.util.Set;

import chess.ChessBoard;

public abstract class ChessPiece {

	public int pieceCode;
	
	public ChessPiece() {
		
	}
	
	public ChessPiece(int pieceCode) {
		this.pieceCode = pieceCode;
	}

	public abstract Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats);
	
}
