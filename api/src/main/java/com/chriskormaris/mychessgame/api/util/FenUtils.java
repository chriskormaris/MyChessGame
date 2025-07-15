package com.chriskormaris.mychessgame.api.util;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Variant;
import com.chriskormaris.mychessgame.api.exception.InvalidFenPositionException;
import com.chriskormaris.mychessgame.api.square.ChessPiece;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
import com.chriskormaris.mychessgame.api.square.EmptySquare;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FenUtils {

	public static ChessBoard getChessBoardFromFenPosition(String fenPosition, Variant variant) {
		return getChessBoardFromFenPosition(fenPosition, Constants.DEFAULT_NUM_OF_ROWS, variant);
	}

	public static ChessBoard getChessBoardFromFenPosition(String fenPosition, int numOfRows, Variant variant) {
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

		if (!castlingAvailability.matches("(K?Q?k?q?)|([A-H]?[A-H]?[a-h]?[a-h]?)|[-]")) {
			throw new InvalidFenPositionException("Invalid castling availability characters. " +
					"Some examples are KQkq, AHah or -.");
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

		ChessBoard chessBoard = new ChessBoard(numOfRows, variant);

		// Set the ChessBoard parameters, according to the given FEN position.
		if (variant != Variant.CHESS_960) {
			if (!fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
				populateGameBoard(chessBoard, startingPieces);
			}
		} else {
			populateGameBoard(chessBoard, startingPieces);
		}

		boolean whiteKingMoved = true;
		boolean leftWhiteRookMoved = true;
		boolean rightWhiteRookMoved = true;
		boolean blackKingMoved = true;
		boolean leftBlackRookMoved = true;
		boolean rightBlackRookMoved = true;

		int leftWhiteRookColumn = 0;
		int rightWhiteRookColumn = 7;
		int leftBlackRookColumn = 0;
		int rightBlackRookColumn = 7;

		int whiteKingColumn = chessBoard.getColumnFromPosition(chessBoard.getWhiteKingPosition());
		int blackKingColumn = chessBoard.getColumnFromPosition(chessBoard.getBlackKingPosition());
		for (char ch : castlingAvailability.toCharArray()) {
			if (ch == Constants.WHITE_KING_CHAR) {
				rightWhiteRookMoved = false;
				whiteKingMoved = false;
			} else if (ch == Constants.WHITE_QUEEN_CHAR) {
				leftWhiteRookMoved = false;
				whiteKingMoved = false;
			} else if (ch == Constants.BLACK_KING_CHAR) {
				rightBlackRookMoved = false;
				blackKingMoved = false;
			} else if (ch == Constants.BLACK_QUEEN_CHAR) {
				leftBlackRookMoved = false;
				blackKingMoved = false;
			} else if (isUpperCaseFile(ch)) {
				int column = ch - (int) 'A';
				if (column < whiteKingColumn) {
					leftWhiteRookColumn = column;
					leftWhiteRookMoved = false;
					whiteKingMoved = false;
				} else if (column > whiteKingColumn) {
					rightWhiteRookColumn = column;
					rightWhiteRookMoved = false;
					whiteKingMoved = false;
				}
			} else if (isLowerCaseFile(ch)) {
				int column = ch - (int) 'a';
				if (column < blackKingColumn) {
					leftBlackRookColumn = column;
					leftBlackRookMoved = false;
					blackKingMoved = false;
				} else if (column > blackKingColumn) {
					rightBlackRookColumn = column;
					rightBlackRookMoved = false;
					blackKingMoved = false;
				}
			}
		}

		chessBoard.setPlayer(whitePlays);

		chessBoard.setLeftWhiteRookColumn(leftWhiteRookColumn);
		chessBoard.setRightWhiteRookColumn(rightWhiteRookColumn);
		chessBoard.setLeftBlackRookColumn(leftBlackRookColumn);
		chessBoard.setRightBlackRookColumn(rightBlackRookColumn);

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
				chessBoard.getGameBoard()[i][j] = EmptySquare.getInstance();
			}
		}
		chessBoard.setWhiteKingPosition("Z0");
		chessBoard.setBlackKingPosition("Z0");

		int counter = 0;
		int i = 0, j = 0;
		while (counter < startingPieces.length()) {
			char symbol = startingPieces.charAt(counter);

			j = j % chessBoard.getNumOfColumns();

			if (Character.isDigit(symbol)) {
				j = j + Character.getNumericValue(symbol);
				counter++;
				continue;
			} else if (symbol == '/') {
				i++;
				j = 0;
				counter++;
				continue;
			}

			try {
				chessBoard.getGameBoard()[i][j] = Utilities.getChessPiece(symbol);
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new RuntimeException(ex);
			}

			if (symbol == Constants.WHITE_KING_CHAR) {
				chessBoard.setWhiteKingPosition(chessBoard.getPositionByRowCol(i, j));
			} else if (symbol == Constants.BLACK_KING_CHAR) {
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
				if (chessSquare.isPiece()) {
					char symbol = ((ChessPiece) chessSquare).getSymbol();
					if (emptySquaresCounter != 0) {
						// Append the number of empty consecutive empty squares in a row
						// and then, the chessPiece character.
						fenPosition.append(emptySquaresCounter).append(symbol);
					} else {
						// Append the chessPiece character.
						fenPosition.append(symbol);
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
			fenPosition.append(" ");
			if (chessBoard.getVariant() != Variant.CHESS_960) {
				fenPosition.append(chessBoard.isWhiteKingSideCastlingAvailable() ? Constants.WHITE_KING_CHAR : "");
				fenPosition.append(chessBoard.isWhiteQueenSideCastlingAvailable() ? Constants.WHITE_QUEEN_CHAR : "");
				fenPosition.append(chessBoard.isBlackKingSideCastlingAvailable() ? Constants.BLACK_KING_CHAR : "");
				fenPosition.append(chessBoard.isBlackQueenSideCastlingAvailable() ? Constants.BLACK_QUEEN_CHAR : "");
			} else {
				fenPosition.append(
						chessBoard.isWhiteQueenSideCastlingAvailable()
								? (char) ((int) 'A' + chessBoard.getLeftWhiteRookColumn())
								: ""
				);
				fenPosition.append(
						chessBoard.isWhiteKingSideCastlingAvailable()
								? (char) ((int) 'A' + chessBoard.getRightWhiteRookColumn())
								: ""
				);
				fenPosition.append(
						chessBoard.isBlackQueenSideCastlingAvailable()
								? (char) ((int) 'a' + chessBoard.getLeftBlackRookColumn())
								: ""
				);
				fenPosition.append(
						chessBoard.isBlackKingSideCastlingAvailable()
								? (char) ((int) 'a' + chessBoard.getRightBlackRookColumn())
								: ""
				);
			}
		} else {
			fenPosition.append(" ").append("-");
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

	private static boolean isUpperCaseFile(char ch) {
		return ch >= 'A' && ch <= 'H';
	}

	private static boolean isLowerCaseFile(char ch) {
		return ch >= 'a' && ch <= 'h';
	}

}
