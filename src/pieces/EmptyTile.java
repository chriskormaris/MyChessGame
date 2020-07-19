package pieces;

import java.util.Set;

import chess.Allegiance;
import chess.ChessBoard;
import utilities.Constants;


public class EmptyTile extends ChessPiece {
	
	public EmptyTile() {
		super(Allegiance.EMPTY, Constants.EMPTY);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		return null;
	}

}
