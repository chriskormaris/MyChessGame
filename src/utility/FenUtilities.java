package utility;

import chess.ChessBoard;
import enumeration.Allegiance;
import piece.*;

public class FenUtilities {
	
	
	public static ChessBoard getChessBoardFromFenPosition(String fenPosition) throws InvalidFenFormatException {
		ChessBoard chessBoard = new ChessBoard();
		
		fenPosition = fenPosition.trim();
		String[] fenPositionTokens = fenPosition.split(" ");
		if (fenPositionTokens.length != 6) {
			throw new InvalidFenFormatException(
					"FEN position must contain 5 white spaces in between. White spaces in between: "
							+ (fenPositionTokens.length-1));
		}
		
		String startingPieces = fenPositionTokens[0];
		ChessPiece[][] gameBoard = createGameBoard(chessBoard, startingPieces);
		
		String nextPlayerChar = fenPositionTokens[1];
		boolean playerFlag;
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
		
		chessBoard.setHalfMoveClock(halfmoveClock);
		
		chessBoard.setHalfMoveNumber(halfmoveNumber);
		
		return chessBoard;
	}
	
	
	public static ChessPiece[][] createGameBoard(ChessBoard chessBoard, String startingPieces) {
		ChessPiece[][] gameBoard = new ChessPiece[chessBoard.getNumOfRows()][Constants.DEFAULT_NUM_OF_COLUMNS];
        for (int i=0; i<chessBoard.getNumOfRows(); i++) {
            for (int j=0; j<Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
            	gameBoard[i][j] = new EmptyTile();
            }
		}
		
		int counter = 0;
		int i = 0, j = 0;
		while (counter < startingPieces.length()) {
			char pieceChar = startingPieces.charAt(counter);
			// System.out.println("counter: " + counter + ", pieceChar: " + pieceChar);
			
			j = j % Constants.DEFAULT_NUM_OF_COLUMNS;

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
			
			// System.out.println("i: " + i + ", j: " + j);
			// System.out.println("chessPiece: " + getChessPieceByChar(pieceChar));
			gameBoard[chessBoard.getNumOfRows()-1-i][j] = getChessPieceByChar(pieceChar);
			
			if (pieceChar == Constants.WHITE_KING) {	
				chessBoard.setWhiteKingPosition(Utilities.getPositionByRowCol(chessBoard.getNumOfRows()-1-i, j));
				// System.out.println("White king position: " + chessBoard.getWhiteKingPosition());
			} else if (pieceChar == Constants.BLACK_KING) {	
				chessBoard.setBlackKingPosition(Utilities.getPositionByRowCol(chessBoard.getNumOfRows()-1-i, j));
				// System.out.println("Black king position: " + chessBoard.getBlackKingPosition());
			}

			j++;
			counter++;
		}
		
		
		return gameBoard;
	}
	
	
	public static ChessPiece getChessPieceByChar(char chessPieceChar) {
		try {
			if (chessPieceChar == Constants.WHITE_PAWN) {
				return new Pawn(Allegiance.WHITE);
			} else if (chessPieceChar == Constants.WHITE_ROOK) {
				return new Rook(Allegiance.WHITE);
			} else if (chessPieceChar == Constants.WHITE_KNIGHT) {
				return new Knight(Allegiance.WHITE);
			} else if (chessPieceChar == Constants.WHITE_BISHOP) {
				return new Bishop(Allegiance.WHITE);
			} else if (chessPieceChar == Constants.WHITE_QUEEN) {
				return new Queen(Allegiance.WHITE);
			} else if (chessPieceChar == Constants.WHITE_KING) {
				return new King(Allegiance.WHITE);
			}
			
			if (chessPieceChar == Constants.BLACK_PAWN) {
				return new Pawn(Allegiance.BLACK);
			} else if (chessPieceChar == Constants.BLACK_ROOK) {
				return new Rook(Allegiance.BLACK);
			} else if (chessPieceChar == Constants.BLACK_KNIGHT) {
				return new Knight(Allegiance.BLACK);
			} else if (chessPieceChar == Constants.BLACK_BISHOP) {
				return new Bishop(Allegiance.BLACK);
			} else if (chessPieceChar == Constants.BLACK_QUEEN) {
				return new Queen(Allegiance.BLACK);
			} else if (chessPieceChar == Constants.BLACK_KING) {
				return new King(Allegiance.BLACK);
			}
			
			else {
				throw new InvalidFenFormatException("Invalid chessPiece character \"" + chessPieceChar + "\"!");
			}
		} catch (InvalidFenFormatException ex) {
            System.err.println(ex.getMessage()); 
            return new EmptyTile();
		}
	}
	
	
	public static char getPieceChar(ChessPiece chessPiece) {
		try {
			
			if (chessPiece.getAllegiance() == Allegiance.WHITE) {
				if (chessPiece instanceof Pawn) {
					return Constants.WHITE_PAWN;
				} else if (chessPiece instanceof Rook) {
					return Constants.WHITE_ROOK;
				} else if (chessPiece instanceof Knight) {
					return Constants.WHITE_KNIGHT;
				} else if (chessPiece instanceof Bishop) {
					return Constants.WHITE_BISHOP;
				} else if (chessPiece instanceof Queen) {
					return Constants.WHITE_QUEEN;
				} else if (chessPiece instanceof King) {
					return Constants.WHITE_KING;
				}
			}

			else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				if (chessPiece instanceof Pawn) {
					return Constants.BLACK_PAWN;
				} else if (chessPiece instanceof Rook) {
					return Constants.BLACK_ROOK;
				} else if (chessPiece instanceof Knight) {
					return Constants.BLACK_KNIGHT;
				} else if (chessPiece instanceof Bishop) {
					return Constants.BLACK_BISHOP;
				} else if (chessPiece instanceof Queen) {
					return Constants.BLACK_QUEEN;
				} else if (chessPiece instanceof King) {
					return Constants.BLACK_KING;
				}
			}
			
			else if (chessPiece instanceof EmptyTile) {
				return '-';
			}
			
			else {
				throw new InvalidFenFormatException("Invalid chessPiece value \"" + chessPiece + "\"!");
			}
			
		} catch (InvalidFenFormatException ex) {
            System.err.println(ex.getMessage()); 
		}
		return '-';
	}

	
	public static String getFenPositionFromChessBoard(ChessBoard chessBoard) {
		StringBuilder fenPosition = new StringBuilder();
		
		/* Step 1: Append the chess gameBoard pieces positions */
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			int emptyTilesCounter = 0;
			for (int j=0; j<Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
				// Get the chessPiece in the indices [numOfRows-i-1][j], from the gameBoard. 
				ChessPiece chessPiece = chessBoard.getGameBoard()[chessBoard.getNumOfRows() - i - 1][j];
				// Convert chessPiece value to chessPiece character.
				char pieceChar = getPieceChar(chessPiece);
				if (pieceChar != '-') {
					if (emptyTilesCounter != 0) {
						// Append the number of empty consecutive empty tiles in a row 
						// and then, the chessPiece character.
						fenPosition.append(emptyTilesCounter).append(pieceChar);
					} else {
						// Append the chessPiece character.
						fenPosition.append(pieceChar);
					}
					emptyTilesCounter = 0;
				} else {
					emptyTilesCounter++;
				}
			}
			if (emptyTilesCounter != 0) {
				// Append the chessPiece character.
				fenPosition.append(emptyTilesCounter);
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
		fenPosition.append(" ").append(chessBoard.getEnPassantPosition());
		
		/* Step 5: Append the half move clock. */
		fenPosition.append(" ").append(chessBoard.getHalfMoveClock());
		
		/* Step 6: Append the half move number. */
		fenPosition.append(" ").append(chessBoard.getHalfMoveNumber());
				
		return fenPosition.toString();
	}

	
}
