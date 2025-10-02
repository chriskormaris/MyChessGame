package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.square.Knight;
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
		movesList.add(new Move("G1", "F3"));  // B27 1. e4 c5 2. Nf3 - Sicilian Opening
		WHITE_OPENING_MOVES.put("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("D2", "D4"));  // C20 King's pawn game
        movesList.add(new Move("F1", "C4"));  // C23 Bishop's opening
		movesList.add(new Move("B1", "C3"));  // C25 Vienna game
		movesList.add(new Move("F2", "F4"));  // C30 King's gambit
		movesList.add(new Move("G1", "F3"));  // C40 King's knight opening
		WHITE_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F2", "F4"));  // C26 Vienna, Falkbeer variation
        WHITE_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/5n2/4p3/4P3/2N5/PPPP1PPP/R1BQKBNR w KQkq - 2 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("E4", "E5"));  // C26 Vienna, Falkbeer variation
        WHITE_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/5n2/8/4Pp2/2N5/PPPP2PP/R1BQKBNR w KQkq - 0 4", movesList);

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
		movesList.add(new Move("B1", "C3"));  // C46 Three knights game
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

        movesList = new ArrayList<>();
        movesList.add(new Move("F3", "G5"));  // C57 Two knights defence
        WHITE_OPENING_MOVES.put("r1bqkb1r/pppp1ppp/2n2n2/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("E4", "D5"));  // C57 Two knights defence
        WHITE_OPENING_MOVES.put("r1bqkb1r/ppp2ppp/2n2n2/3pp1N1/2B1P3/8/PPPP1PPP/RNBQK2R w KQkq d6 0 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G5", "F7"));  // C57 two knights defence, Fegatello attack
        WHITE_OPENING_MOVES.put("r1bqkb1r/ppp2ppp/2n5/3np1N1/2B5/8/PPPP1PPP/RNBQK2R w KQkq - 0 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D1", "F3"));  // C57 two knights defence, Fegatello attack
        WHITE_OPENING_MOVES.put("r1bq1b1r/ppp2kpp/2n5/3np3/2B5/8/PPPP1PPP/RNBQK2R w KQ - 0 7", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B1", "C3"));  // C57 two knights defence, Fegatello attack
        WHITE_OPENING_MOVES.put("r1bq1b1r/ppp3pp/2n1k3/3np3/2B5/5Q2/PPPP1PPP/RNB1K2R w KQ - 2 8", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B1", "C3"));  // C50 Italian Game
        WHITE_OPENING_MOVES.put("r1bqkbnr/ppp2ppp/2np4/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("H2", "H3"));  // C50 Italian Game
        WHITE_OPENING_MOVES.put("r2qkbnr/ppp2ppp/2np4/4p3/2B1P1b1/2N2N2/PPPP1PPP/R1BQK2R w KQkq - 2 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F3", "E5"));  // C50 Italian Game, Legal Trap
        WHITE_OPENING_MOVES.put("r2qkbnr/ppp2ppp/2np4/4p2b/2B1P3/2N2N1P/PPPP1PP1/R1BQK2R w KQkq - 1 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("C4", "F7"));  // C50 Italian Game, Legal Trap
        WHITE_OPENING_MOVES.put("r2qkbnr/ppp2ppp/2np4/4N3/2B1P3/2N4P/PPPP1PP1/R1BbK2R w KQkq - 0 7", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D2", "D4"));  // B50 Sicilian
        WHITE_OPENING_MOVES.put("rnbqkbnr/pp2pppp/3p4/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F3", "D4"));  // B54 Sicilian
        WHITE_OPENING_MOVES.put("rnbqkbnr/pp2pppp/3p4/8/3pP3/5N2/PPP2PPP/RNBQKB1R w KQkq - 0 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B1", "C3"));  // B56 Sicilian
        WHITE_OPENING_MOVES.put("rnbqkb1r/pp2pppp/3p1n2/8/3NP3/8/PPP2PPP/RNBQKB1R w KQkq - 1 5", movesList);
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
		movesList.add(new Move("E7", "E5"));  // A40 1. d4 e5 - Englund gambit
        BLACK_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("B8", "C6"));  // B00 KP, Nimzovich defence
		movesList.add(new Move("D7", "D5"));  // B01 Scandinavian (centre counter) defence
		movesList.add(new Move("G8", "F6"));  // B02 Alekhine's defence
		movesList.add(new Move("D7", "D6"));  // B07 Pirc defence
		movesList.add(new Move("C7", "C6"));  // B10 Caro-Kann defence
		movesList.add(new Move("C7", "C5"));  // B20 Sicilian defence
		movesList.add(new Move("E7", "E6"));  // C00 French defence
		movesList.add(new Move("E7", "E5"));  // C20 King's pawn game
        BLACK_OPENING_MOVES.put("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G8", "F6"));  // C26 Vienna, Falkbeer variation
        BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4p3/4P3/2N5/PPPP1PPP/R1BQKBNR b KQkq - 1 2", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D7", "D5"));  // C29 Vienna gambit
        BLACK_OPENING_MOVES.put("rnbqkb1r/pppp1ppp/5n2/4p3/4PP2/2N5/PPPP2PP/R1BQKBNR b KQkq f3 0 3", movesList);

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
        movesList.add(new Move("C8", "F5"));  // D06 Queen's Gambit Declined, Grau (Sahovic) defence
		movesList.add(new Move("E7", "E5"));  // D08 Queen's Gambit Declined, Albin counter-gambit
		movesList.add(new Move("C7", "C6"));  // D10 Queen's Gambit Declined Slav defence
		movesList.add(new Move("D5", "C4"));  // D20 Queen's gambit accepted
		movesList.add(new Move("E7", "E6"));  // D30 Queen's gambit declined
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

        movesList = new ArrayList<>();
        movesList.add(new Move("D7", "D6"));  // C50 Italian Game
        movesList.add(new Move("F8", "C5"));  // C50 Giuoco Piano
        movesList.add(new Move("F8", "E7"));  // C50 Hungarian defence
        movesList.add(new Move("G8", "F6"));  // C55 Two knights defence
        movesList.add(new Move("C6", "D4"));  // Blackburne Shilling Gambit Trap
        BLACK_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D7", "D5"));  // C57 Two knights defence
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppp1ppp/2n2n2/4p1N1/2B1P3/8/PPPP1PPP/RNBQK2R b KQkq - 5 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("C6", "A5"));  // C58 two knights defence
        BLACK_OPENING_MOVES.put("r1bqkb1r/ppp2ppp/2n2n2/3Pp1N1/2B5/8/PPPP1PPP/RNBQK2R b KQkq - 0 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("E8", "F7"));  // C57 two knights defence, Fegatello attack
        BLACK_OPENING_MOVES.put("r1bqkb1r/ppp2Npp/2n5/3np3/2B5/8/PPPP1PPP/RNBQK2R b KQkq - 0 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F7", "E6"));  // C57 two knights defence, Fegatello attack
        BLACK_OPENING_MOVES.put("r1bq1b1r/ppp2kpp/2n5/3np3/2B5/5Q2/PPPP1PPP/RNB1K2R b KQ - 1 7", movesList);

		movesList = new ArrayList<>();
		movesList.add(new Move("C6", "B4"));  // C57 two knights defence, Fegatello attack
		BLACK_OPENING_MOVES.put("r1bq1b1r/ppp3pp/2n1k3/3np3/2B5/2N2Q2/PPPP1PPP/R1B1K2R b KQ - 3 8", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B8", "C6"));  // A40 Queen's pawn, Charlick (Englund) gambit
        BLACK_OPENING_MOVES.put("rnbqkbnr/pppp1ppp/8/4P3/8/8/PPP1PPPP/RNBQKBNR b KQkq - 0 2", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D8", "E7"));  // A40 Queen's pawn, Charlick (Englund) gambit
        BLACK_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/2n5/4P3/8/5N2/PPP1PPPP/RNBQKB1R b KQkq - 2 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("E7", "B4"));  // A40 Queen's pawn, Charlick (Englund) gambit (Trap)
        BLACK_OPENING_MOVES.put("r1b1kbnr/ppppqppp/2n5/4P3/5B2/5N2/PPP1PPPP/RN1QKB1R b KQkq - 4 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B4", "B2"));  // A40 Queen's pawn, Charlick (Englund) gambit (Trap)
        BLACK_OPENING_MOVES.put("r1b1kbnr/pppp1ppp/2n5/4P3/1q6/5N2/PPPBPPPP/RN1QKB1R b KQkq - 6 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F8", "B4"));  // A40 Queen's pawn, Charlick (Englund) gambit (Trap)
        BLACK_OPENING_MOVES.put("r1b1kbnr/pppp1ppp/2n5/4P3/8/2B2N2/PqP1PPPP/RN1QKB1R b KQkq - 1 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("C6", "B4"));  // A40 Queen's pawn, Charlick (Englund) gambit (Trap)
        BLACK_OPENING_MOVES.put("r1b1k1nr/pppp1ppp/2n5/4P3/1B6/5N2/PqP1PPPP/RN1QKB1R b KQkq - 0 7", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D8", "G5"));  // Blackburne Shilling Gambit Trap
        BLACK_OPENING_MOVES.put("r1bqkbnr/pppp1ppp/8/4N3/2BnP3/8/PPPP1PPP/RNBQK2R b KQkq - 0 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G5", "G2"));  // Blackburne Shilling Gambit Trap
        BLACK_OPENING_MOVES.put("r1b1kbnr/pppp1Npp/8/6q1/2BnP3/8/PPPP1PPP/RNBQK2R b KQkq - 0 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F8", "C5"));  // Blackburne Shilling Gambit Trap
        BLACK_OPENING_MOVES.put("r1b1kbnN/pppp2pp/8/8/3nq3/8/PPPPBP1P/RNBQK3 b KQkq - 1 8", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D4", "F3"));  // Blackburne Shilling Gambit Trap
        BLACK_OPENING_MOVES.put("r1b1k1nN/pppp2pp/8/2b5/3nq3/2N5/PPPPBP1P/R1BQK3 b KQkq - 3 9", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G8", "F6"));  // D50 Queen's Gambit Declined, 4.Bg5
        BLACK_OPENING_MOVES.put("rnbqkbnr/ppp2ppp/4p3/3p4/2PP4/2N5/PP2PPPP/R1BQKBNR b KQkq - 1 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B8", "D7"));  // D35 Queen's Gambit Declined, 3...Nf6
        BLACK_OPENING_MOVES.put("rnbqkb1r/ppp2ppp/4pn2/3p2B1/2PP4/2N5/PP2PPPP/R2QKBNR b KQkq - 3 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("E6", "D5"));  // D51 Queen's Gambit Declined, 4.Bg5 Nbd7 (Elephant Trap)
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppn1ppp/4pn2/3P2B1/3P4/2N5/PP2PPPP/R2QKBNR b KQkq - 0 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F6", "D5"));  // D51 Queen's Gambit Declined, 4.Bg5 Nbd7 (Elephant Trap)
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppn1ppp/5n2/3N2B1/3P4/8/PP2PPPP/R2QKBNR b KQkq - 0 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F8", "B4"));  // D51 Queen's Gambit Declined, 4.Bg5 Nbd7 (Elephant Trap)
        BLACK_OPENING_MOVES.put("r1bBkb1r/pppn1ppp/8/3n4/3P4/8/PP2PPPP/R2QKBNR b KQkq - 0 7", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D5", "D4"));  // D08 Queen's Gambit Declined, Albin counter-gambit
        BLACK_OPENING_MOVES.put("rnbqkbnr/ppp2ppp/8/3pP3/2P5/8/PP2PPPP/RNBQKBNR b KQkq - 0 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F8", "B4"));  // D08 Queen's Gambit Declined, Albin counter-gambit
        BLACK_OPENING_MOVES.put("rnbqkbnr/ppp2ppp/8/4P3/2Pp4/4P3/PP3PPP/RNBQKBNR b KQkq - 0 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D4", "E3"));  // D08 Queen's Gambit Declined, Albin counter-gambit, Lasker trap
        BLACK_OPENING_MOVES.put("rnbqk1nr/ppp2ppp/8/4P3/1bPp4/4P3/PP1B1PPP/RN1QKBNR b KQkq - 2 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("E3", "F2"));  // D08 Queen's Gambit Declined, Albin counter-gambit, Lasker trap
        BLACK_OPENING_MOVES.put("rnbqk1nr/ppp2ppp/8/4P3/1BP5/4p3/PP3PPP/RN1QKBNR b KQkq - 0 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F2", "G1", new Knight(Allegiance.BLACK)));  // D08 Queen's Gambit Declined, Albin counter-gambit, Lasker trap
        BLACK_OPENING_MOVES.put("rnbqk1nr/ppp2ppp/8/4P3/1BP5/8/PP2KpPP/RN1Q1BNR b kq - 1 7", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D8", "H4"));  // D08 Queen's Gambit Declined, Albin counter-gambit, Lasker trap
        BLACK_OPENING_MOVES.put("rnbqk1nr/ppp2ppp/8/4P3/1BP5/8/PP4PP/RN1QKBnR b kq - 1 8", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("B8", "C6"));  // D08 Queen's Gambit Declined, Albin counter-gambit, Lasker trap
        BLACK_OPENING_MOVES.put("rnb1k1nr/ppp2ppp/8/4P3/1BP4q/8/PP1K2PP/RN1Q1BnR b kq - 3 9", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("F6", "G4"));  // C65 Ruy Lopez, Berlin defence, 4.O-O
        movesList.add(new Move("F8", "C5"));  // C65 Ruy Lopez, Berlin defence, Beverwijk variation
        movesList.add(new Move("D7", "D6"));  // C66 Ruy Lopez, Berlin defence, 4.O-O, d6
        movesList.add(new Move("F6", "E4"));  // C67 Ruy Lopez, Berlin defence, open variation
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppp1ppp/2n2n2/1B2p3/4P3/5N2/PPPP1PPP/RNBQ1RK1 b kq - 5 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("H7", "H5"));  // C65 Ruy Lopez, Berlin defence, 4.O-O
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppp1ppp/2n5/1B2p3/4P1n1/5N1P/PPPP1PP1/RNBQ1RK1 b kq - 0 5", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("H5", "G4"));  // C65 Ruy Lopez, Berlin defence, 4.O-O (Fishing Pole Trap)
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppp1pp1/2n5/1B2p2p/4P1P1/5N2/PPPP1PP1/RNBQ1RK1 b kq - 0 6", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D8", "H4"));  // C65 Ruy Lopez, Berlin defence, 4.O-O (Fishing Pole Trap)
        BLACK_OPENING_MOVES.put("r1bqkb1r/pppp1pp1/2n5/1B2p3/4P1p1/8/PPPP1PP1/RNBQNRK1 b kq - 1 7", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G4", "G3"));  // C65 Ruy Lopez, Berlin defence, 4.O-O (Fishing Pole Trap)
        BLACK_OPENING_MOVES.put("r1b1kb1r/pppp1pp1/2n5/1B2p3/4P1pq/5P2/PPPP2P1/RNBQNRK1 b kq - 0 8", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("D7", "D6"));  // B50 Sicilian
        BLACK_OPENING_MOVES.put("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("C5", "D4"));  // B50 Sicilian
        BLACK_OPENING_MOVES.put("rnbqkbnr/pp2pppp/3p4/2p5/3PP3/5N2/PPP2PPP/RNBQKB1R b KQkq d3 0 3", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G8", "F6"));  // B54 Sicilian
        BLACK_OPENING_MOVES.put("rnbqkbnr/pp2pppp/3p4/8/3NP3/8/PPP2PPP/RNBQKB1R b KQkq - 0 4", movesList);

        movesList = new ArrayList<>();
        movesList.add(new Move("G7", "G6"));  // B70 Sicilian, dragon variation
        movesList.add(new Move("A7", "A6"));  // B90 Sicilian, Najdorf
        BLACK_OPENING_MOVES.put("rnbqkb1r/pp2pppp/3p1n2/8/3NP3/2N5/PPP2PPP/R1BQKB1R b KQkq - 2 5", movesList);
	}

	// Returns null if the "fenPosition" is not the key of any Map.
	public static Move getNextMove(ChessBoard chessBoard) {
		if (chessBoard.getHalfMoveNumber() > 18) return null;

		String fenPosition = FenUtils.getFenPositionFromChessBoard(chessBoard);

		if (chessBoard.whitePlays()) {
			if (WHITE_OPENING_MOVES.containsKey(fenPosition)) {
				List<Move> movesList = WHITE_OPENING_MOVES.get(fenPosition);
				int moveIndex = RANDOM.nextInt(movesList.size());
                return movesList.get(moveIndex);
			}
		} else {
			if (BLACK_OPENING_MOVES.containsKey(fenPosition)) {
				List<Move> movesList = BLACK_OPENING_MOVES.get(fenPosition);
				int moveIndex = RANDOM.nextInt(movesList.size());
                return movesList.get(moveIndex);
			}
		}

		return null;
	}

}
