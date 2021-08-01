package piece;

import chess.ChessBoard;
import enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;


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
		return new HashSet<>();
	}

}
