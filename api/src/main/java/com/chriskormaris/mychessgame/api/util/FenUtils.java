package com.chriskormaris.mychessgame.api.util;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.exception.InvalidFenPositionException;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
import com.chriskormaris.mychessgame.api.square.EmptySquare;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FenUtils {

	public static ChessBoard getChessBoardFromFenPosition(String fenPosition) {
		return getChessBoardFromFenPosition(fenPosition, Constants.DEFAULT_NUM_OF_ROWS);
	}

	public static ChessBoard getChessBoardFromFenPosition(String fenPosition, int numOfRows) {
		fenPosition = fenPosition.trim();
		String[] fenPositionTokens = fenPosition.split(" ");
		if (fenPositionTokens.length != 6) {
			throw new InvalidFenPositionException(
					"FEN position must contain 5 white spaces in between. White spaces in between: "
							+ (fenPositionTokens.length - 1)
			);
		}

		String startingPieces = fenPositionTokens[0];

		String nextPlayerChar = fenPositionTokens[1];
		boolean whitePlays;
		if (nextPlayerChar.equals("w")) {
			whitePlays = true;
		} else if (nextPlayerChar.equals("b")) {
			whitePlays = false;
		} else {
			throw new InvalidFenPositionException(
					"Invalid player character. It should be \"w\" or \"b\", NOT: \"" + nextPlayerChar + "\"!"
			);
		}

		String castlingAvailability = fenPositionTokens[2];
		boolean whiteKingMoved = false;
		boolean leftWhiteRookMoved = false;
		boolean rightWhiteRookMoved = false;
		boolean blackKingMoved = false;
		boolean leftBlackRookMoved = false;
		boolean rightBlackRookMoved = false;

		if (castlingAvailability.contains(String.valueOf(Constants.WHITE_KING))
				&& !castlingAvailability.contains(String.valueOf(Constants.WHITE_QUEEN))) {
			leftWhiteRookMoved = true;
			whiteKingMoved = true;
		} else if (!castlingAvailability.contains(String.valueOf(Constants.WHITE_KING))
				&& castlingAvailability.contains(String.valueOf(Constants.WHITE_QUEEN))) {
			rightWhiteRookMoved = true;
			whiteKingMoved = true;
		} else if (!castlingAvailability.contains(String.valueOf(Constants.WHITE_KING))
				&& !castlingAvailability.contains(String.valueOf(Constants.WHITE_QUEEN))) {
			whiteKingMoved = true;
			leftWhiteRookMoved = true;
			rightWhiteRookMoved = true;
		}
		if (castlingAvailability.contains(String.valueOf(Constants.BLACK_KING))
				&& !castlingAvailability.contains(String.valueOf(Constants.BLACK_QUEEN))) {
			leftBlackRookMoved = true;
			blackKingMoved = true;
		} else if (!castlingAvailability.contains(String.valueOf(Constants.BLACK_KING))
				&& castlingAvailability.contains(String.valueOf(Constants.BLACK_QUEEN))) {
			rightBlackRookMoved = true;
			blackKingMoved = true;
		} else if (!castlingAvailability.contains(String.valueOf(Constants.BLACK_KING))
				&& !castlingAvailability.contains(String.valueOf(Constants.BLACK_QUEEN))) {
			blackKingMoved = true;
			leftBlackRookMoved = true;
			rightBlackRookMoved = true;
		}

		String enPassantPosition = fenPositionTokens[3].toUpperCase();

		int halfMoveClock;
		try {
			halfMoveClock = Integer.parseInt(fenPositionTokens[4]);
		} catch (NumberFormatException ex) {
			throw new InvalidFenPositionException("Provided half-move clock value is not a number!");
		}

		int fullMoveNumber;
		try {
			fullMoveNumber = Integer.parseInt(fenPositionTokens[5]);
		} catch (NumberFormatException ex) {
			throw new InvalidFenPositionException("Provided full-move counter value is not a number!");
		}

		int halfMoveNumber = fullMoveNumber * 2;
		if (whitePlays) {
			halfMoveNumber -= 1;
		}

		ChessBoard chessBoard = new ChessBoard(numOfRows);

		// Set the ChessBoard parameters, according to the given FEN position.
		if (!fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			populateGameBoard(chessBoard, startingPieces);
		}

		chessBoard.setPlayer(whitePlays);

		chessBoard.setWhiteKingMoved(whiteKingMoved);
		chessBoard.setLeftWhiteRookMoved(leftWhiteRookMoved);
		chessBoard.setRightWhiteRookMoved(rightWhiteRookMoved);
		chessBoard.setBlackKingMoved(blackKingMoved);
		chessBoard.setLeftBlackRookMoved(leftBlackRookMoved);
		chessBoard.setRightBlackRookMoved(rightBlackRookMoved);

		chessBoard.setEnPassantPosition(enPassantPosition);

		chessBoard.setHalfMoveClock(halfMoveClock);

		chessBoard.setHalfMoveNumber(halfMoveNumber);

		return chessBoard;
	}

	public static void populateGameBoard(ChessBoard chessBoard, String startingPieces) {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				chessBoard.getGameBoard()[i][j] = new EmptySquare();
			}
		}
		chessBoard.setWhiteKingPosition("Z0");
		chessBoard.setBlackKingPosition("Z0");

		int counter = 0;
		int i = 0, j = 0;
		while (counter < startingPieces.length()) {
			char pieceChar = startingPieces.charAt(counter);

			j = j % chessBoard.getNumOfColumns();

			if (Character.isDigit(pieceChar)) {
				j = j + Character.getNumericValue(pieceChar);
				counter++;
				continue;
			} else if (pieceChar == '/') {
				i++;
				j = 0;
				counter++;
				continue;
			}

			try {
				chessBoard.getGameBoard()[i][j] = Utilities.getChessPiece(pieceChar);
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new RuntimeException(ex);
			}

			if (pieceChar == Constants.WHITE_KING) {
				chessBoard.setWhiteKingPosition(chessBoard.getPositionByRowCol(i, j));
			} else if (pieceChar == Constants.BLACK_KING) {
				chessBoard.setBlackKingPosition(chessBoard.getPositionByRowCol(i, j));
			}

			j++;
			counter++;
		}
	}

	public static String getFenPositionFromChessBoard(ChessBoard chessBoard) {
		StringBuilder fenPosition = new StringBuilder();

		/* Step 1: Append the chess gameBoard pieces positions */
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			int emptySquaresCounter = 0;
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				// Get the chessPiece in the indices [i][j], from the gameBoard.
				ChessSquare chessSquare = chessBoard.getGameBoard()[i][j];
				// Convert chessPiece value to chessPiece character.
				char pieceChar = Utilities.getPieceChar(chessSquare);
				if (pieceChar != '-') {
					if (emptySquaresCounter != 0) {
						// Append the number of empty consecutive empty squares in a row
						// and then, the chessPiece character.
						fenPosition.append(emptySquaresCounter).append(pieceChar);
					} else {
						// Append the chessPiece character.
						fenPosition.append(pieceChar);
					}
					emptySquaresCounter = 0;
				} else {
					emptySquaresCounter++;
				}
			}
			if (emptySquaresCounter != 0) {
				// Append the chessPiece character.
				fenPosition.append(emptySquaresCounter);
			}
			if (i < chessBoard.getNumOfRows() - 1) {
				// Append the row terminator character.
				fenPosition.append('/');
			}
		}

		/* Step 2: Append the player who plays next. */
		fenPosition.append(" ").append(chessBoard.whitePlays() ? 'w' : 'b');

		/* Step 3: Append the available castling moves. */
		if (chessBoard.isWhiteKingSideCastlingAvailable() || chessBoard.isWhiteQueenSideCastlingAvailable()
				|| chessBoard.isBlackKingSideCastlingAvailable() || chessBoard.isBlackQueenSideCastlingAvailable()) {
			fenPosition.append(" ").append(chessBoard.isWhiteKingSideCastlingAvailable() ? 'K' : "");
			fenPosition.append(chessBoard.isWhiteQueenSideCastlingAvailable() ? 'Q' : "");
			fenPosition.append(chessBoard.isBlackKingSideCastlingAvailable() ? 'k' : "");
			fenPosition.append(chessBoard.isBlackQueenSideCastlingAvailable() ? 'q' : "");
		} else {
			fenPosition.append(" -");
		}

		/* Step 4: Append the current "en passant" position. */
		fenPosition.append(" ").append(chessBoard.getEnPassantPosition().toLowerCase());

		/* Step 5: Append the half-move clock. */
		fenPosition.append(" ").append(chessBoard.getHalfMoveClock());

		/* Step 6: Append the full-move number. */
		fenPosition.append(" ").append((int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2));

		return fenPosition.toString();
	}

	// Returns the FEN position, but without the half-move clock and the full-move number.
	public static String skipCounters(String fenPosition) {
		String[] fenPositionParts = fenPosition.split(" ");
		StringBuilder formattedFenPosition = new StringBuilder();
		for (int i = 0; i < fenPositionParts.length; i++) {
			if (i < fenPositionParts.length - 2) {
				if (i > 0) {
					formattedFenPosition.append(" ");
				}
				formattedFenPosition.append(fenPositionParts[i]);
			}
		}
		return formattedFenPosition.toString();
	}

}
