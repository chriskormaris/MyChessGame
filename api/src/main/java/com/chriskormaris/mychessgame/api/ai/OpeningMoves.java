package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.util.FenUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OpeningMoves {

	private static final Map<String, List<Move>> WHITE_OPENING_MOVES = new HashMap<>();
	private static final Map<String, List<Move>> BLACK_OPENING_MOVES = new HashMap<>();

	private static final Random RANDOM = new Random();

	// Initialize White opening moves.
	static {
		List<Move> movesList = new ArrayList<>();

		// movesList.add(new Move("F2", "F4"));  // A02 Bird's opening
		// movesList.add(new Move("G1", "F3"));  // A04 Reti opening
		// movesList.add(new Move("C2", "C4"));  // A10 English opening
		movesList.add(new Move("D2", "D4"));  // A40 Queen's pawn
		movesList.add(new Move("E2", "E4"));  // B00 King's pawn opening
		WHITE_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D2", "D4"));  // B00 King's pawn opening
		WHITE_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/3p4/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B1", "C3"));  // B07 Pirc defence
		WHITE_OPENING_MOVES.put("rnbqkb1r/ppp1pppp/3p1n2/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 1 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("F1", "C4"));  // B20 Sicilian defence
		WHITE_OPENING_MOVES.put("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D2", "D4"));  // C20 King's pawn game
		movesList.add(new Move("F1", "C4"));  // C23 Bishop's opening
		movesList.add(new Move("F2", "F4"));  // C30 King's gambit
		movesList.add(new Move("G1", "F3"));  // C40 King's knight opening
		WHITE_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("C2", "C3"));  // C21 Danish gambit
		WHITE_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/8/3pP3/8/PPP2PPP/RNBQKBNR w KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D1", "F3"));  // C23 Bishop's opening
		movesList.add(new Move("D1", "H5"));  // C23 Bishop's opening
		WHITE_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/8/PPPP1PPP/RNBQK1NR w KQkq - 2 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G1", "F3"));  // C34 King's Gambit Accepted
		WHITE_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/8/4Pp2/8/PPPP2PP/RNBQKBNR w KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("F3", "E5"));  // C42 Petrov's defence
		movesList.add(new Move("D2", "D4"));  // C43 Petrov, modern (Steinitz) attack
		WHITE_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("C2", "C3"));  // C44 Ponziani opening
		movesList.add(new Move("D2", "D4"));  // C44 Scotch opening
		movesList.add(new Move("F1", "C4"));  // C50 Italian Game
		movesList.add(new Move("F1", "B5"));  // C60 Ruy Lopez (Spanish opening)
		WHITE_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("C1", "F4"));  // D00 Queen's pawn, Mason variation
		movesList.add(new Move("C2", "C4"));  // D06 Queen's Gambit
		WHITE_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/8/3p4/3P4/8/PPP1PPPP/RNBQKBNR w KQkq d6 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G1", "F3"));  // D02 London System
		WHITE_OPENING_MOVES.put("rnbqkb1r/ppp1pppp/5n2/3p4/3P1B2/8/PPP1PPPP/RN1QKBNR w KQkq - 2 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("E2", "E3"));  // D20 Queen's gambit accepted
		WHITE_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/8/8/2pP4/8/PP2PPPP/RNBQKBNR w KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B1", "C3"));  // C00 French defence
		movesList.add(new Move("D2", "D4"));  // C00 French defence
		movesList.add(new Move("G1", "F3"));  // C00 French defence
		WHITE_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/4p3/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("E4", "D5"));  // B01 Scandinavian (centre counter) defence
		WHITE_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B1", "C3"));  // B01 Scandinavian (centre counter) defence
		WHITE_OPENING_MOVES.put("rnb1kbnr/ppp1pppp/8/3q4/8/8/PPPP1PPP/RNBQKBNR w KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D1", "E2"));  // C42 Petrov, Damiano variation
		WHITE_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/8/4N3/4n3/8/PPPP1PPP/RNBQKB1R w KQkq - 0 4", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("C2", "C4"));  // A50 Queen's pawn game
		WHITE_OPENING_MOVES.put("rnbqkb1r/pppppppp/5n2/8/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 1 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B2", "C3"));  // E61 King's Indian defence, 3.Nc3
		WHITE_OPENING_MOVES.put("rnbqkb1r/pppppp1p/5np1/8/2PP4/8/PP2PPPP/RNBQKBNR w KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B1", "C3"));  // E00 Queen's pawn game
		movesList.add(new Move("G2", "G3"));  // E00 Catalan opening
		WHITE_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/4pn2/8/2PP4/8/PP2PPPP/RNBQKBNR w KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D2", "D4"));  // B12 Caro-Kann defence
		WHITE_OPENING_MOVES.put("rnbqkbnr/pp1ppppp/2p5/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("F1", "G2"));  // E00 Catalan opening
		WHITE_OPENING_MOVES.put("rnbqkb1r/ppp2ppp/4pn2/3p4/2PP4/6P1/PP2PP1P/RNBQKBNR w KQkq d6 0 4", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("F3", "D4"));  // C44 Scotch opening
		WHITE_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/8/3pP3/5N2/PPP2PPP/RNBQKB1R w KQkq - 0 4", movesList);
	}

	// Initialize Black opening moves.
	static {
		List<Move> movesList = new ArrayList<>();

		movesList.add(new Move("E7", "E5"));  // A04 Reti opening
		movesList.add(new Move("G8", "F6"));  // A05 Reti opening
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("E7", "E5"));  // A20 English opening
		movesList.add(new Move("C7", "C5"));  // A30 English, symmetrical variation
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/2P5/8/PP1PPPPP/RNBQKBNR b KQkq c3 0 1", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // A45 Queen's pawn game
		movesList.add(new Move("F7", "F5"));  // A80 Dutch
		movesList.add(new Move("D7", "D5"));  // D00 Queen's pawn game
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B8", "C6"));  // B00 KP, Nimzovich defence
		movesList.add(new Move("D7", "D5"));  // B01 Scandinavian (centre counter) defence
		movesList.add(new Move("D7", "D6"));  // B07 Pirc defence
		movesList.add(new Move("C7", "C6"));  // B10 Caro-Kann defence
		movesList.add(new Move("C7", "C5"));  // B20 Sicilian defence
		movesList.add(new Move("E7", "E6"));  // C00 French defence
		movesList.add(new Move("E7", "E5"));  // C20 King's pawn game
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // B00 King's pawn opening
		BLACK_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/3p4/8/3PP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G7", "G6"));  // B07 Pirc defence
		BLACK_OPENING_MOVES.put("rnbqkb1r/ppp1pppp/3p1n2/8/3PP3/2N5/PPP2PPP/R1BQKBNR b KQkq - 2 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("E5", "D4"));  // C21 Centre game
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/3PP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B8", "C6"));  // C23 Bishop's opening
		movesList.add(new Move("G8", "F6"));  // C24 Bishop's opening, Berlin defence
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/2B1P3/8/PPPP1PPP/RNBQK1NR b KQkq - 1 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // C23 Bishop's opening
		movesList.add(new Move("F7", "F6"));  // C23 Bishop's opening
		BLACK_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5Q2/PPPP1PPP/RNB1K1NR b KQkq - 3 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // C23 Bishop's opening
		movesList.add(new Move("G7", "G6"));  // C23 Bishop's opening
		BLACK_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/4p2Q/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 3 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("E5", "F4"));  // C33 King's gambit accepted
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/4PP2/8/PPPP2PP/RNBQKBNR b KQkq f3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D8", "E7"));  // C40 Gunderam defence
		movesList.add(new Move("D7", "D6"));  // C41 Philidor's defence
		movesList.add(new Move("G8", "F6"));  // C42 Petrov's defence
		movesList.add(new Move("B8", "C6"));  // C44 King's pawn game
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // C65 Ruy Lopez, Berlin defence
		BLACK_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // D00 Queen's pawn, Mason variation
		BLACK_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/8/3p4/3P1B2/8/PPP1PPPP/RN1QKBNR b KQkq - 1 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("G8", "F6"));  // D06 Queen's Gambit Declined, Marshall defence
		movesList.add(new Move("C7", "C6"));  // D10 Queen's Gambit Declined Slav defence
		movesList.add(new Move("D5", "C4"));  // D20 Queen's gambit accepted
		movesList.add(new Move("E7", "E6"));  // D30 Queen's gambit declined
		movesList.add(new Move("C8", "F5"));  // D06 Queen's Gambit Declined, Grau (Sahovic) defence
		BLACK_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/8/3p4/2PP4/8/PP2PPPP/RNBQKBNR b KQkq c3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D7", "D5"));  // C00 French defence
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/4p3/8/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D8", "D5"));  // B01 Scandinavian (centre counter) defence
		movesList.add(new Move("G8", "F6"));  // B01 Scandinavian defence
		BLACK_OPENING_MOVES.put("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D5", "D8"));  // B01 Scandinavian (centre counter) defence
		BLACK_OPENING_MOVES.put("rnb1kbnr/ppp1pppp/8/3q4/8/2N5/PPPP1PPP/R1BQKBNR b KQkq - 1 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D7", "D6"));  // C42 Petrov's defence
		BLACK_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/5n2/4N3/4P3/8/PPPP1PPP/RNBQKB1R b KQkq - 0 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("E7", "E6"));  // E00 Queen's pawn game
		movesList.add(new Move("G7", "G6"));  // E60 King's Indian defence
		BLACK_OPENING_MOVES.put("rnbqkb1r/pppppppp/5n2/8/2PP4/8/PP2PPPP/RNBQKBNR b KQkq c3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("F8", "G7"));  // E61 King's Indian defence, 3.Nc3
		BLACK_OPENING_MOVES.put("rnbqkb1r/pppppp1p/5np1/8/2PP4/2N5/PP2PPPP/R1BQKBNR b KQkq - 1 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("F8", "B4"));  // E20 Nimzo-Indian defence
		BLACK_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/4pn2/8/2PP4/2N5/PP2PPPP/R1BQKBNR b KQkq - 1 3", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D7", "D5"));  // B12 Caro-Kann defence
		BLACK_OPENING_MOVES.put("rnbqkbnr/pp1ppppp/2p5/8/3PP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D7", "D5"));  // C00 French defence
		BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/4p3/8/3PP3/8/PPP2PPP/RNBQKBNR b KQkq d3 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D8", "E7"));  // C42 Petrov, Damiano variation
		BLACK_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/8/4N3/4n3/8/PPPPQPPP/RNB1KB1R b KQkq - 1 4", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D7", "D6"));  // C42 Petrov, Damiano variation
		BLACK_OPENING_MOVES.put("rnb1kb1r/ppppqppp/8/4N3/4Q3/8/PPPP1PPP/RNB1KB1R b KQkq - 0 5", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D7", "D5"));  // E00 Catalan opening
		BLACK_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/4pn2/8/2PP4/6P1/PP2PP1P/RNBQKBNR b KQkq - 0 3", movesList);
	}

	// Returns null, if the "moveIndex" exceeds the size of "movesList",
	// or if the "fenPosition" is not the key of any Map.
	public static Move getNextMove(ChessBoard chessBoard) {
		if (chessBoard.getHalfMoveNumber() > 8) return null;

		String fenPosition = FenUtils.getFenPositionFromChessBoard(chessBoard);

		if (chessBoard.whitePlays()) {
			if (WHITE_OPENING_MOVES.containsKey(fenPosition)) {
				List<Move> movesList = WHITE_OPENING_MOVES.get(fenPosition);
				int moveIndex = RANDOM.nextInt(movesList.size() + 1);
				if (moveIndex < movesList.size()) {
					return movesList.get(moveIndex);
				}
			}
		} else {
			if (BLACK_OPENING_MOVES.containsKey(fenPosition)) {
				List<Move> movesList = BLACK_OPENING_MOVES.get(fenPosition);
				int moveIndex = RANDOM.nextInt(movesList.size() + 1);
				if (moveIndex < movesList.size()) {
					return movesList.get(moveIndex);
				}
			}
		}

		return null;
	}

}
