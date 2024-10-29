package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.square.ChessSquare;

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
	default int getPieceGamePhaseValue(ChessSquare square) {
		if (square.isPawn()) {
			return PAWN_GAME_PHASE_VALUE;
		} else if (square.isKnight()) {
			return KNIGHT_GAME_PHASE_VALUE;
		} else if (square.isBishop()) {
			return BISHOP_GAME_PHASE_VALUE;
		} else if (square.isRook()) {
			return ROOK_GAME_PHASE_VALUE;
		} else if (square.isQueen()) {
			return QUEEN_GAME_PHASE_VALUE;
		} else if (square.isKing()) {
			return KING_GAME_PHASE_VALUE;
		}
		return 0;
	}

}
