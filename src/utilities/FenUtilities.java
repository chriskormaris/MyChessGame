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
		
		String nextPlayerChar = fenPositionTokens[1];
		boolean playerFlag = false;
		if (nextPlayerChar.equals("w")) {
			playerFlag = true;
		} else if (nextPlayerChar.equals("b")) {
			playerFlag = false;
		} else {
			throw new InvalidFenFormatException(
					"Invalid player character. It should be \"w\" or \"b\", NOT: \"" + nextPlayerChar + "\"!");
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
		if (!playerFlag)
			halfmoveNumber -= 1;
		
		// set the ChessBoard parameters, according to the given FEN position
		chessBoard.setGameBoard(gameBoard);
		
		chessBoard.setPlayer(playerFlag);
		
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
			gameBoard[chessBoard.getNumOfRows()-1-i][j] = getChessPieceValueByChar(piece);
			
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
	
	
	public static int getChessPieceValueByChar(char pieceChar) {
		try {
			if (pieceChar == 'r') {
				return Constants.BLACK_ROOK;
			} else if (pieceChar == 'n') {
				return Constants.BLACK_KNIGHT;
			} else if (pieceChar == 'b') {
				return Constants.BLACK_BISHOP;
			} else if (pieceChar == 'q') {
				return Constants.BLACK_QUEEN;
			} else if (pieceChar == 'k') {
				return Constants.BLACK_KING;
			} else if (pieceChar == 'p') {
				return Constants.BLACK_PAWN;
			} 
			
			else if (pieceChar == 'R') {
				return Constants.WHITE_ROOK;
			} else if (pieceChar == 'N') {
				return Constants.WHITE_KNIGHT;
			} else if (pieceChar == 'B') {
				return Constants.WHITE_BISHOP;
			} else if (pieceChar == 'Q') {
				return Constants.WHITE_QUEEN;
			} else if (pieceChar == 'K') {
				return Constants.WHITE_KING;
			} else if (pieceChar == 'P') {
				return Constants.WHITE_PAWN;
			} else {
				throw new InvalidFenFormatException("Invalid chess piece character \"" + pieceChar + "\"!");
			}
		} catch (InvalidFenFormatException ex) {
            System.err.println(ex.getMessage()); 
            return Constants.EMPTY;
		}
	}
	
	
	public static char getPieceCharByChessPieceValue(int chessPiece) {
		try {
			if (chessPiece == Constants.BLACK_ROOK) {
				return 'r';
			} else if (chessPiece == Constants.BLACK_KNIGHT) {
				return 'n';
			} else if (chessPiece == Constants.BLACK_BISHOP) {
				return 'b';
			} else if (chessPiece == Constants.BLACK_QUEEN) {
				return 'q';
			} else if (chessPiece == Constants.BLACK_KING) {
				return 'k';
			} else if (chessPiece == Constants.BLACK_PAWN) {
				return 'p';
			} 
			
			else if (chessPiece == Constants.WHITE_ROOK) {
				return 'R';
			} else if (chessPiece == Constants.WHITE_KNIGHT) {
				return 'N';
			} else if (chessPiece == Constants.WHITE_BISHOP) {
				return 'B';
			} else if (chessPiece == Constants.WHITE_QUEEN) {
				return 'Q';
			} else if (chessPiece == Constants.WHITE_KING) {
				return 'K';
			} else if (chessPiece == Constants.WHITE_PAWN) {
				return 'P';
			} 

			else if (chessPiece == Constants.EMPTY) {
				return '0';
			}
			
			else {
				throw new InvalidFenFormatException("Invalid chess piece value \"" + chessPiece + "\"!");
			}
			
		} catch (InvalidFenFormatException ex) {
            System.err.println(ex.getMessage()); 
            return '0';
		}
	}

	
	// TODO
	public static String getFenPositionFromChessBoard(ChessBoard chessBoard) {
		String fenPosition = "";
		
		/* Step 1: Append the chess gameBoard pieces positions */
		for (int i=0; i<chessBoard.getGameBoard().length; i++) {
			int emptyTilesCounter = 0;
			for (int j=0; j<chessBoard.getGameBoard()[0].length; j++) {
				// Get the piece in the indices [numOfRows-i-1][j], from the gameBoard. 
				int chessPiece = chessBoard.getGameBoard()[chessBoard.getGameBoard().length - i - 1][j];
				// Convert chessPiece value to chess piece character.
				char pieceChar = getPieceCharByChessPieceValue(chessPiece);
				if (pieceChar != '0') {
					if (emptyTilesCounter != 0) {
						// Append the number of empty consecutive empty tiles in a row 
						// and then, the chess piece character.
						fenPosition += String.valueOf(emptyTilesCounter) + pieceChar;	
					} else {
						// Append the piece character.
						fenPosition += pieceChar;
					}
					emptyTilesCounter = 0;
				} else {
					emptyTilesCounter++;
				}
			}
			if (emptyTilesCounter != 0) {
				// Append the chess piece character.
				fenPosition += emptyTilesCounter;
			}
			emptyTilesCounter = 0;
			if (i < chessBoard.getGameBoard().length - 1) {
				// Append the row terminator character.
				fenPosition += '/';
			}
		}
		
		/* Step 2: Append the player who plays next. */
		fenPosition += " " + (chessBoard.whitePlays() ? 'w' : 'b');
		
		/* Step 3: Append the available castling moves. */
		if (chessBoard.isWhiteKingsideCastlingAvailable() || chessBoard.isWhiteQueensideCastlingAvailable()
			|| chessBoard.isBlackKingsideCastlingAvailable() || chessBoard.isBlackQueensideCastlingAvailable()) {
			fenPosition += " " + (chessBoard.isWhiteKingsideCastlingAvailable() ? 'K' : "");
			fenPosition += chessBoard.isWhiteQueensideCastlingAvailable() ? 'Q' : "";
			fenPosition += chessBoard.isBlackKingsideCastlingAvailable() ? 'k' : "";
			fenPosition += chessBoard.isBlackQueensideCastlingAvailable() ? 'q' : "";
		} else {
			fenPosition += " -";
		}
		
		/* Step 4: Append the current "en passant" position. */
		fenPosition += " " + chessBoard.getEnPassantPosition();
		
		/* Step 5: Append the half move clock. */
		fenPosition += " " + chessBoard.getHalfmoveClock();
		
		/* Step 6: Append the half move number. */
		fenPosition += " " + chessBoard.getHalfmoveNumber();
				
		return fenPosition;
	}

	
}
