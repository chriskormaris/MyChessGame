package utilities;

import chess.ChessBoard;

public class FenUtilities {
	
	
	public static ChessBoard getChessBoardFromFenPosition(String fenPosition) throws InvalidFenFormatException {
		// GameParameters.numOfRows = Constants.DEFAULT_NUM_OF_ROWS;
		ChessBoard chessBoard = new ChessBoard();
		
		fenPosition = fenPosition.trim();
		String[] fenPositionTokens = fenPosition.split(" ");
		if (fenPositionTokens.length != 6) {
			throw new InvalidFenFormatException(
					"FEN position must contain 5 white spaces inbetween. White spaces inbetween: " 
							+ (fenPositionTokens.length-1));
		}
		
		String startingPieces = fenPositionTokens[0];
		int[][] gameBoard = createGameBoard(chessBoard, startingPieces);
		
		String player = fenPositionTokens[1];
		boolean whitePlays = false;
		if (player.equals("w")) {
			whitePlays = true;
		} else if (player.equals("b")) {
			whitePlays = false;
		} else {
			throw new InvalidFenFormatException(
					"Invalid player character. It should be \"w\" or \"b\", NOT: \"" + player + "\"!");
		}
		
		String castlingAvailability = fenPositionTokens[2];
		boolean whiteKingMoved = false;
		boolean leftWhiteRookMoved = false;
		boolean rightWhiteRookMoved = false;
		boolean blackKingMoved = false;
		boolean leftBlackRookMoved = false;
		boolean rightBlackRookMoved = false;
		
		if (!castlingAvailability.contains("Q") && castlingAvailability.contains("K")) {
			leftWhiteRookMoved = true;
			whiteKingMoved = true;
		} else if (!castlingAvailability.contains("K") && castlingAvailability.contains("Q")) {
			rightWhiteRookMoved = true;
			whiteKingMoved = true;
		} else if (!castlingAvailability.contains("K") && !castlingAvailability.contains("Q")) {
			whiteKingMoved = true;
			leftWhiteRookMoved = true;
			rightWhiteRookMoved = true;
		}
		if (!castlingAvailability.contains("q") && castlingAvailability.contains("k")) {
			leftBlackRookMoved = true;
			blackKingMoved = true;
		} else if (!castlingAvailability.contains("k") && castlingAvailability.contains("q")) {
			rightBlackRookMoved = true;
			blackKingMoved = true;
		} else if (!castlingAvailability.contains("k") && !castlingAvailability.contains("q")) {
			blackKingMoved = true;
			leftBlackRookMoved = true;
			rightBlackRookMoved = true;
		}
		
		String enPassantPosition = fenPositionTokens[3].toUpperCase();
		
		int halfmoveClock = Integer.parseInt(fenPositionTokens[4]);
		
		int fullmoveNumber = Integer.parseInt(fenPositionTokens[5]);
		int halfmoveNumber = fullmoveNumber * 2;
		if (!whitePlays)
			halfmoveNumber -= 1;
		
		// set the ChessBoard parameters, according to the given FEN position
		chessBoard.setGameBoard(gameBoard);
		
		chessBoard.setWhitePlays(whitePlays);
		
		chessBoard.setWhiteKingMoved(whiteKingMoved);
		chessBoard.setLeftWhiteRookMoved(leftWhiteRookMoved);
		chessBoard.setRightWhiteRookMoved(rightWhiteRookMoved);
		chessBoard.setBlackKingMoved(blackKingMoved);
		chessBoard.setLeftBlackRookMoved(leftBlackRookMoved);
		chessBoard.setRightBlackRookMoved(rightBlackRookMoved);
		
		chessBoard.setEnPassantPosition(enPassantPosition);
		
		chessBoard.setHalfmoveClock(halfmoveClock);
		
		chessBoard.setHalfmoveNumber(halfmoveNumber);
		
		return chessBoard;
	}
	
	
	public static int[][] createGameBoard(ChessBoard chessBoard, String startingPieces) {
		int[][] gameBoard = new int[chessBoard.getNumOfRows()][Constants.NUM_OF_COLUMNS];
		
		int counter = 0;
		int i = 0, j = 0;
		while (counter < startingPieces.length()) {
			char piece = startingPieces.charAt(counter);
			// System.out.println("counter: " + counter + ", piece: " + piece);
			
			j = j % Constants.NUM_OF_COLUMNS;

			if (Character.isDigit(piece)) {
				j = j + Character.getNumericValue(piece);
				counter++;
				continue;
			} else if (piece == '/') {
				i++;
				j = 0;
				counter++;
				continue;
			}
			
			// System.out.println("i: " + i + ", j: " + j);
			gameBoard[chessBoard.getNumOfRows()-1-i][j] = getGameBoardValueByPiece(piece);
			
			if (piece == 'K') {	
				chessBoard.setWhiteKingPosition(Utilities.getPositionByRowCol(chessBoard.getNumOfRows()-1-i, j));
			} else if (piece == 'k') {	
				chessBoard.setBlackKingPosition(Utilities.getPositionByRowCol(chessBoard.getNumOfRows()-1-i, j));
			}

			j++;
			counter++;
		}
		
		
		return gameBoard;
	}
	
	
	public static int getGameBoardValueByPiece(char piece) {
		try {
			if (piece == 'r') {
				return Constants.BLACK_ROOK;
			} else if (piece == 'n') {
				return Constants.BLACK_KNIGHT;
			} else if (piece == 'b') {
				return Constants.BLACK_BISHOP;
			} else if (piece == 'q') {
				return Constants.BLACK_QUEEN;
			} else if (piece == 'k') {
				return Constants.BLACK_KING;
			} else if (piece == 'p') {
				return Constants.BLACK_PAWN;
			} 
			
			else if (piece == 'R') {
				return Constants.WHITE_ROOK;
			} else if (piece == 'N') {
				return Constants.WHITE_KNIGHT;
			} else if (piece == 'B') {
				return Constants.WHITE_BISHOP;
			} else if (piece == 'Q') {
				return Constants.WHITE_QUEEN;
			} else if (piece == 'K') {
				return Constants.WHITE_KING;
			} else if (piece == 'P') {
				return Constants.WHITE_PAWN;
			} else {
				throw new InvalidFenFormatException("Invalid piece character \"" + piece + "\"!");
			}
		} catch (InvalidFenFormatException ex) {
            System.err.println(ex.getMessage()); 
            return Constants.EMPTY;
		}
	}

	
}
