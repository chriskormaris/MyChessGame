package com.chriskormaris.mychessgame.api.chess_board;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.EmptyTile;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.utility.ChessPieceShortestPath;
import com.chriskormaris.mychessgame.api.utility.Constants;
import com.chriskormaris.mychessgame.api.utility.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ChessBoard {

	public final static int NUM_OF_COLUMNS = Constants.DEFAULT_NUM_OF_COLUMNS;
	private final int numOfRows;

	/* Immediate move that led to this board. */
	private Move lastMove;


	/* The ChessBoard's gameBoard:
	 *      A     B     C     D     E     F     G     H
	 *   _________________________________________________
	 * 8 |(7,0)|(7,1)|(7,2)|(7,3)|(7,4)|(7,5)|(7,6)|(7,7)| 8
	 * 7 |(6,0)|(6,1)|(6,2)|(6,3)|(6,4)|(6,5)|(6,6)|(6,7)| 7
	 * 6 |(5,0)|(5,1)|(5,2)|(5,3)|(5,4)|(5,5)|(5,6)|(5,7)| 6
	 * 5 |(4,0)|(4,1)|(4,2)|(4,3)|(4,4)|(4,5)|(4,6)|(4,7)| 5
	 * 4 |(3,0)|(3,1)|(3,2)|(3,3)|(3,4)|(3,5)|(3,6)|(3,7)| 4
	 * 3 |(2,0)|(2,1)|(2,2)|(2,3)|(2,4)|(2,5)|(2,6)|(2,7)| 3
	 * 2 |(1,0)|(1,1)|(1,2)|(1,3)|(1,4)|(1,5)|(1,6)|(1,7)| 2
	 * 1 |(0,0)|(0,1)|(0,2)|(0,3)|(0,4)|(0,5)|(0,6)|(0,7)| 1
	 *   -------------------------------------------------
	 *      A     B     C     D     E     F     G     H
	 * E.g: A1 = (0,0), H8 = (7,7), B3 = (2,1), C2 = (1,2) etc. */
	private ChessPiece[][] gameBoard;

	/* A board with:
	 * 1 for areas threatened by white pieces.
	 * 0 for areas not threatened by white pieces. */
	private int[][] tilesThreatenedByWhite;

	/* A board with:
	 * 1 for areas threatened by black pieces.
	 * 0 for areas not threatened by black pieces. */
	private int[][] tilesThreatenedByBlack;

	/* These are used to define if a checkmate has occurred. */
	private String whiteKingPosition;
	private String blackKingPosition;

	/* The player who plays next. True is for White. False is for Black.
	 * The player who control the white pieces plays first. */
	private boolean player;

	/* These variables are used to handle the "castling" implementation. */
	private boolean whiteKingMoved;
	private boolean leftWhiteRookMoved;
	private boolean rightWhiteRookMoved;

	private boolean blackKingMoved;
	private boolean leftBlackRookMoved;
	private boolean rightBlackRookMoved;

	private boolean whiteCastlingDone;
	private boolean blackCastlingDone;

	/* The position where the last pawn that leaped two steps forward
	 * would normally be, if it had only moved one step forward.
	 * If the last move was not a move made by a pawn,
	 * or the move was not a leap of two tiles forward,
	 * this variable is equal to "-". */
	private String enPassantPosition;

	/* This variable is used to determine a draw,
	 * if no chessPiece has been captured in 50 full moves (100 half moves). */
	private int halfMoveClock;

	/* 1 full move corresponds to 2 half moves. */
	private int halfMoveNumber;

	private boolean whiteKingInCheck;
	private boolean blackKingInCheck;
	private Map<String, Set<String>> whiteKingInCheckValidPieceMoves;
	private Map<String, Set<String>> blackKingInCheckValidPieceMoves;

	/* These variables are also used for the "undo" functionality. */
	private boolean isWhiteCheckmate;
	private boolean isBlackCheckmate;
	private boolean isWhiteStalemateDraw;
	private boolean isBlackStalemateDraw;
	private boolean isInsufficientMaterialDraw;

	private int whiteCapturedPiecesCounter;
	private int blackCapturedPiecesCounter;

	private int score;
	private double lastCapturedPieceValue;
	private Set<ChessPiece> promotedPieces;

	private Set<String> positionsToRemove;
	private Map<String, ChessPiece> piecesToPlace;

	private ChessPiece capturedPiece;

	public ChessBoard() {
		this(Constants.DEFAULT_NUM_OF_ROWS);
	}

	public ChessBoard(int numOfRows) {
		this.numOfRows = numOfRows;

		this.lastMove = new Move();

		this.gameBoard = new ChessPiece[numOfRows][NUM_OF_COLUMNS];
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				this.gameBoard[i][j] = new EmptyTile();
			}
		}

		// this.gameBoard = FenUtilities.createGameBoard(this, Constants.DEFAULT_STARTING_PIECES);

		this.tilesThreatenedByWhite = new int[numOfRows][NUM_OF_COLUMNS];
		this.tilesThreatenedByBlack = new int[numOfRows][NUM_OF_COLUMNS];

		this.whiteKingPosition = "E1";
		this.blackKingPosition = "E" + numOfRows;

		this.player = Constants.WHITE;  // White plays first.

		this.enPassantPosition = "-";

		this.halfMoveClock = 0;
		this.halfMoveNumber = 1;

		this.whiteCapturedPiecesCounter = 0;
		this.blackCapturedPiecesCounter = 0;

		this.score = 0;
		this.lastCapturedPieceValue = 0;

		this.promotedPieces = new HashSet<>();
		this.positionsToRemove = new HashSet<>();
		this.piecesToPlace = new HashMap<>();

		this.whiteKingInCheckValidPieceMoves = new HashMap<>();
		this.blackKingInCheckValidPieceMoves = new HashMap<>();

		setThreats();
	}


	// Copy constructor
	public ChessBoard(ChessBoard otherBoard) {
		this.numOfRows = otherBoard.numOfRows;

		this.lastMove = new Move(otherBoard.lastMove);

		this.gameBoard = Utilities.copyGameBoard(otherBoard.getGameBoard());
		this.tilesThreatenedByWhite = Utilities.copyIntBoard(otherBoard.getTilesThreatenedByWhite());
		this.tilesThreatenedByBlack = Utilities.copyIntBoard(otherBoard.getTilesThreatenedByBlack());

		this.whiteKingPosition = otherBoard.getWhiteKingPosition();
		this.blackKingPosition = otherBoard.getBlackKingPosition();

		this.player = otherBoard.whitePlays();

		this.whiteKingMoved = otherBoard.isWhiteKingMoved();
		this.leftWhiteRookMoved = otherBoard.isLeftWhiteRookMoved();
		this.rightWhiteRookMoved = otherBoard.isRightWhiteRookMoved();

		this.blackKingMoved = otherBoard.isBlackKingMoved();
		this.leftBlackRookMoved = otherBoard.isLeftBlackRookMoved();
		this.rightBlackRookMoved = otherBoard.isRightBlackRookMoved();

		this.whiteCastlingDone = otherBoard.isWhiteCastlingDone();
		this.blackCastlingDone = otherBoard.isBlackCastlingDone();

		this.enPassantPosition = otherBoard.getEnPassantPosition();

		this.halfMoveClock = otherBoard.getHalfMoveClock();
		this.halfMoveNumber = otherBoard.getHalfMoveNumber();

		this.whiteKingInCheck = otherBoard.isWhiteKingInCheck();
		this.blackKingInCheck = otherBoard.isBlackKingInCheck();

		this.whiteKingInCheckValidPieceMoves = new HashMap<>(otherBoard.getWhiteKingInCheckValidPieceMoves());
		this.blackKingInCheckValidPieceMoves = new HashMap<>(otherBoard.getBlackKingInCheckValidPieceMoves());

		this.isWhiteCheckmate = otherBoard.isWhiteCheckmate();
		this.isBlackCheckmate = otherBoard.isBlackCheckmate();
		this.isWhiteStalemateDraw = otherBoard.isWhiteStalemateDraw();
		this.isBlackStalemateDraw = otherBoard.isBlackStalemateDraw();
		this.isInsufficientMaterialDraw = otherBoard.isInsufficientMaterialDraw();

		this.whiteCapturedPiecesCounter = otherBoard.getWhiteCapturedPiecesCounter();
		this.blackCapturedPiecesCounter = otherBoard.getBlackCapturedPiecesCounter();

		this.score = otherBoard.getScore();
		this.lastCapturedPieceValue = otherBoard.getLastCapturedPieceValue();

		this.promotedPieces = new HashSet<>(otherBoard.getPromotedPieces());
		this.positionsToRemove = new HashSet<>(otherBoard.getPositionsToRemove());
		this.piecesToPlace = new HashMap<>(otherBoard.getPiecesToPlace());
		this.capturedPiece = otherBoard.getCapturedPiece();
	}

	// It prints the chess board board on the console.
	public static void printChessBoard(ChessPiece[][] chessBoard) {
		System.out.println(getChessBoardString(chessBoard));
	}

	public static String getChessBoardString(ChessPiece[][] chessBoard) {
		StringBuilder output = new StringBuilder();
		output.append("    A   B   C   D   E   F   G   H\n");
		output.append("  ---------------------------------\n");
		int n1 = chessBoard.length;
		int n2 = chessBoard[0].length;
		for (int i = 0; i < n1; i++) {
			output.append(n1 - i).append(" |");
			for (int j = 0; j < n2; j++) {
				output.append(" ").append(chessBoard[n1 - 1 - i][j].getChessPieceChar()).append(" |");
			}
			output.append(" ").append(n1 - i).append("\n");
		}
		output.append("  ---------------------------------\n");
		output.append("    A   B   C   D   E   F   G   H\n");

		return output.toString();
	}

	// Make a move; it places a letter in the board
	public void makeMove(Move move, boolean displayMove) {
		List<String> positionsList = move.getPositions();

		// previous position
		String previousPosition = positionsList.get(0);

		// next position
		String nextPosition = positionsList.get(1);

		movePieceFromAPositionToAnother(previousPosition, nextPosition, displayMove);

		this.lastMove = new Move(move);
	}

	// The parameter "displayMove" should be set to true when we make the move on the actual board.
	// If the method is called while running the minimax AI algorithm,
	// then the parameter "displayMove" should be set to false.
	public void movePieceFromAPositionToAnother(String positionStart, String positionEnd, boolean displayMove) {
		// System.out.println("startPosition: " + startPosition);
		// System.out.println("endPosition: " + endPosition);

		int rowStart = Utilities.getRowFromPosition(positionStart);
		int columnStart = Utilities.getColumnFromPosition(positionStart);
		ChessPiece chessPiece = this.gameBoard[rowStart][columnStart];
		// if (displayMove) {
		// 	System.out.println("rowStart: " + rowStart + ", columnStart: " + columnStart);
		// 	System.out.println("chessPiece:" + chessPiece);
		// }

		int rowEnd = Utilities.getRowFromPosition(positionEnd);
		int columnEnd = Utilities.getColumnFromPosition(positionEnd);
		ChessPiece endTile = this.gameBoard[rowEnd][columnEnd];
		// if (displayMove) {
		// 	System.out.println("rowEnd: " + rowEnd + ", columnEnd: " + columnEnd);
		// 	System.out.println("endTile:" + endTile);
		// }

		if (positionsToRemove.size() > 0) {
			positionsToRemove.clear();
		}
		if (piecesToPlace.size() > 0) {
			piecesToPlace.clear();
		}
		if (capturedPiece != null) {
			capturedPiece = null;
		}

		// Allow only valid moves, for all the chess board pieces.
		// Move only if the tile is empty or the tile contains an opponent chessPiece.
		// Also allow castling, en passant and promotion moves.
		// System.out.println("hintPositions: " + hintPositions);
		if (endTile instanceof EmptyTile ||
				chessPiece.getAllegiance() != endTile.getAllegiance()) {

			this.lastCapturedPieceValue = Utilities.getChessPieceValue(positionEnd, endTile, this.halfMoveNumber);

			if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				this.lastCapturedPieceValue = -this.lastCapturedPieceValue;
			}

			Set<String> castlingPositions = null;
			if (chessPiece instanceof King) {
				castlingPositions = King.getCastlingPositions(positionStart, this);
			}

			this.gameBoard[rowStart][columnStart] = new EmptyTile();
			this.gameBoard[rowEnd][columnEnd] = chessPiece;
			if (displayMove) {
				positionsToRemove.add(positionStart);
				piecesToPlace.put(positionEnd, chessPiece);
			}

			// Implementation of castling here.
			if (chessPiece instanceof King) {

				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					setWhiteKingPosition(positionEnd);
					// System.out.println("white king new position: " + whiteKingPosition);
					setWhiteKingMoved(true);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					setBlackKingPosition(positionEnd);
					// System.out.println("black king new position: " + blackKingPosition);
					setBlackKingMoved(true);
				}

				/* Castling implementation */

				// System.out.println("castlingPositions: " + castlingPositions);
				if (castlingPositions.contains(positionEnd)) {
					// White queen side castling
					if (positionEnd.equals("C1")) {
						// Move the left white rook to the correct position.
						this.gameBoard[0][0] = new EmptyTile();
						this.gameBoard[0][3] = new Rook(Allegiance.WHITE);
						if (displayMove) {
							positionsToRemove.add("A1");
							piecesToPlace.put("D1", new Rook(Allegiance.WHITE));
						}

						setWhiteKingMoved(true);
						setLeftWhiteRookMoved(true);
						setWhiteCastlingDone(true);
					}
					// White king side castling
					else if (positionEnd.equals("G1")) {
						// Move the right white rook to the correct position.
						this.gameBoard[0][7] = new EmptyTile();
						this.gameBoard[0][5] = new Rook(Allegiance.WHITE);
						if (displayMove) {
							positionsToRemove.add("H1");
							piecesToPlace.put("F1", new Rook(Allegiance.WHITE));
						}

						setWhiteKingMoved(true);
						setRightWhiteRookMoved(true);
						setWhiteCastlingDone(true);
					}
					// Black queen side castling
					else if (positionEnd.equals("C" + this.numOfRows)) {
						// Move the left black rook to the correct position.
						this.gameBoard[this.numOfRows - 1][0] = new EmptyTile();
						this.gameBoard[this.numOfRows - 1][3] = new Rook(Allegiance.BLACK);
						if (displayMove) {
							positionsToRemove.add("A" + this.numOfRows);
							piecesToPlace.put("D" + this.numOfRows, new Rook(Allegiance.BLACK));
						}

						setBlackKingMoved(true);
						setLeftBlackRookMoved(true);
						setBlackCastlingDone(true);
					}
					// Black king side castling
					else if (positionEnd.equals("G" + this.numOfRows)) {
						// Move the right black rook to the correct position.
						this.gameBoard[this.numOfRows - 1][7] = new EmptyTile();
						this.gameBoard[this.numOfRows - 1][5] = new Rook(Allegiance.BLACK);
						if (displayMove) {
							positionsToRemove.add("H" + this.numOfRows);
							piecesToPlace.put("F" + this.numOfRows, new Rook(Allegiance.BLACK));
						}

						setBlackKingMoved(true);
						setRightBlackRookMoved(true);
						setBlackCastlingDone(true);
					}
				}

			} else if (chessPiece instanceof Rook) {
				if (!this.isLeftWhiteRookMoved() && (positionStart.equals("A1") ||
						!(Utilities.getChessPieceFromPosition(this.gameBoard, "A1") instanceof Rook))) {
					this.setLeftWhiteRookMoved(true);
					this.setWhiteCastlingDone(false);
				} else if (!this.isRightWhiteRookMoved() && (positionStart.equals("H1") ||
						!(Utilities.getChessPieceFromPosition(this.gameBoard, "H1") instanceof Rook))) {
					this.setRightWhiteRookMoved(true);
					this.setWhiteCastlingDone(false);
				} else if (!this.isLeftBlackRookMoved() && (positionStart.equals("A" + numOfRows) ||
						!(Utilities.getChessPieceFromPosition(this.gameBoard, "A" + numOfRows) instanceof Rook))) {
					this.setLeftBlackRookMoved(true);
					this.setBlackCastlingDone(false);
				} else if (!this.isRightBlackRookMoved() && (positionStart.equals("H" + numOfRows) ||
						!(Utilities.getChessPieceFromPosition(this.gameBoard, "H" + numOfRows) instanceof Rook))) {
					this.setRightBlackRookMoved(true);
					this.setBlackCastlingDone(false);
				}
			}

			// Implementation of "en passant" functionality and "pawn promotion" here.
			if (chessPiece instanceof Pawn) {

				/* En passant implementation */

				// Remove the captured en passant pieces.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd - 1 >= 0) {

					String twoStepsForwardBlackPawnPosition = Utilities.getPositionByRowCol(rowEnd - 1, columnEnd);
					// if (displayMove)
					// System.out.println("twoStepsForwardBlackPawnPosition: " + twoStepsForwardBlackPawnPosition);
					int twoStepsForwardBlackPawnPositionRow = Utilities.getRowFromPosition(twoStepsForwardBlackPawnPosition);
					int twoStepsForwardBlackPawnPositionColumn = Utilities.getColumnFromPosition(twoStepsForwardBlackPawnPosition);

					// White pawn captures black pawn.
					ChessPiece possibleBlackEnPassantCapturedPawn = this.gameBoard[rowEnd - 1][columnEnd];
					if (possibleBlackEnPassantCapturedPawn instanceof Pawn
							&& possibleBlackEnPassantCapturedPawn.getAllegiance() == Allegiance.BLACK
							&& this.enPassantPosition.equals(Utilities.getPositionByRowCol(rowEnd, columnEnd))) {

						if (displayMove) {
							positionsToRemove.add(twoStepsForwardBlackPawnPosition);

							capturedPiece = possibleBlackEnPassantCapturedPawn;
						} else {
							this.gameBoard[twoStepsForwardBlackPawnPositionRow][twoStepsForwardBlackPawnPositionColumn] = new EmptyTile();
						}
						this.enPassantPosition = "-";
					}

					// Black pawn captures white pawn.
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd + 1 < numOfRows) {

					String twoStepsForwardWhitePawnPosition = Utilities.getPositionByRowCol(rowEnd + 1, columnEnd);
					int twoStepsForwardWhitePawnPositionRow = Utilities.getRowFromPosition(twoStepsForwardWhitePawnPosition);
					int twoStepsForwardWhitePawnPositionColumn = Utilities.getColumnFromPosition(twoStepsForwardWhitePawnPosition);

					ChessPiece possibleWhiteEnPassantCapturedPawn = this.gameBoard[rowEnd + 1][columnEnd];
					if (possibleWhiteEnPassantCapturedPawn instanceof Pawn
							&& possibleWhiteEnPassantCapturedPawn.getAllegiance() == Allegiance.WHITE
							&& this.enPassantPosition.equals(Utilities.getPositionByRowCol(rowEnd, columnEnd))) {

						if (displayMove) {
							positionsToRemove.add(twoStepsForwardWhitePawnPosition);

							capturedPiece = possibleWhiteEnPassantCapturedPawn;
						} else {
							this.gameBoard[twoStepsForwardWhitePawnPositionRow][twoStepsForwardWhitePawnPositionColumn] = new EmptyTile();
						}
						this.enPassantPosition = "-";
					}

				}

				// Save the two-step forward moves as one step forward move.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd == rowStart + 2) {
					this.enPassantPosition = Utilities.getPositionByRowCol(rowStart + 1, columnStart);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == rowStart - 2) {
					this.enPassantPosition = Utilities.getPositionByRowCol(rowStart - 1, columnStart);
				} else {
					this.enPassantPosition = "-";
				}

				/* Pawn promotion implementation */
				// If a pawn is going to be promoted and this is not a display move,
				// automatically choose the best promotion piece, based on the best outcome.
				if ((chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd == this.numOfRows - 1
						|| chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == 0) && !displayMove) {
					automaticPawnPromotion(chessPiece, positionEnd, false);
				}

			} else {
				this.enPassantPosition = "-";
			}

			setThreats();

			// Increase the halfMoveClock if no capture has occurred.
			if (endTile.getAllegiance() == Allegiance.EMPTY) {
				halfMoveClock++;
			} else {  // a capture has occurred
				halfMoveClock = 0;
			}

			// If a chessPiece capture has occurred.
			if (chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof EmptyTile) && !displayMove) {
				updateScoreAfterPieceCapture(endTile);
			}
		}
	}

	public void updateScoreAfterPieceCapture(ChessPiece endTile) {
		if (promotedPieces.contains(endTile)) {
			if (endTile.getAllegiance() == Allegiance.WHITE) {
				score -= Constants.PAWN_VALUE;
			} else if (endTile.getAllegiance() == Allegiance.BLACK) {
				score += Constants.PAWN_VALUE;
			}
		} else if (endTile.getAllegiance() == Allegiance.WHITE) {
			if (endTile instanceof Pawn) {
				score -= Constants.PAWN_VALUE;
			} else if (endTile instanceof Rook) {
				score -= Constants.ROOK_VALUE;
			} else if (endTile instanceof Knight) {
				score -= Constants.KNIGHT_VALUE;
			} else if (endTile instanceof Bishop) {
				score -= Constants.BISHOP_VALUE;
			} else if (endTile instanceof Queen) {
				score -= Constants.QUEEN_VALUE;
			}
		} else if (endTile.getAllegiance() == Allegiance.BLACK) {
			if (endTile instanceof Pawn) {
				score += Constants.PAWN_VALUE;
			} else if (endTile instanceof Rook) {
				score += Constants.ROOK_VALUE;
			} else if (endTile instanceof Knight) {
				score += Constants.KNIGHT_VALUE;
			} else if (endTile instanceof Bishop) {
				score += Constants.BISHOP_VALUE;
			} else if (endTile instanceof Queen) {
				score += Constants.QUEEN_VALUE;
			}
		}

		if (endTile.getAllegiance() == Allegiance.WHITE) {
			this.whiteCapturedPiecesCounter++;
		} else if (endTile.getAllegiance() == Allegiance.BLACK) {
			this.blackCapturedPiecesCounter++;
		}
	}

	public void automaticPawnPromotion(ChessPiece chessPiece, String positionEnd, boolean displayMove) {
		ChessPiece queen = new Queen(chessPiece.getAllegiance());
		ChessPiece rook = new Rook(chessPiece.getAllegiance());
		ChessPiece bishop = new Bishop(chessPiece.getAllegiance());
		ChessPiece knight = new Knight(chessPiece.getAllegiance());

		ChessPiece[] promotionChessPieces = {queen, rook, bishop, knight};

		ChessBoard chessBoard = new ChessBoard(this);

		// System.out.println("Printing Knight promotion board (before)...");
		// System.out.println(chessBoard);

		int rowEnd = Utilities.getRowFromPosition(positionEnd);
		int columnEnd = Utilities.getColumnFromPosition(positionEnd);

		chessBoard.getGameBoard()[rowEnd][columnEnd] = knight;
		chessBoard.setThreats();

		// System.out.println("Printing Knight promotion board (after)...");
		// System.out.println(chessBoard);

		// System.out.println("Checking for Knight checkmate...");
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			chessBoard.checkForWhiteCheckmate(false);
			// System.out.println("chessBoard.isWhiteCheckmate: " + chessBoard.isWhiteCheckmate);
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			chessBoard.checkForBlackCheckmate(false);
			// System.out.println("chessBoard.isBlackCheckmate: " + chessBoard.isBlackCheckmate);
		}

		if (chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.isWhiteCheckmate()
				||
				chessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.isBlackCheckmate()) {
			this.gameBoard[rowEnd][columnEnd] = knight;
			if (displayMove) {
				piecesToPlace.put(positionEnd, knight);
			}
			promotedPieces.add(knight);
		} else {
			for (ChessPiece currentPromotionPiece : promotionChessPieces) {
				chessBoard.getGameBoard()[rowEnd][columnEnd] = currentPromotionPiece;
				chessBoard.setThreats();

				if (chessPiece.getAllegiance() == Allegiance.WHITE && !chessBoard.checkForBlackStalemateDraw()
						||
						chessPiece.getAllegiance() == Allegiance.BLACK && !chessBoard.checkForWhiteStalemateDraw()) {
					this.gameBoard[rowEnd][columnEnd] = currentPromotionPiece;
					if (displayMove) {
						piecesToPlace.put(positionEnd, currentPromotionPiece);
					}
					promotedPieces.add(currentPromotionPiece);
					break;
				}
			}
		}
	}

	// Checks whether a given tile (with given row and column) contains a chessPiece of the given player.
	// White pieces have positive values. Black pieces have negative values.
	private boolean rowColContainsPlayerPiece(int row, int column, boolean player) {
		return player == Constants.WHITE && gameBoard[row][column].getAllegiance() == Allegiance.WHITE
				|| player == Constants.BLACK && gameBoard[row][column].getAllegiance() == Allegiance.BLACK;
	}

	/* Generates the children of the state
	 * Any square in the board that is empty,
	 * or is an opponent chessPiece, results to a child.
	 * Some special cases include "en passant" and castling.
	 */
	public List<ChessBoard> getChildren(boolean player) {
		List<ChessBoard> children = new ArrayList<>();

		// int childPlayer = (getLastPlayer() == Constants.WHITE) ? Constants.BLACK : Constants.BLACK;

		// int i = 0;
		// System.out.println("**********************************************");
		for (int row = 0; row < numOfRows; row++) {
			for (int column = 0; column < NUM_OF_COLUMNS; column++) {
				if (rowColContainsPlayerPiece(row, column, player)) {
					String initialPosition = Utilities.getPositionByRowCol(row, column);
					Set<String> nextPositions = new HashSet<>();
					if (!this.whiteKingInCheck && whitePlays()
							|| !this.blackKingInCheck && blackPlays()) {
						nextPositions = getNextPositions(initialPosition);
					} else if (this.whiteKingInCheck && this.player) {
						nextPositions = this.whiteKingInCheckValidPieceMoves.get(initialPosition);
					} else if (this.blackKingInCheck && !this.player) {
						nextPositions = this.blackKingInCheckValidPieceMoves.get(initialPosition);
					}

					// System.out.println("nextPositions: " + nextPositions);

					if (nextPositions != null) {
						for (String nextPosition : nextPositions) {
							// System.out.println(initialPosition + ": " + nextPositions);
							ChessBoard child = new ChessBoard(this);

							List<String> moves = new ArrayList<>();
							moves.add(initialPosition);
							moves.add(nextPosition);

							// Move move = new Move(moves, evaluate());
							Move move = new Move(moves);

							// move.setValue(this.evaluate());

							child.makeMove(move, false);
							this.player = !this.player;

							// System.out.println("**********************************************");
							child.getLastMove().setPositions(moves);
							child.getLastMove().setValue(child.evaluate());
							// System.out.println("**********************************************\n");

							// System.out.println("**********************************************");
							// System.out.println("player " + player + ", child " + i + ", last move -> " + child.getLastMove());
							// System.out.println("**********************************************\n");

							children.add(child);

							// i++;
						}
					}
				}
			}
		}
		// System.out.println("**********************************************\n");

		return children;
	}

	@SuppressWarnings("unused")
	private int countLegalMoves(boolean player) {
		int counter = 0;

		for (int row = 0; row < numOfRows; row++) {
			for (int column = 0; column < NUM_OF_COLUMNS; column++) {
				if (rowColContainsPlayerPiece(row, column, player)) {
					String initialPosition = Utilities.getPositionByRowCol(row, column);
					Set<String> nextPositions;
					if (!this.whiteKingInCheck && this.player || !this.blackKingInCheck && !this.player) {
						nextPositions = getNextPositions(initialPosition);
					} else if (this.whiteKingInCheck && this.player) {
						nextPositions = this.whiteKingInCheckValidPieceMoves.get(initialPosition);
					} else {
						nextPositions = this.blackKingInCheckValidPieceMoves.get(initialPosition);
					}

					if (nextPositions != null) {
						counter += nextPositions.size();
					}

				}
			}
		}

		return counter;
	}

	private int countLegalMovesFromRowCol(int row, int column, boolean player) {
		int counter = 0;

		if (rowColContainsPlayerPiece(row, column, player)) {
			String initialPosition = Utilities.getPositionByRowCol(row, column);
			Set<String> nextPositions;
			if (!this.whiteKingInCheck && this.player || !this.blackKingInCheck && !this.player) {
				nextPositions = getNextPositions(initialPosition);
			} else if (this.whiteKingInCheck && this.player) {
				nextPositions = this.whiteKingInCheckValidPieceMoves.get(initialPosition);
			} else {
				nextPositions = this.blackKingInCheckValidPieceMoves.get(initialPosition);
			}

			if (nextPositions != null) {
				counter += nextPositions.size();
			}

		}

		return counter;
	}

	/*
	 * We use a heuristic function that takes into consideration
	 * how close we are to a checkmate and the importance of
	 * the opponent's pieces we capture.
	 */
	public double evaluate() {
		int whiteScore = 0;
		int blackScore = 0;

		this.isWhiteCheckmate = checkForWhiteCheckmate(false);
		this.isBlackCheckmate = checkForBlackCheckmate(false);
		this.isWhiteStalemateDraw = checkForWhiteStalemateDraw();
		this.isBlackStalemateDraw = checkForBlackStalemateDraw();
		this.isInsufficientMaterialDraw = checkForInsufficientMaterialDraw();

		if (this.isWhiteCheckmate) return Constants.CHECKMATE_VALUE;
		if (this.isBlackCheckmate) return -Constants.CHECKMATE_VALUE;
		if (this.isWhiteStalemateDraw) return 0;
		if (this.isBlackStalemateDraw) return 0;
		if (this.isInsufficientMaterialDraw) return 0;
		// if (checkForNoPieceCaptureDraw()) return 0;

		// String startPosition = lastMove.getPositions().get(0);
		String endPosition = lastMove.getPositions().get(1);
		ChessPiece lastMovedPiece = Utilities.getChessPieceFromPosition(this.gameBoard, endPosition);
		double lastMovedChessPieceValue = Utilities.getChessPieceValue(endPosition, lastMovedPiece, this.halfMoveNumber);
		// System.out.println("lastPiece: " + Utilities.getPieceNameByValue(lastPiece));

		int endRow = Utilities.getRowFromPosition(endPosition);
		int endColumn = Utilities.getColumnFromPosition(endPosition);

		/* DEBUGGING. */
		// System.out.println(lastMove);
		// System.out.println("lastCapturedPieceValue: " + this.lastCapturedPieceValue);

		// System.out.println("gameBoard");
		// printChessBoard(gameBoard);

		// If Castling has occurred, add to the score.
		if (whiteCastlingDone) {
			// System.out.println("White castling done!");
			whiteScore += Constants.CASTLING_VALUE;
		} else if (blackCastlingDone) {
			// System.out.println("Black castling done!");
			blackScore += Constants.CASTLING_VALUE;
		} else if (!whiteCastlingDone && !isWhiteQueenSideCastlingAvailable() && !isWhiteKingSideCastlingAvailable()) {
			// System.out.println("White castling lost!");
			whiteScore -= 10 * Constants.CASTLING_VALUE;
		} else if (!blackCastlingDone && !isBlackQueenSideCastlingAvailable() && !isBlackKingSideCastlingAvailable()) {
			// System.out.println("Black castling lost!");
			blackScore -= 10 * Constants.CASTLING_VALUE;
		}

		// Calculate the values of each chessPiece and store them in the "valueBoard" array.
		int n1 = numOfRows;
		int n2 = NUM_OF_COLUMNS;
		double[][] valueBoard = new double[n1][n2];
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				String position = Utilities.getPositionByRowCol(i, j);
				/* In the beginning, these sum up to 39, for each player. */
				valueBoard[i][j] = Utilities.getChessPieceValue(position, this.gameBoard[i][j], this.halfMoveNumber);
			}
		}

		double checkValue;
		if (this.halfMoveNumber <= Constants.MIDDLE_GAME_HALF_MOVES_THRESHOLD) {
			checkValue = Constants.CHECK_VALUE;
		} else {
			checkValue = Constants.CHECK_LATE_VALUE;
		}

		/* Evaluation for the check move. */
		// If last the chessPiece is White and the Black King is in check.
		if (lastMovedPiece.getAllegiance() == Allegiance.WHITE && this.blackKingInCheck) {
			// If the White chessPiece is threatened, and it has no cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += checkValue / lastMovedChessPieceValue;
			}
			// If the White chessPiece is threatened, and it has cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
				whiteScore += checkValue - lastMovedChessPieceValue;
			}
			// If the White chessPiece is not threatened, and it has no cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += checkValue;
			}
			// If the White chessPiece is not threatened, and it has cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
				whiteScore += checkValue;
			}
		}
		// If the last chessPiece is Black and the White King is in check.
		else if (lastMovedPiece.getAllegiance() == Allegiance.BLACK && this.whiteKingInCheck) {
			// If the Black chessPiece is threatened, and it has no cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += checkValue / lastMovedChessPieceValue;
			}
			// If the Black chessPiece is threatened, and it has cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += checkValue - lastMovedChessPieceValue;
			}
			// If the Black chessPiece is not threatened, and it has no cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += checkValue;
			}
			// If the Black chessPiece is not threatened, and it has cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += checkValue;
			}
		}

		/* Evaluation for a capture move. */
		// Check if a White chessPiece captured a Black chessPiece, which means that the score has increased.
		if (lastMovedPiece.getAllegiance() == Allegiance.WHITE && this.lastCapturedPieceValue != 0) {
			// System.out.println("White captured a Black chessPiece.");
			// If the White chessPiece is threatened, and it has no cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += 5 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// whiteScore += 1;
			}
			// If the White chessPiece is threatened, and it has cover.
			else if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
				whiteScore += 10 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// whiteScore += 1;
			}
			// If the White chessPiece is not threatened, and it has no cover.
			else if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += 80 * this.lastCapturedPieceValue;
				// whiteScore += 1;
			}
			// If the White chessPiece is not threatened, and it has cover.
			else if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
				whiteScore += 100 * this.lastCapturedPieceValue;
				// whiteScore += 2;
			}
		}
		// If a Black chessPiece captured a White chessPiece, which means that the score has decreased.
		if (lastMovedPiece.getAllegiance() == Allegiance.BLACK && this.lastCapturedPieceValue != 0) {
			// System.out.println("Black captured a White chessPiece.");
			// If the Black chessPiece is threatened, and it has no cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += 5 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// blackScore += 1;
			}
			// If the Black chessPiece is threatened, and it has cover.
			else if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += 10 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// blackScore += 1;
			}
			// If the Black chessPiece is not threatened, and it has no cover.
			else if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += 80 * this.lastCapturedPieceValue;
				// blackScore += 1;
			}
			// If the Black chessPiece is not threatened, and it has cover.
			else if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += 100 * this.lastCapturedPieceValue;
				// blackScore += 2;
			}
		}

		/* Add to the score the sum of the values of all the pieces of each player. */
		/* Lower the score, for each chessPiece threatened by the opponent. */
		int whiteLegalMoves = 0;
		int blackLegalMoves = 0;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {

				/* Lower the score if the chessPiece is threatened by the opponent.
				 * Else, if the chessPiece is not threatened, increment the score. */
				// If the chessPiece is White.
				if (this.gameBoard[i][j].getAllegiance() == Allegiance.WHITE) {
					// If the White chessPiece is threatened, and it has no cover.
					if (this.tilesThreatenedByBlack[i][j] == 1 && this.tilesThreatenedByWhite[i][j] == 0) {
						whiteScore -= 50 * valueBoard[i][j];
						whiteScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the White chessPiece is threatened, and it has cover.
					else if (this.tilesThreatenedByBlack[i][j] == 1 && this.tilesThreatenedByWhite[i][j] == 1) {
						whiteScore -= 20 * valueBoard[i][j];
						whiteScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the White chessPiece is not threatened, and it has no cover.
					else if (this.tilesThreatenedByBlack[i][j] == 0 && this.tilesThreatenedByWhite[i][j] == 0) {
						// whiteScore += valueBoard[i][j];
						whiteScore += valueBoard[i][j];
					}
					// If the White chessPiece is not threatened, and it has cover.
					else if (this.tilesThreatenedByBlack[i][j] == 0 && this.tilesThreatenedByWhite[i][j] == 1) {
						// whiteScore += valueBoard[i][j];
						whiteScore += valueBoard[i][j];
					}
				}
				// If the chessPiece is Black.
				if (this.gameBoard[i][j].getAllegiance() == Allegiance.BLACK) {
					// If the Black chessPiece is threatened, and it has no cover.
					if (this.tilesThreatenedByWhite[i][j] == 1 && this.tilesThreatenedByBlack[i][j] == 0) {
						blackScore -= 50 * valueBoard[i][j];
						blackScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the Black chessPiece is threatened, and it has cover.
					else if (this.tilesThreatenedByWhite[i][j] == 1 && this.tilesThreatenedByBlack[i][j] == 1) {
						blackScore -= 20 * valueBoard[i][j];
						blackScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the Black chessPiece is not threatened, and it has no cover.
					else if (this.tilesThreatenedByWhite[i][j] == 0 && this.tilesThreatenedByBlack[i][j] == 0) {
						// blackScore += valueBoard[i][j];
						blackScore += valueBoard[i][j];
					}
					// If the Black chessPiece is not threatened, and it has cover.
					else if (this.tilesThreatenedByWhite[i][j] == 0 && this.tilesThreatenedByBlack[i][j] == 1) {
						// blackScore += valueBoard[i][j];
						blackScore += valueBoard[i][j];
					}
				}

				whiteLegalMoves += countLegalMovesFromRowCol(i, j, Constants.WHITE);
				blackLegalMoves += countLegalMovesFromRowCol(i, j, Constants.BLACK);

			}
		}

		whiteScore += Constants.MOBILITY_MULTIPLIER * whiteLegalMoves;
		blackScore += Constants.MOBILITY_MULTIPLIER * blackLegalMoves;

		/* Two bishops remaining check. */
		int numOfWhiteBishops = getNumOfBishops(Allegiance.WHITE);
		int numOfBlackBishops = getNumOfBishops(Allegiance.BLACK);
    	/*
		if (numOfWhiteBishops == 2)
			whiteScore += Constants.TWO_BISHOPS_VALUE;
		if (numOfBlackBishops == 2)
			blackScore += Constants.TWO_BISHOPS_VALUE;
		*/

		// Add extra penalty, if any Queen, Rook, Bishop or Knight is lost, in early game.
		if (this.halfMoveNumber <= Constants.MIDDLE_GAME_HALF_MOVES_THRESHOLD) {
			whiteScore -= (isQueenLost(Allegiance.WHITE)) ? Constants.QUEEN_VALUE * 130 : 0;
			blackScore -= (isQueenLost(Allegiance.BLACK)) ? Constants.QUEEN_VALUE * 130 : 0;

			whiteScore -= (2 - getNumOfRooks(Allegiance.WHITE)) * Constants.ROOK_VALUE * 130;
			blackScore -= (2 - getNumOfRooks(Allegiance.BLACK)) * Constants.ROOK_VALUE * 130;

			whiteScore -= (2 - numOfWhiteBishops) * Constants.BISHOP_VALUE * 130;
			blackScore -= (2 - numOfBlackBishops) * Constants.BISHOP_VALUE * 130;

			whiteScore -= (2 - getNumOfKnights(Allegiance.WHITE)) * Constants.KNIGHT_VALUE * 130;
			blackScore -= (2 - getNumOfKnights(Allegiance.BLACK)) * Constants.KNIGHT_VALUE * 130;
		}

		// Add extra penalty, if any Queen, Rook, Bishop or Knight is lost, in late game.
		else {
			whiteScore -= (isQueenLost(Allegiance.WHITE)) ? Constants.QUEEN_LATE_VALUE * 130 : 0;
			blackScore -= (isQueenLost(Allegiance.BLACK)) ? Constants.QUEEN_LATE_VALUE * 130 : 0;

			whiteScore -= (2 - getNumOfRooks(Allegiance.WHITE)) * Constants.ROOK_LATE_VALUE * 130;
			blackScore -= (2 - getNumOfRooks(Allegiance.BLACK)) * Constants.ROOK_LATE_VALUE * 130;

			whiteScore -= (2 - numOfWhiteBishops) * Constants.BISHOP_LATE_VALUE * 130;
			blackScore -= (2 - numOfBlackBishops) * Constants.BISHOP_LATE_VALUE * 130;

			whiteScore -= (2 - getNumOfKnights(Allegiance.WHITE)) * Constants.KNIGHT_LATE_VALUE * 130;
			blackScore -= (2 - getNumOfKnights(Allegiance.BLACK)) * Constants.KNIGHT_LATE_VALUE * 130;
		}

		return whiteScore - blackScore;
	}

	@SuppressWarnings("unused")
	// Might be used to determine if we are in a late game state.
	private int getPlayerNumOfPiecesLeft(boolean player) {
		int numOfPieces = 0;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if ((player == Constants.WHITE && gameBoard[i][j].getAllegiance() == Allegiance.WHITE)
						|| (player == Constants.BLACK && gameBoard[i][j].getAllegiance() == Allegiance.BLACK))
					numOfPieces++;
			}
		}
		return numOfPieces;
	}

	private boolean isQueenLost(Allegiance playerAllegiance) {
		int n1 = this.gameBoard.length;
		int n2 = this.gameBoard[0].length;
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (this.gameBoard[i][j] instanceof Queen && playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					return false;
				}
			}
		}

		return true;
	}

	private int getNumOfRooks(Allegiance playerAllegiance) {
		int numOfRooks = 0;

		int n1 = this.gameBoard.length;
		int n2 = this.gameBoard[0].length;
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (this.gameBoard[i][j] instanceof Rook && playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfRooks++;
				}
			}
		}

		return numOfRooks;
	}

	private int getNumOfBishops(Allegiance playerAllegiance) {
		int numOfBishops = 0;

		int n1 = this.gameBoard.length;
		int n2 = this.gameBoard[0].length;
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (this.gameBoard[i][j] instanceof Bishop
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfBishops++;
				}
			}
		}

		return numOfBishops;
	}

	private int getNumOfKnights(Allegiance playerAllegiance) {
		int numOfKnights = 0;

		int n1 = this.gameBoard.length;
		int n2 = this.gameBoard[0].length;
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (this.gameBoard[i][j] instanceof Knight
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfKnights++;
				}
			}
		}

		return numOfKnights;
	}

	/*
	 * A state is terminal if there is a win or draw condition.
	 */
	public boolean checkForTerminalState() {
		if (checkForWhiteCheckmate(false)) return true;

		if (checkForBlackCheckmate(false)) return true;

		// Check for White stalemate, only if the last player was Black,
		// meaning that the next player should be White.
		if (whitePlays() && checkForWhiteStalemateDraw()) return true;

		// Check for Black stalemate, only if the last player was White,
		// meaning that the next player should be Black.
		if (blackPlays() && checkForBlackStalemateDraw()) return true;

		return checkForInsufficientMaterialDraw();

		// if (checkForNoPieceCaptureDraw()) return true;
	}

	public boolean isTerminalState() {
		return isWhiteCheckmate() || isBlackCheckmate()
				|| isWhiteStalemateDraw() || isBlackStalemateDraw()
				|| isInsufficientMaterialDraw() /* || checkForNoPieceCaptureDraw() */;
	}

	public Set<String> getNextPositions(String position) {
		Set<String> nextPositions;

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = this.getGameBoard()[row][column];

		nextPositions = gameBoard[row][column].getNextPositions(position, this, false);

		/* Remove positions that lead to the king being in check. */
		nextPositions = removePositionsLeadingToOppositeCheck(position, chessPiece, nextPositions);

		return nextPositions;
	}

	public Set<String> removePositionsLeadingToOppositeCheck(String position, ChessPiece chessPiece,
	                                                         Set<String> nextPositions) {

		ChessBoard initialChessBoard = new ChessBoard(this);

		Set<String> tempNextPositions = new HashSet<>(nextPositions);
		for (String tempNextPosition : tempNextPositions) {
			initialChessBoard.movePieceFromAPositionToAnother(position, tempNextPosition, false);

			int whiteKingRow = Utilities.getRowFromPosition(initialChessBoard.getWhiteKingPosition());
			int whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

			int blackKingRow = Utilities.getRowFromPosition(initialChessBoard.getBlackKingPosition());
			int blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());

			if (chessPiece.getAllegiance() == Allegiance.WHITE
					&& initialChessBoard.getTilesThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 1
					|| chessPiece.getAllegiance() == Allegiance.BLACK
					&& initialChessBoard.getTilesThreatenedByWhite()[blackKingRow][blackKingColumn] == 1) {
				nextPositions.remove(tempNextPosition);
			}

			initialChessBoard = new ChessBoard(this);

		}

		return nextPositions;
	}

	// It should be called after we move any chessPiece in the this.
	// For many chessPiece moves at once we only need to call this method once in the end
	// (for example when calling the initial state of the chessBoard).
	public void setThreats() {

		// First, remove all the threatened areas.
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				this.tilesThreatenedByWhite[i][j] = 0;
				this.tilesThreatenedByBlack[i][j] = 0;
			}
		}

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {

				ChessPiece chessPiece = this.gameBoard[i][j];
				String position = Utilities.getPositionByRowCol(i, j);

				Set<String> threatPositions;

				threatPositions = chessPiece.getNextPositions(position, this, true);

				// System.out.println("position: " + position + ", threatPositions: " + threatPositions);

				if (threatPositions != null && threatPositions.size() != 0) {
					for (String threatPosition : threatPositions) {
						int row = Utilities.getRowFromPosition(threatPosition);
						int column = Utilities.getColumnFromPosition(threatPosition);
						if (chessPiece.getAllegiance() == Allegiance.WHITE)
							this.tilesThreatenedByWhite[row][column] = 1;
						else if (chessPiece.getAllegiance() == Allegiance.BLACK)
							this.tilesThreatenedByBlack[row][column] = 1;
					}
				}

			}
		}

	}

	// Check for White checkmate (if White wins!)
	public boolean checkForWhiteCheckmate(boolean storeKingInCheckMoves) {

		this.isWhiteCheckmate = false;

		ChessBoard initialChessBoard = new ChessBoard(this);

		int blackKingRow = Utilities.getRowFromPosition(this.getBlackKingPosition());
		int blackKingColumn = Utilities.getColumnFromPosition(this.getBlackKingPosition());
		int blackKingThreatened = getTilesThreatenedByWhite()[blackKingRow][blackKingColumn];

		if (storeKingInCheckMoves) {
			this.blackKingInCheckValidPieceMoves = new HashMap<>();
		}

		if (blackKingThreatened == 1) {
			this.blackKingInCheck = true;

			// Check for all possible moves made by Black,
			// that can get the Black king out of a possible check
			// and store them in a variable called "blackKingInCheckValidPieceMoves".
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < NUM_OF_COLUMNS; j++) {
					ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
					if (currentPiece.getAllegiance() == Allegiance.BLACK) {
						String currentPosition = Utilities.getPositionByRowCol(i, j);
						Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);

						Set<String> validBlackKingInCheckTempHintPosition = new HashSet<>();
						for (String currentHintPosition : currentHintPositions) {
							initialChessBoard.movePieceFromAPositionToAnother(currentPosition, currentHintPosition, false);

							blackKingRow = Utilities.getRowFromPosition(initialChessBoard.getBlackKingPosition());
							blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());

							if (initialChessBoard.getTilesThreatenedByWhite()[blackKingRow][blackKingColumn] == 0) {
								blackKingThreatened = 0;
								validBlackKingInCheckTempHintPosition.add(currentHintPosition);
							}

							initialChessBoard = new ChessBoard(this);

						}
						if (storeKingInCheckMoves && validBlackKingInCheckTempHintPosition.size() > 0) {
							this.blackKingInCheckValidPieceMoves.put(currentPosition,
									validBlackKingInCheckTempHintPosition);
						}
					}

				}
			}

			if (blackKingThreatened == 1) {
				this.isWhiteCheckmate = true;
			}

		} else {
			if (storeKingInCheckMoves) {
				this.blackKingInCheck = false;
				this.blackKingInCheckValidPieceMoves = new HashMap<>();
			}
		}

		return this.isWhiteCheckmate;
	}

	// Check for Black checkmate (if Black wins!)
	public boolean checkForBlackCheckmate(boolean storeKingInCheckMoves) {

		this.isBlackCheckmate = false;

		ChessBoard initialChessBoard = new ChessBoard(this);

		int whiteKingRow = Utilities.getRowFromPosition(this.getWhiteKingPosition());
		int whiteKingColumn = Utilities.getColumnFromPosition(this.getWhiteKingPosition());
		int whiteKingThreatened = getTilesThreatenedByBlack()[whiteKingRow][whiteKingColumn];

		if (storeKingInCheckMoves) {
			this.whiteKingInCheckValidPieceMoves = new HashMap<>();
		}

		if (whiteKingThreatened == 1) {
			this.whiteKingInCheck = true;

			// Check for all possible moves made by White,
			// that can get the White king out of a possible check
			// and store them in a variable called "whiteKingInCheckValidPieceMoves".
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < NUM_OF_COLUMNS; j++) {
					ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
					if (currentPiece.getAllegiance() == Allegiance.WHITE) {
						String currentPosition = Utilities.getPositionByRowCol(i, j);
						Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);

						Set<String> validWhiteKingInCheckTempHintPositions = new HashSet<>();
						for (String currentHintPosition : currentHintPositions) {
							initialChessBoard.movePieceFromAPositionToAnother(currentPosition, currentHintPosition, false);

							whiteKingRow = Utilities.getRowFromPosition(initialChessBoard.getWhiteKingPosition());
							whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

							if (initialChessBoard.getTilesThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 0) {
								whiteKingThreatened = 0;
								validWhiteKingInCheckTempHintPositions.add(currentHintPosition);
							}

							initialChessBoard = new ChessBoard(this);

						}
						if (storeKingInCheckMoves && validWhiteKingInCheckTempHintPositions.size() > 0) {
							this.whiteKingInCheckValidPieceMoves.put(currentPosition, validWhiteKingInCheckTempHintPositions);
						}

					}

				}
			}

			if (whiteKingThreatened == 1) {
				this.isBlackCheckmate = true;
			}

		} else {
			if (storeKingInCheckMoves) {
				this.whiteKingInCheck = false;
				this.whiteKingInCheckValidPieceMoves = new HashMap<>();
			}
		}

		return this.isBlackCheckmate;
	}

	// Checks if there are insufficient material for a checkmate, left on the chess board.
	public boolean checkForInsufficientMaterialDraw() {
		boolean whiteHasInsufficientMaterial =
				isLoneKing(Allegiance.WHITE)
						|| isLoneKingPlusOneOrTwoKnights(Allegiance.WHITE) || isLoneKingPlusABishop(Allegiance.WHITE);

		boolean blackHasInsufficientMaterial =
				isLoneKing(Allegiance.BLACK) || isLoneKingPlusOneOrTwoKnights(Allegiance.BLACK)
						|| isLoneKingPlusABishop(Allegiance.BLACK);

		this.isInsufficientMaterialDraw = whiteHasInsufficientMaterial && blackHasInsufficientMaterial;

		boolean isDeadDrawGame = checkForDeadGameDraw();
		if (isDeadDrawGame) {
			this.isInsufficientMaterialDraw = true;
		}

		return this.isInsufficientMaterialDraw;
	}

	public boolean checkForDeadGameDraw() {
		// Check for a special case of draw, the dead game draw.
		// It occurs when only the kings and at least three pawns from each side are left on the board
		// and neither king can cross to the other side of the board.
		boolean isDeadDrawGame = true;
		if (isLoneKingPlusAtLeastThreePawns(Allegiance.WHITE) && isLoneKingPlusAtLeastThreePawns(Allegiance.BLACK)) {

			// Check if the pawns can make any move.
			int n1 = gameBoard.length;
			int n2 = gameBoard[0].length;
			for (int i = 0; i < n1; i++) {
				for (int j = 0; j < n2; j++) {
					if (gameBoard[i][j] instanceof Pawn) {
						String position = Utilities.getPositionByRowCol(i, j);
						Set<String> nextPositions = getNextPositions(position);
						if (nextPositions.size() > 0) {
							isDeadDrawGame = false;
							i = 1000;
							j = 1000;
						}
					}
				}
			}

			if (isDeadDrawGame) {
				// Run algorithm to find if the White king can get to position "A8"
				// in the given number of moves (max depth).
				isDeadDrawGame = !ChessPieceShortestPath.canGoToPosition(this, new King(Allegiance.WHITE),
						whiteKingPosition, "A8", Constants.DEAD_DRAW_MAX_BFS_DEPTH);
			}

		} else {
			isDeadDrawGame = false;
		}

		return isDeadDrawGame;
	}

	// Checks if only a king has remained on the board, on the given player's side.
	public boolean isLoneKing(Allegiance playerAllegiance) {
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (!(getGameBoard()[i][j] instanceof EmptyTile)
						&& !(getGameBoard()[i][j] instanceof King)
						&& playerAllegiance == getGameBoard()[i][j].getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + this.getGameBoard()[i][j]);
					return false;
				}
			}
		}

		return true;
	}

	// Checks if only a king and at least three pawns have remained on the board, on the given player's side.
	public boolean isLoneKingPlusAtLeastThreePawns(Allegiance playerAllegiance) {
		int numOfPawns = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (!(getGameBoard()[i][j] instanceof EmptyTile)
						&& !(getGameBoard()[i][j] instanceof King)
						&& !(getGameBoard()[i][j] instanceof Pawn)
						&& playerAllegiance == getGameBoard()[i][j].getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + this.getGameBoard()[i][j]);
					return false;
				}
				if (getGameBoard()[i][j] instanceof Pawn) {
					numOfPawns++;
				}
			}
		}

		return numOfPawns > 2;
	}

	// Checks if only a king and one or two knights have remained on the board, on the given player's side.
	public boolean isLoneKingPlusOneOrTwoKnights(Allegiance playerAllegiance) {

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (!(getGameBoard()[i][j] instanceof EmptyTile)
						&& !(getGameBoard()[i][j] instanceof King || getGameBoard()[i][j] instanceof Knight)
						&& playerAllegiance == getGameBoard()[i][j].getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + this.getGameBoard()[i][j]);
					return false;
				}
			}
		}

		return true;
	}

	// Checks if only a king and one bishop have remained on the board, on the given player's side.
	public boolean isLoneKingPlusABishop(Allegiance playerAllegiance) {
		int numOfBishops = getNumOfBishops(playerAllegiance);
		if (numOfBishops != 1) return false;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (!(getGameBoard()[i][j] instanceof EmptyTile)
						&& !(getGameBoard()[i][j] instanceof King || getGameBoard()[i][j] instanceof Bishop)
						&& playerAllegiance == getGameBoard()[i][j].getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + this.getGameBoard()[i][j]);
					return false;
				}
			}
		}

		return true;
	}

	// It checks for a stalemate. It gets called after the opposing player, makes a move.
	// A stalemate occurs when a player has no legal moves to make. Then, the game ends in a draw.
	// If the Black player makes a move, then we check for a White player stalemate and vice-versa.
	public boolean checkForWhiteStalemateDraw() {

		this.isWhiteStalemateDraw = true;

		ChessBoard initialChessBoard = new ChessBoard(this);

		// System.out.println("Checking for White stalemate...");
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
				// System.out.println("i: " + i + ", j: " + j + ", tempChessPiece: " + tempChessPiece);
				if (currentPiece.getAllegiance() == Allegiance.WHITE) {
					String currentPosition = Utilities.getPositionByRowCol(i, j);
					Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);

					for (String currentHintPosition : currentHintPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(currentPosition, currentHintPosition, false);

						int whiteKingRow = Utilities.getRowFromPosition(initialChessBoard.getWhiteKingPosition());
						int whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

						// If any move exists without getting the White king in check,
						// then there still are legal moves, and we do not have a stalemate scenario.
						boolean legalMovesExist =
								initialChessBoard.getTilesThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 0;

						initialChessBoard = new ChessBoard(this);

						if (legalMovesExist) {
							this.isWhiteStalemateDraw = false;
							i = j = 1000000;
							break;
						}

					}
				}
			}
		}

		return this.isWhiteStalemateDraw;
	}

	// It checks for a stalemate. It gets called after the opposing player, makes a move.
	// A stalemate occurs when a player has no legal moves to make. Then, the game ends in a draw.
	// If the White player makes a move, then we check for a Black player stalemate and vice-versa.
	public boolean checkForBlackStalemateDraw() {

		this.isBlackStalemateDraw = true;

		ChessBoard initialChessBoard = new ChessBoard(this);

		// System.out.println("Checking for Black stalemate...");
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
				// System.out.println("i: " + i + ", j: " + j + ", tempChessPiece: " + tempChessPiece);
				if (currentPiece.getAllegiance() == Allegiance.BLACK) {
					String currentPosition = Utilities.getPositionByRowCol(i, j);
					Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);

					for (String currentHintPosition : currentHintPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(currentPosition, currentHintPosition, false);

						int blackKingRow = Utilities.getRowFromPosition(initialChessBoard.getBlackKingPosition());
						int blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());

						// If any move exists without getting the Black king in check,
						// then there still are legal moves, and we do not have a stalemate scenario.
						boolean legalMovesExist =
								initialChessBoard.getTilesThreatenedByWhite()[blackKingRow][blackKingColumn] == 0;

						initialChessBoard = new ChessBoard(this);

						if (legalMovesExist) {
							this.isBlackStalemateDraw = false;
							i = j = 1000000;
							break;
						}

					}
				}
			}
		}

		return this.isBlackStalemateDraw;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public void setLastMove(Move lastMove) {
		this.lastMove.setPositions(lastMove.getPositions());
		this.lastMove.setValue(lastMove.getValue());
	}

	public ChessPiece[][] getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(ChessPiece[][] gameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;
		this.gameBoard = new ChessPiece[n1][n2];
		for (int i = 0; i < n1; i++) {
			System.arraycopy(gameBoard[i], 0, this.gameBoard[i], 0, n2);
		}
	}

	public int[][] getTilesThreatenedByWhite() {
		return tilesThreatenedByWhite;
	}

	public void setTilesThreatenedByWhite(int[][] tilesThreatenedByWhite) {
		this.tilesThreatenedByWhite = tilesThreatenedByWhite;
	}

	public int[][] getTilesThreatenedByBlack() {
		return tilesThreatenedByBlack;
	}

	public void setTilesThreatenedByBlack(int[][] tilesThreatenedByBlack) {
		this.tilesThreatenedByBlack = tilesThreatenedByBlack;
	}

	public String getEnPassantPosition() {
		return enPassantPosition;
	}

	public void setEnPassantPosition(String enPassantPosition) {
		this.enPassantPosition = enPassantPosition;
	}

	public boolean isWhiteKingMoved() {
		return whiteKingMoved;
	}

	public void setWhiteKingMoved(boolean whiteKingMoved) {
		this.whiteKingMoved = whiteKingMoved;
	}

	public boolean isLeftWhiteRookMoved() {
		return leftWhiteRookMoved;
	}

	public void setLeftWhiteRookMoved(boolean leftWhiteRookMoved) {
		this.leftWhiteRookMoved = leftWhiteRookMoved;
	}

	public boolean isRightWhiteRookMoved() {
		return rightWhiteRookMoved;
	}

	public void setRightWhiteRookMoved(boolean rightWhiteRookMoved) {
		this.rightWhiteRookMoved = rightWhiteRookMoved;
	}

	public boolean isWhiteQueenSideCastlingAvailable() {
		return !whiteKingMoved && !leftWhiteRookMoved;
	}

	public boolean isWhiteKingSideCastlingAvailable() {
		return !whiteKingMoved && !rightWhiteRookMoved;
	}

	public boolean isBlackKingMoved() {
		return blackKingMoved;
	}

	public void setBlackKingMoved(boolean blackKingMoved) {
		this.blackKingMoved = blackKingMoved;
	}

	public boolean isLeftBlackRookMoved() {
		return leftBlackRookMoved;
	}

	public void setLeftBlackRookMoved(boolean leftBlackRookMoved) {
		this.leftBlackRookMoved = leftBlackRookMoved;
	}

	public boolean isRightBlackRookMoved() {
		return rightBlackRookMoved;
	}

	public void setRightBlackRookMoved(boolean rightBlackRookMoved) {
		this.rightBlackRookMoved = rightBlackRookMoved;
	}

	public boolean isBlackQueenSideCastlingAvailable() {
		return !blackKingMoved && !leftBlackRookMoved;
	}

	public boolean isBlackKingSideCastlingAvailable() {
		return !blackKingMoved && !rightBlackRookMoved;
	}

	public boolean isWhiteCastlingDone() {
		return whiteCastlingDone;
	}

	public void setWhiteCastlingDone(boolean whiteCastlingDone) {
		this.whiteCastlingDone = whiteCastlingDone;
	}

	public boolean isBlackCastlingDone() {
		return blackCastlingDone;
	}

	public void setBlackCastlingDone(boolean blackCastlingDone) {
		this.blackCastlingDone = blackCastlingDone;
	}

	public String getWhiteKingPosition() {
		return whiteKingPosition;
	}

	public void setWhiteKingPosition(String whiteKingPosition) {
		this.whiteKingPosition = whiteKingPosition;
	}

	public String getBlackKingPosition() {
		return blackKingPosition;
	}

	public void setBlackKingPosition(String blackKingPosition) {
		this.blackKingPosition = blackKingPosition;
	}

	public boolean isWhiteKingInCheck() {
		return whiteKingInCheck;
	}

	public void setWhiteKingInCheck(boolean whiteKingInCheck) {
		this.whiteKingInCheck = whiteKingInCheck;
	}

	public boolean isBlackKingInCheck() {
		return blackKingInCheck;
	}

	public void setBlackKingInCheck(boolean blackKingInCheck) {
		this.blackKingInCheck = blackKingInCheck;
	}

	public Map<String, Set<String>> getWhiteKingInCheckValidPieceMoves() {
		return whiteKingInCheckValidPieceMoves;
	}

	public void setWhiteKingInCheckValidPieceMoves(Map<String, Set<String>> whiteKingInCheckValidPieceMoves) {
		this.whiteKingInCheckValidPieceMoves = new HashMap<>(whiteKingInCheckValidPieceMoves);
	}

	public Map<String, Set<String>> getBlackKingInCheckValidPieceMoves() {
		return blackKingInCheckValidPieceMoves;
	}

	public void setBlackKingInCheckValidPieceMoves(Map<String, Set<String>> blackKingInCheckValidPieceMoves) {
		this.blackKingInCheckValidPieceMoves = new HashMap<>(blackKingInCheckValidPieceMoves);
	}

	public boolean whitePlays() {
		return (this.player == Constants.WHITE);
	}

	public boolean blackPlays() {
		return (this.player == Constants.BLACK);
	}

	public boolean getPlayer() {
		return player;
	}

	public void setPlayer(boolean player) {
		this.player = player;
	}

	public boolean getNextPlayer() {
		return !player;
	}

	public int getHalfMoveNumber() {
		return halfMoveNumber;
	}

	public void setHalfMoveNumber(int halfMoveNumber) {
		this.halfMoveNumber = halfMoveNumber;
	}

	public boolean isWhiteCheckmate() {
		return isWhiteCheckmate;
	}

	public void setWhiteCheckmate(boolean isWhiteCheckmate) {
		this.isWhiteCheckmate = isWhiteCheckmate;
	}

	public boolean isBlackCheckmate() {
		return isBlackCheckmate;
	}

	public void setBlackCheckmate(boolean isBlackCheckmate) {
		this.isBlackCheckmate = isBlackCheckmate;
	}

	public boolean isInsufficientMaterialDraw() {
		return isInsufficientMaterialDraw;
	}

	public void setInsufficientMaterialDraw(boolean isInsufficientMaterialDraw) {
		this.isInsufficientMaterialDraw = isInsufficientMaterialDraw;
	}

	public boolean isWhiteStalemateDraw() {
		return isWhiteStalemateDraw;
	}

	public void setWhiteStalemateDraw(boolean isWhiteStalemateDraw) {
		this.isWhiteStalemateDraw = isWhiteStalemateDraw;
	}

	public boolean isBlackStalemateDraw() {
		return isBlackStalemateDraw;
	}

	public void setBlackStalemateDraw(boolean isBlackStalemateDraw) {
		this.isBlackStalemateDraw = isBlackStalemateDraw;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public int getHalfMoveClock() {
		return halfMoveClock;
	}

	public void setHalfMoveClock(int halfMoveClock) {
		this.halfMoveClock = halfMoveClock;
	}

	public boolean checkForNoPieceCaptureDraw() {
		return this.halfMoveClock >= Constants.NO_CAPTURE_DRAW_HALF_MOVES_LIMIT;
	}

	public int getWhiteCapturedPiecesCounter() {
		return whiteCapturedPiecesCounter;
	}

	public void incrementWhiteCapturedPiecesCounter() {
		this.whiteCapturedPiecesCounter++;
	}

	public int getBlackCapturedPiecesCounter() {
		return blackCapturedPiecesCounter;
	}

	public void incrementBlackCapturedPiecesCounter() {
		this.blackCapturedPiecesCounter++;
	}

	public int getScore() {
		return score;
	}

	public void incrementScore() {
		this.score++;
	}

	public double getLastCapturedPieceValue() {
		return lastCapturedPieceValue;
	}

	public void setLastCapturedPieceValue(double lastCapturedPieceValue) {
		this.lastCapturedPieceValue = lastCapturedPieceValue;
	}

	public Set<ChessPiece> getPromotedPieces() {
		return promotedPieces;
	}

	public void setPromotedPieces(Set<ChessPiece> promotedPieces) {
		this.promotedPieces = new HashSet<>(promotedPieces);
	}

	public Set<String> getPositionsToRemove() {
		return positionsToRemove;
	}

	public void setPositionsToRemove(Set<String> positionsToRemove) {
		this.positionsToRemove = new HashSet<>(positionsToRemove);
	}

	public Map<String, ChessPiece> getPiecesToPlace() {
		return piecesToPlace;
	}

	public void setPiecesToPlace(Map<String, ChessPiece> piecesToPlace) {
		this.piecesToPlace = new HashMap<>(piecesToPlace);
	}

	public ChessPiece getCapturedPiece() {
		return capturedPiece;
	}

	public void setCapturedPiece(ChessPiece capturedPiece) {
		this.capturedPiece = capturedPiece;
	}

	@Override
	public String toString() {
		return getChessBoardString(this.gameBoard);
	}

}