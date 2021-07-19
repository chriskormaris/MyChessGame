package piece;

import java.util.Set;

import chess.ChessBoard;
import enumeration.Allegiance;


public class EmptyTile extends ChessPiece {
	
	public EmptyTile() {
		super(Allegiance.EMPTY);
	}
	
	@Override
	public ChessPiece makeCopy() {
		return new EmptyTile();
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		return null;
	}

}