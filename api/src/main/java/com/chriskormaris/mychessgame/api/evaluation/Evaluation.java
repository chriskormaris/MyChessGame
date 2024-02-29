package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;

public interface Evaluation {

	int PIECE_VALUE_MULTIPLIER = 2;

	int PAWN_GAME_PHASE_VALUE = 0;
	int KNIGHT_GAME_PHASE_VALUE = 1;
	int BISHOP_GAME_PHASE_VALUE = 1;
	int ROOK_GAME_PHASE_VALUE = 2;
	int QUEEN_GAME_PHASE_VALUE = 4;
	int KING_GAME_PHASE_VALUE = 0;

	double evaluate(ChessBoard chessBoard);

	// At the start of the game, the sum of all pieces' "gamePhase" values should be equal to 24.
	// In case of early promotion, the sum could be more than 24.
	default int getPieceGamePhaseValue(ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return PAWN_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Knight) {
			return KNIGHT_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Bishop) {
			return BISHOP_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Rook) {
			return ROOK_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Queen) {
			return QUEEN_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof King) {
			return KING_GAME_PHASE_VALUE;
		}
		return 0;
	}

}
