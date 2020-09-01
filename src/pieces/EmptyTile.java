package pieces;

import java.util.Set;

import chess.ChessBoard;
import enumerations.Allegiance;


public class EmptyTile extends ChessPiece {
	
	public EmptyTile() {
		super(Allegiance.EMPTY);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		return null;
	}

}
