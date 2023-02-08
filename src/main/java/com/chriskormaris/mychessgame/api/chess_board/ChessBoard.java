package com.chriskormaris.mychessgame.api.chess_board;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GamePhase;
import com.chriskormaris.mychessgame.api.enumeration.GameResult;
import com.chriskormaris.mychessgame.api.evaluation_function.PeSTOEvaluationUtils;
import com.chriskormaris.mychessgame.api.evaluation_function.ShannonEvaluationUtils;
import com.chriskormaris.mychessgame.api.evaluation_function.SimplifiedEvaluationUtils;
import com.chriskormaris.mychessgame.api.evaluation_function.WukongEvaluationUtils;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.EmptySquare;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.api.util.Utilities;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.chriskormaris.mychessgame.api.util.Constants.NUM_OF_COLUMNS;


@Getter
@Setter
public class ChessBoard {

	private final int numOfRows;

	/* Immediate move that led to this board. */
	private Move lastMove;


	/* The ChessBoard's gameBoard:
	 *      A     B     C     D     E     F     G     H
	 *   _________________________________________________
	 * 8 |(0,0)|(0,1)|(0,2)|(0,3)|(0,4)|(0,5)|(0,6)|(0,7)| 8
	 * 7 |(1,0)|(1,1)|(1,2)|(1,3)|(1,4)|(1,5)|(1,6)|(1,7)| 7
	 * 6 |(2,0)|(2,1)|(2,2)|(2,3)|(2,4)|(2,5)|(2,6)|(2,7)| 6
	 * 5 |(3,0)|(3,1)|(3,2)|(3,3)|(3,4)|(3,5)|(3,6)|(3,7)| 5
	 * 4 |(4,0)|(4,1)|(4,2)|(4,3)|(4,4)|(4,5)|(4,6)|(4,7)| 4
	 * 3 |(5,0)|(5,1)|(5,2)|(5,3)|(5,4)|(5,5)|(5,6)|(5,7)| 3
	 * 2 |(6,0)|(6,1)|(6,2)|(6,3)|(6,4)|(6,5)|(6,6)|(6,7)| 2
	 * 1 |(7,0)|(7,1)|(7,2)|(7,3)|(7,4)|(7,5)|(7,6)|(7,7)| 1
	 *   -------------------------------------------------
	 *      A     B     C     D     E     F     G     H
	 * E.g: A1 = (7,0), H8 = (0,7), B3 = (5,1), C2 = (6,2) etc. */
	private ChessPiece[][] gameBoard;

	/* A board with:
	 * 1 for areas threatened by white pieces.
	 * 0 for areas not threatened by white pieces. */
	private int[][] squaresThreatenedByWhite;

	/* A board with:
	 * 1 for areas threatened by black pieces.
	 * 0 for areas not threatened by black pieces. */
	private int[][] squaresThreatenedByBlack;

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
	 * or the move was not a leap of two squares forward,
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

	private GameResult gameResult;

	private int score;

	// These variables are used for "castling" and "en passant".
	private Set<String> positionsToRemove;
	private Map<String, ChessPiece> piecesToPlace;

	private ChessPiece capturedEnPassantPiece;

	private int whiteCapturedPiecesCounter;
	private int blackCapturedPiecesCounter;

	public ChessBoard() {
		this(Constants.DEFAULT_NUM_OF_ROWS);
	}

	public ChessBoard(int numOfRows) {
		this.numOfRows = numOfRows;

		this.lastMove = new Move();

		this.gameBoard = new ChessPiece[numOfRows][NUM_OF_COLUMNS];
		placePiecesToStartingPositions();

		// this.gameBoard = FenUtils.createGameBoard(this, Constants.DEFAULT_STARTING_PIECES);

		this.squaresThreatenedByWhite = new int[numOfRows][NUM_OF_COLUMNS];
		this.squaresThreatenedByBlack = new int[numOfRows][NUM_OF_COLUMNS];

		this.whiteKingPosition = "E1";
		this.blackKingPosition = "E" + numOfRows;

		this.player = Constants.WHITE;  // White plays first.

		this.enPassantPosition = "-";

		this.halfMoveClock = 0;
		this.halfMoveNumber = 1;

		this.gameResult = GameResult.NONE;

		this.score = 0;

		this.whiteKingInCheckValidPieceMoves = new HashMap<>();
		this.blackKingInCheckValidPieceMoves = new HashMap<>();

		this.positionsToRemove = new HashSet<>();
		this.piecesToPlace = new HashMap<>();

		setThreats();
	}


	// Copy constructor
	public ChessBoard(ChessBoard otherBoard) {
		this.numOfRows = otherBoard.numOfRows;

		this.lastMove = new Move(otherBoard.lastMove);

		this.gameBoard = Utilities.copyGameBoard(otherBoard.getGameBoard());
		this.squaresThreatenedByWhite = Utilities.copyIntBoard(otherBoard.getSquaresThreatenedByWhite());
		this.squaresThreatenedByBlack = Utilities.copyIntBoard(otherBoard.getSquaresThreatenedByBlack());

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

		this.gameResult = otherBoard.getGameResult();

		this.score = otherBoard.getScore();

		this.positionsToRemove = new HashSet<>(otherBoard.getPositionsToRemove());
		this.piecesToPlace = new HashMap<>(otherBoard.getPiecesToPlace());
		this.capturedEnPassantPiece = otherBoard.getCapturedEnPassantPiece();

		this.whiteCapturedPiecesCounter = otherBoard.getWhiteCapturedPiecesCounter();
		this.blackCapturedPiecesCounter = otherBoard.getBlackCapturedPiecesCounter();
	}

	public void placePiecesToStartingPositions() {
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				this.gameBoard[i][j] = new EmptySquare();
			}
		}

		for (int j = 0; j < NUM_OF_COLUMNS; j++) {
			this.gameBoard[1][j] = new Pawn(Allegiance.BLACK);  // 2nd row
		}

		this.gameBoard[0][0] = new Rook(Allegiance.BLACK);  // A8
		this.gameBoard[0][1] = new Knight(Allegiance.BLACK);  // B8
		this.gameBoard[0][2] = new Bishop(Allegiance.BLACK);  // C8
		this.gameBoard[0][3] = new Queen(Allegiance.BLACK);  // D8

		String blackKingPosition = "E" + numOfRows;
		this.setBlackKingPosition(blackKingPosition);
		this.gameBoard[0][4] = new King(Allegiance.BLACK);

		this.gameBoard[0][5] = new Bishop(Allegiance.BLACK);  // F8
		this.gameBoard[0][6] = new Knight(Allegiance.BLACK);  // G8
		this.gameBoard[0][7] = new Rook(Allegiance.BLACK);  // H8


		for (int j = 0; j < NUM_OF_COLUMNS; j++) {
			this.gameBoard[numOfRows - 2][j] = new Pawn(Allegiance.WHITE);  // (n-th - 1) row
		}

		this.gameBoard[numOfRows - 1][0] = new Rook(Allegiance.WHITE);  // A1
		this.gameBoard[numOfRows - 1][1] = new Knight(Allegiance.WHITE);  // B1
		this.gameBoard[numOfRows - 1][2] = new Bishop(Allegiance.WHITE);  // C1
		this.gameBoard[numOfRows - 1][3] = new Queen(Allegiance.WHITE);  // D1

		String whiteKingPosition = "E1";
		this.setWhiteKingPosition(whiteKingPosition);
		this.gameBoard[numOfRows - 1][4] = new King(Allegiance.WHITE);

		this.gameBoard[numOfRows - 1][5] = new Bishop(Allegiance.WHITE);  // F1
		this.gameBoard[numOfRows - 1][6] = new Knight(Allegiance.WHITE);  // G1
		this.gameBoard[numOfRows - 1][7] = new Rook(Allegiance.WHITE);  // H1
	}

	// It prints the chess board on the console.
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
				output.append(" ").append(chessBoard[i][j].getChessPieceChar()).append(" |");
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

		int rowStart = Utilities.getRowFromPosition(positionStart, numOfRows);
		int columnStart = Utilities.getColumnFromPosition(positionStart);
		ChessPiece chessPiece = this.gameBoard[rowStart][columnStart];
		// if (displayMove) {
		// 	System.out.println("rowStart: " + rowStart + ", columnStart: " + columnStart);
		// 	System.out.println("chessPiece:" + chessPiece);
		// }

		int rowEnd = Utilities.getRowFromPosition(positionEnd, numOfRows);
		int columnEnd = Utilities.getColumnFromPosition(positionEnd);
		ChessPiece endSquare = this.gameBoard[rowEnd][columnEnd];
		// if (displayMove) {
		// 	System.out.println("rowEnd: " + rowEnd + ", columnEnd: " + columnEnd);
		// 	System.out.println("endSquare:" + endSquare);
		// }

		if (positionsToRemove.size() > 0) {
			positionsToRemove.clear();
		}
		if (piecesToPlace.size() > 0) {
			piecesToPlace.clear();
		}
		if (capturedEnPassantPiece != null) {
			capturedEnPassantPiece = null;
		}

		// Allow only valid moves, for all the chess board pieces.
		// Move only if the square is empty or the square contains an opponent chessPiece.
		// Also allow castling, en passant and promotion moves.
		if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()) {

			Set<String> castlingPositions = null;
			if (chessPiece instanceof King) {
				castlingPositions = King.getCastlingPositions(positionStart, this);
			}

			this.gameBoard[rowStart][columnStart] = new EmptySquare();
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
						this.gameBoard[this.numOfRows - 1][0] = new EmptySquare();
						this.gameBoard[this.numOfRows - 1][3] = new Rook(Allegiance.WHITE);
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
						this.gameBoard[this.numOfRows - 1][7] = new EmptySquare();
						this.gameBoard[this.numOfRows - 1][5] = new Rook(Allegiance.WHITE);
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
						this.gameBoard[0][0] = new EmptySquare();
						this.gameBoard[0][3] = new Rook(Allegiance.BLACK);
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
						this.gameBoard[0][7] = new EmptySquare();
						this.gameBoard[0][5] = new Rook(Allegiance.BLACK);
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
						!(Utilities.getChessPieceFromPosition(this.gameBoard, "A" + numOfRows)
								instanceof Rook))) {
					this.setLeftBlackRookMoved(true);
					this.setBlackCastlingDone(false);
				} else if (!this.isRightBlackRookMoved() && (positionStart.equals("H" + numOfRows) ||
						!(Utilities.getChessPieceFromPosition(this.gameBoard, "H" + numOfRows)
								instanceof Rook))) {
					this.setRightBlackRookMoved(true);
					this.setBlackCastlingDone(false);
				}
			}

			// Implementation of "en passant" functionality and "pawn promotion" here.
			if (chessPiece instanceof Pawn) {

				/* En passant implementation */

				// Remove the captured en passant pieces.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd + 1 < numOfRows) {

					String twoStepsForwardBlackPawnPosition = Utilities.getPositionByRowCol(
							rowEnd + 1,
							columnEnd,
							numOfRows
					);
					// if (displayMove)
					// System.out.println("twoStepsForwardBlackPawnPosition: " + twoStepsForwardBlackPawnPosition);
					int twoStepsForwardBlackPawnPositionRow = Utilities.getRowFromPosition(
							twoStepsForwardBlackPawnPosition,
							numOfRows
					);
					int twoStepsForwardBlackPawnPositionColumn = Utilities.getColumnFromPosition(
							twoStepsForwardBlackPawnPosition);

					// White pawn captures black pawn.
					ChessPiece possibleBlackEnPassantCapturedPawn = this.gameBoard[rowEnd + 1][columnEnd];
					if (possibleBlackEnPassantCapturedPawn instanceof Pawn
							&& possibleBlackEnPassantCapturedPawn.getAllegiance() == Allegiance.BLACK
							&& this.enPassantPosition.equals(
							Utilities.getPositionByRowCol(rowEnd, columnEnd, numOfRows))) {

						if (displayMove) {
							positionsToRemove.add(twoStepsForwardBlackPawnPosition);
							capturedEnPassantPiece = possibleBlackEnPassantCapturedPawn;
						} else {
							this.gameBoard
									[twoStepsForwardBlackPawnPositionRow]
									[twoStepsForwardBlackPawnPositionColumn] = new EmptySquare();
						}
						this.enPassantPosition = "-";
					}

					// Black pawn captures white pawn.
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd - 1 >= 0) {

					String twoStepsForwardWhitePawnPosition = Utilities.getPositionByRowCol(
							rowEnd - 1,
							columnEnd,
							numOfRows
					);
					int twoStepsForwardWhitePawnPositionRow = Utilities.getRowFromPosition(
							twoStepsForwardWhitePawnPosition,
							numOfRows
					);
					int twoStepsForwardWhitePawnPositionColumn = Utilities.getColumnFromPosition(
							twoStepsForwardWhitePawnPosition);

					// Black pawn captures white pawn.
					ChessPiece possibleWhiteEnPassantCapturedPawn = this.gameBoard[rowEnd - 1][columnEnd];
					if (possibleWhiteEnPassantCapturedPawn instanceof Pawn
							&& possibleWhiteEnPassantCapturedPawn.getAllegiance() == Allegiance.WHITE
							&& this.enPassantPosition.equals(
							Utilities.getPositionByRowCol(rowEnd, columnEnd, numOfRows))) {

						if (displayMove) {
							positionsToRemove.add(twoStepsForwardWhitePawnPosition);
							capturedEnPassantPiece = possibleWhiteEnPassantCapturedPawn;
						} else {
							this.gameBoard
									[twoStepsForwardWhitePawnPositionRow]
									[twoStepsForwardWhitePawnPositionColumn] = new EmptySquare();
						}
						this.enPassantPosition = "-";
					}

				}

				// Save the two-step forward moves as one step forward move.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd == rowStart - 2) {
					this.enPassantPosition = Utilities.getPositionByRowCol(rowStart - 1, columnStart, numOfRows);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == rowStart + 2) {
					this.enPassantPosition = Utilities.getPositionByRowCol(rowStart + 1, columnStart, numOfRows);
				} else {
					this.enPassantPosition = "-";
				}

				/* Pawn promotion implementation */
				// If a pawn is going to be promoted and this is not a display move,
				// automatically choose the best promotion piece, based on the best outcome.
				if ((chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd == 0
						|| chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == this.numOfRows - 1)
						&& !displayMove) {
					automaticPawnPromotion(chessPiece, positionEnd, false);
				}
			} else {
				this.enPassantPosition = "-";
			}

			setThreats();

			// Increase the halfMoveClock if no capture has occurred.
			if (endSquare.getAllegiance() == Allegiance.EMPTY) {
				halfMoveClock++;
			} else {  // a capture has occurred
				halfMoveClock = 0;
			}

			// If a chessPiece capture has occurred.
			if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)) {
				updateScoreAfterPieceCapture(endSquare);

				if (!displayMove) {
					incrementCapturedPiecesCounter(endSquare);
				}
			}
		}
	}

	private void updateScoreAfterPieceCapture(ChessPiece endSquare) {
		if (endSquare.isPromoted()) {
			if (endSquare.getAllegiance() == Allegiance.WHITE) {
				score -= Constants.PAWN_SCORE_VALUE;
			} else if (endSquare.getAllegiance() == Allegiance.BLACK) {
				score += Constants.PAWN_SCORE_VALUE;
			}
		} else if (endSquare.getAllegiance() == Allegiance.WHITE) {
			if (endSquare instanceof Pawn) {
				score -= Constants.PAWN_SCORE_VALUE;
			} else if (endSquare instanceof Rook) {
				score -= Constants.ROOK_SCORE_VALUE;
			} else if (endSquare instanceof Knight) {
				score -= Constants.KNIGHT_SCORE_VALUE;
			} else if (endSquare instanceof Bishop) {
				score -= Constants.BISHOP_SCORE_VALUE;
			} else if (endSquare instanceof Queen) {
				score -= Constants.QUEEN_SCORE_VALUE;
			}
		} else if (endSquare.getAllegiance() == Allegiance.BLACK) {
			if (endSquare instanceof Pawn) {
				score += Constants.PAWN_SCORE_VALUE;
			} else if (endSquare instanceof Rook) {
				score += Constants.ROOK_SCORE_VALUE;
			} else if (endSquare instanceof Knight) {
				score += Constants.KNIGHT_SCORE_VALUE;
			} else if (endSquare instanceof Bishop) {
				score += Constants.BISHOP_SCORE_VALUE;
			} else if (endSquare instanceof Queen) {
				score += Constants.QUEEN_SCORE_VALUE;
			}
		}
	}

	public void automaticPawnPromotion(ChessPiece chessPiece, String positionEnd, boolean displayMove) {
		ChessPiece queen = new Queen(chessPiece.getAllegiance(), true);
		ChessPiece rook = new Rook(chessPiece.getAllegiance(), true);
		ChessPiece bishop = new Bishop(chessPiece.getAllegiance(), true);
		ChessPiece knight = new Knight(chessPiece.getAllegiance(), true);

		ChessPiece[] promotionChessPieces = {queen, rook, bishop, knight};

		ChessBoard chessBoard = new ChessBoard(this);

		// System.out.println("Printing Knight promotion board (before)...");
		// System.out.println(chessBoard);

		int rowEnd = Utilities.getRowFromPosition(positionEnd, numOfRows);
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

		ChessPiece promotedPiece = knight;
		// If promoting to Knight does not cause a mate, then try other promotions.
		if (!(chessPiece.getAllegiance() == Allegiance.WHITE
				&& chessBoard.getGameResult() == GameResult.WHITE_CHECKMATE)
				&& !(chessPiece.getAllegiance() == Allegiance.BLACK
				&& chessBoard.getGameResult() == GameResult.BLACK_CHECKMATE)) {
			for (ChessPiece currentPromotionPiece : promotionChessPieces) {
				promotedPiece = currentPromotionPiece;
				chessBoard.getGameBoard()[rowEnd][columnEnd] = promotedPiece;
				chessBoard.setThreats();

				if (chessPiece.getAllegiance() == Allegiance.WHITE && !chessBoard.checkForBlackStalemateDraw()
						||
						chessPiece.getAllegiance() == Allegiance.BLACK && !chessBoard.checkForWhiteStalemateDraw()) {
					break;
				}
				// If Stalemate can't be avoided, at least end the game with a Queen promotion.
				else if (currentPromotionPiece instanceof Knight) {
					promotedPiece = queen;
				}
			}
		}
		this.gameBoard[rowEnd][columnEnd] = promotedPiece;
		if (displayMove) {
			piecesToPlace.put(positionEnd, promotedPiece);
		}
	}

	/* Generates the children of the state
	 * Any square in the board that is empty,
	 * or is an opponent chessPiece, results to a child.
	 * Some special cases include "en passant" and castling.
	 */
	public List<ChessBoard> getChildren(Allegiance allegiance, EvaluationFunction evaluationFunction) {
		List<ChessBoard> children = new ArrayList<>();

		// int childPlayer = (getLastPlayer() == Constants.WHITE) ? Constants.BLACK : Constants.BLACK;

		// int i = 0;
		// System.out.println("**********************************************");
		for (int row = 0; row < numOfRows; row++) {
			for (int column = 0; column < NUM_OF_COLUMNS; column++) {
				ChessPiece chessPiece = this.gameBoard[row][column];
				if (allegiance == chessPiece.getAllegiance()) {
					String initialPosition = Utilities.getPositionByRowCol(row, column, numOfRows);
					Set<String> nextPositions = new HashSet<>();
					if (!this.whiteKingInCheck && whitePlays() || !this.blackKingInCheck && blackPlays()) {
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

							// Move move = new Move(moves, evaluate(evaluationFunction));
							Move move = new Move(moves);

							// move.setValue(this.evaluate(evaluationFunction));

							child.makeMove(move, false);
							this.player = !this.player;

							// System.out.println("**********************************************");
							child.getLastMove().setPositions(moves);
							child.getLastMove().setValue(child.evaluate(evaluationFunction));
							// System.out.println("**********************************************\n");

							// System.out.println("**********************************************");
							// System.out.print("player " + player + ", child " + i);
							// System.out.println(", last move -> " + child.getLastMove());
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

	/* Evaluation Function. 4 options:
	 * 1) Simplified
	 * 2) PeSTO's
	 * 3) Wukong
	 * 4) Shannon's */
	public double evaluate(EvaluationFunction evaluationFunction) {
		if (checkForWhiteCheckmate(false)) return Constants.CHECKMATE_VALUE;
		if (checkForBlackCheckmate(false)) return -Constants.CHECKMATE_VALUE;
		if (checkForWhiteStalemateDraw()) return 0;
		if (checkForBlackStalemateDraw()) return 0;
		if (checkForInsufficientMaterialDraw()) return 0;
		if (checkForNoPieceCaptureDraw()) return 0;

		if (evaluationFunction == EvaluationFunction.SIMPLIFIED) {
			return simplifiedEvaluation();
		} else if (evaluationFunction == EvaluationFunction.PESTO) {
			return pestoEvaluation();
		} else if (evaluationFunction == EvaluationFunction.WUKONG) {
			return wukongEvaluation();
		} else if (evaluationFunction == EvaluationFunction.SHANNON) {
			return shannonEvaluation();
		}
		return 0;
	}

	// Simplified Evaluation Function.
	private double simplifiedEvaluation() {
		int score = 0;
		GamePhase gamePhase = SimplifiedEvaluationUtils.getGamePhase(this, this.halfMoveNumber);

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = this.gameBoard[i][j];
				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					score += SimplifiedEvaluationUtils.getPieceCentipawnValue(chessPiece);
					score += SimplifiedEvaluationUtils.getPieceSquareValue(i, j, chessPiece, gamePhase);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					score -= SimplifiedEvaluationUtils.getPieceCentipawnValue(chessPiece);

					int row = numOfRows - 1 - i;
					score -= SimplifiedEvaluationUtils.getPieceSquareValue(row, j, chessPiece, gamePhase);
				}
			}
		}

		return score;
	}

	// PeSTO's Evaluation Function.
	private double pestoEvaluation() {
		int gamePhase = 0;
		int openingScore = 0;
		int endgameScore = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = this.gameBoard[i][j];
				gamePhase += Utilities.getPieceGamePhaseValue(chessPiece);

				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					openingScore += PeSTOEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.OPENING);
					endgameScore += PeSTOEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.ENDGAME);

					openingScore += PeSTOEvaluationUtils.getPieceSquareValue(i, j, chessPiece, GamePhase.OPENING);
					endgameScore += PeSTOEvaluationUtils.getPieceSquareValue(i, j, chessPiece, GamePhase.ENDGAME);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					openingScore -= PeSTOEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.OPENING);
					endgameScore -= PeSTOEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.ENDGAME);

					int row = numOfRows - 1 - i;
					openingScore -= PeSTOEvaluationUtils.getPieceSquareValue(row, j, chessPiece, GamePhase.OPENING);
					endgameScore -= PeSTOEvaluationUtils.getPieceSquareValue(row, j, chessPiece, GamePhase.ENDGAME);
				}
			}
		}

		// In case of early promotion, the "gamePhase" value could be more than 24.
		int openingGamePhase = Math.min(gamePhase, 24);
		int endGamePhase = 24 - openingGamePhase;
		return (openingScore * openingGamePhase + endgameScore * endGamePhase) / 24.0;
	}

	// Wukong Evaluation Function.
	private double wukongEvaluation() {
		int gamePhase = 0;
		int openingScore = 0;
		int endgameScore = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = this.gameBoard[i][j];
				gamePhase += Utilities.getPieceGamePhaseValue(chessPiece);

				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					openingScore += WukongEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.OPENING);
					endgameScore += WukongEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.ENDGAME);

					openingScore += WukongEvaluationUtils.getPieceSquareValue(i, j, chessPiece, GamePhase.OPENING);
					endgameScore += WukongEvaluationUtils.getPieceSquareValue(i, j, chessPiece, GamePhase.ENDGAME);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					openingScore -= WukongEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.OPENING);
					endgameScore -= WukongEvaluationUtils.getPieceCentipawnValue(chessPiece, GamePhase.ENDGAME);

					int row = numOfRows - 1 - i;
					openingScore -= WukongEvaluationUtils.getPieceSquareValue(row, j, chessPiece, GamePhase.OPENING);
					endgameScore -= WukongEvaluationUtils.getPieceSquareValue(row, j, chessPiece, GamePhase.ENDGAME);
				}
			}
		}

		// In case of early promotion, the "gamePhase" value could be more than 24.
		int openingGamePhase = Math.min(gamePhase, 24);
		int endGamePhase = 24 - openingGamePhase;
		return (openingScore * openingGamePhase + endgameScore * endGamePhase) / 24.0;
	}

	// Shannon's Evaluation Function.
	private double shannonEvaluation() {
		int score = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = this.gameBoard[i][j];

				String position = Utilities.getPositionByRowCol(i, j, numOfRows);
				// System.out.print("chessPiece: " + chessPiece + ", i: " + i + ", j: " + j);
				// System.out.println(", position: " + position);
				int numberOfLegalMoves = chessPiece.getNextPositions(position, this, false).size();

				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					score += ShannonEvaluationUtils.getPieceValue(chessPiece);
					score += ShannonEvaluationUtils.MOBILITY_MULTIPLIER * numberOfLegalMoves;
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					score -= ShannonEvaluationUtils.getPieceValue(chessPiece);
					score -= ShannonEvaluationUtils.MOBILITY_MULTIPLIER * numberOfLegalMoves;
				}

				if (chessPiece instanceof Pawn) {
					Pawn pawn = (Pawn) chessPiece;
					if (pawn.isDoubledPawn(position, this) || pawn.isBlockedPawn(position, this)
							|| pawn.isIsolatedPawn(position, this)) {
						if (pawn.getAllegiance() == Allegiance.WHITE) {
							score -= ShannonEvaluationUtils.DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						} else if (pawn.getAllegiance() == Allegiance.BLACK) {
							score += ShannonEvaluationUtils.DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						}
					}
				}

			}
		}

		return score;
	}

	private int countPawns(Allegiance playerAllegiance) {
		int numOfPawns = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (this.gameBoard[i][j] instanceof Pawn
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfPawns++;
				}
			}
		}

		return numOfPawns;
	}

	private int countKnights(Allegiance playerAllegiance) {
		int numOfKnights = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (this.gameBoard[i][j] instanceof Knight
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfKnights++;
				}
			}
		}

		return numOfKnights;
	}

	private int countBishops(Allegiance playerAllegiance) {
		int numOfBishops = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (this.gameBoard[i][j] instanceof Bishop
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfBishops++;
				}
			}
		}

		return numOfBishops;
	}

	private int countRooks(Allegiance playerAllegiance) {
		int numOfRooks = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (this.gameBoard[i][j] instanceof Rook
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfRooks++;
				}
			}
		}

		return numOfRooks;
	}

	public int countQueens(Allegiance playerAllegiance) {
		int numOfQueens = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (this.gameBoard[i][j] instanceof Queen
						&& playerAllegiance == this.gameBoard[i][j].getAllegiance()) {
					numOfQueens++;
				}
			}
		}

		return numOfQueens;
	}

	// A minor piece is considered a Knight or a Bishop.
	// The method is true if no more than one Queen exists,
	// and the other pieces are all Pawns plus one Knight or Bishop maximum.
	public boolean isQueenPlusOneMinorPieceMaximum(Allegiance playerAllegiance) {
		int numOfQueens = countQueens(playerAllegiance);
		if (numOfQueens == 0) return true;
		if (numOfQueens > 1) return false;

		int numOfKnights = countKnights(playerAllegiance);
		int numOfBishops = countBishops(playerAllegiance);
		if (numOfKnights + numOfBishops > 1) return false;

		int numOfRooks = countRooks(playerAllegiance);
		return numOfRooks <= 0;
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

		if (checkForInsufficientMaterialDraw()) return true;

		return checkForNoPieceCaptureDraw();
	}

	public boolean isTerminalState() {
		return this.gameResult != GameResult.NONE;
	}

	public Set<String> getNextPositions(String position) {
		Set<String> nextPositions;

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position, numOfRows);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = this.getGameBoard()[row][column];

		nextPositions = gameBoard[row][column].getNextPositions(position, this, false);

		/* Remove positions that lead to the king being in check. */
		nextPositions = removePositionsLeadingToOppositeCheck(position, chessPiece, nextPositions);

		return nextPositions;
	}

	public Set<String> removePositionsLeadingToOppositeCheck(
			String position,
			ChessPiece chessPiece,
			Set<String> nextPositions
	) {
		ChessBoard initialChessBoard = new ChessBoard(this);

		Set<String> tempNextPositions = new HashSet<>(nextPositions);
		for (String tempNextPosition : tempNextPositions) {
			initialChessBoard.movePieceFromAPositionToAnother(position, tempNextPosition, false);

			int whiteKingRow = Utilities.getRowFromPosition(initialChessBoard.getWhiteKingPosition(), numOfRows);
			int whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

			int blackKingRow = Utilities.getRowFromPosition(initialChessBoard.getBlackKingPosition(), numOfRows);
			int blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());

			if (chessPiece.getAllegiance() == Allegiance.WHITE
					&& initialChessBoard.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 1
					|| chessPiece.getAllegiance() == Allegiance.BLACK
					&& initialChessBoard.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn] == 1) {
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
				this.squaresThreatenedByWhite[i][j] = 0;
				this.squaresThreatenedByBlack[i][j] = 0;
			}
		}

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {

				ChessPiece chessPiece = this.gameBoard[i][j];
				String position = Utilities.getPositionByRowCol(i, j, numOfRows);

				Set<String> threatPositions;

				threatPositions = chessPiece.getNextPositions(position, this, true);

				// System.out.println("position: " + position + ", threatPositions: " + threatPositions);

				if (threatPositions != null && threatPositions.size() != 0) {
					for (String threatPosition : threatPositions) {
						int row = Utilities.getRowFromPosition(threatPosition, numOfRows);
						int column = Utilities.getColumnFromPosition(threatPosition);
						if (chessPiece.getAllegiance() == Allegiance.WHITE)
							this.squaresThreatenedByWhite[row][column] = 1;
						else if (chessPiece.getAllegiance() == Allegiance.BLACK)
							this.squaresThreatenedByBlack[row][column] = 1;
					}
				}

			}
		}

	}

	// Check for White checkmate (if White wins!)
	public boolean checkForWhiteCheckmate(boolean storeKingInCheckMoves) {
		boolean isWhiteCheckmate = false;

		ChessBoard initialChessBoard = new ChessBoard(this);

		int blackKingRow = Utilities.getRowFromPosition(this.getBlackKingPosition(), numOfRows);
		int blackKingColumn = Utilities.getColumnFromPosition(this.getBlackKingPosition());
		int blackKingThreatened = getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn];

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
						String currentPosition = Utilities.getPositionByRowCol(i, j, numOfRows);
						Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

						Set<String> validBlackKingInCheckTempNextPosition = new HashSet<>();
						for (String nextPosition : nextPositions) {
							initialChessBoard.movePieceFromAPositionToAnother(
									currentPosition,
									nextPosition,
									false
							);

							blackKingRow = Utilities.getRowFromPosition(
									initialChessBoard.getBlackKingPosition(),
									numOfRows
							);
							blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());

							if (initialChessBoard.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn] == 0) {
								blackKingThreatened = 0;
								validBlackKingInCheckTempNextPosition.add(nextPosition);
							}

							initialChessBoard = new ChessBoard(this);
						}
						if (storeKingInCheckMoves && validBlackKingInCheckTempNextPosition.size() > 0) {
							this.blackKingInCheckValidPieceMoves.put(
									currentPosition,
									validBlackKingInCheckTempNextPosition
							);
						}
					}
				}
			}

			if (blackKingThreatened == 1) {
				isWhiteCheckmate = true;
				this.gameResult = GameResult.WHITE_CHECKMATE;
			}
		} else {
			if (storeKingInCheckMoves) {
				this.blackKingInCheck = false;
				this.blackKingInCheckValidPieceMoves = new HashMap<>();
			}
		}

		return isWhiteCheckmate;
	}

	// Check for Black checkmate (if Black wins!)
	public boolean checkForBlackCheckmate(boolean storeKingInCheckMoves) {
		boolean isBlackCheckmate = false;

		ChessBoard initialChessBoard = new ChessBoard(this);

		int whiteKingRow = Utilities.getRowFromPosition(this.getWhiteKingPosition(), numOfRows);
		int whiteKingColumn = Utilities.getColumnFromPosition(this.getWhiteKingPosition());
		int whiteKingThreatened = getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn];

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
						String currentPosition = Utilities.getPositionByRowCol(i, j, numOfRows);
						Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

						Set<String> validWhiteKingInCheckTempNextPositions = new HashSet<>();
						for (String nextPosition : nextPositions) {
							initialChessBoard.movePieceFromAPositionToAnother(
									currentPosition,
									nextPosition,
									false
							);

							whiteKingRow = Utilities.getRowFromPosition(
									initialChessBoard.getWhiteKingPosition(),
									numOfRows
							);
							whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

							if (initialChessBoard.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 0) {
								whiteKingThreatened = 0;
								validWhiteKingInCheckTempNextPositions.add(nextPosition);
							}

							initialChessBoard = new ChessBoard(this);
						}
						if (storeKingInCheckMoves && validWhiteKingInCheckTempNextPositions.size() > 0) {
							this.whiteKingInCheckValidPieceMoves.put(
									currentPosition,
									validWhiteKingInCheckTempNextPositions
							);
						}
					}
				}
			}

			if (whiteKingThreatened == 1) {
				isBlackCheckmate = true;
				this.gameResult = GameResult.BLACK_CHECKMATE;
			}
		} else {
			if (storeKingInCheckMoves) {
				this.whiteKingInCheck = false;
				this.whiteKingInCheckValidPieceMoves = new HashMap<>();
			}
		}

		return isBlackCheckmate;
	}

	// It checks for a stalemate. It gets called after the opposing player, makes a move.
	// A stalemate occurs when a player has no legal moves to make. Then, the game ends in a draw.
	// If the Black player makes a move, then we check for a White player stalemate and vice-versa.
	public boolean checkForWhiteStalemateDraw() {
		boolean isWhiteStalemateDraw = true;

		ChessBoard initialChessBoard = new ChessBoard(this);

		// System.out.println("Checking for White stalemate...");
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
				// System.out.println("i: " + i + ", j: " + j + ", tempChessPiece: " + tempChessPiece);
				if (currentPiece.getAllegiance() == Allegiance.WHITE) {
					String currentPosition = Utilities.getPositionByRowCol(i, j, numOfRows);
					Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

					for (String nextPosition : nextPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(
								currentPosition,
								nextPosition,
								false
						);

						int whiteKingRow = Utilities.getRowFromPosition(
								initialChessBoard.getWhiteKingPosition(),
								numOfRows
						);
						int whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

						// If any move exists without getting the White king in check,
						// then there still are legal moves, and we do not have a stalemate scenario.
						boolean legalMovesExist =
								initialChessBoard.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 0;

						initialChessBoard = new ChessBoard(this);

						if (legalMovesExist) {
							isWhiteStalemateDraw = false;
							i = j = 1000000;
							break;
						}

					}
				}
			}
		}

		if (isWhiteStalemateDraw) {
			this.gameResult = GameResult.WHITE_STALEMATE_DRAW;
		}

		return isWhiteStalemateDraw;
	}

	// It checks for a stalemate. It gets called after the opposing player, makes a move.
	// A stalemate occurs when a player has no legal moves to make. Then, the game ends in a draw.
	// If the White player makes a move, then we check for a Black player stalemate and vice-versa.
	public boolean checkForBlackStalemateDraw() {
		boolean isBlackStalemateDraw = true;

		ChessBoard initialChessBoard = new ChessBoard(this);

		// System.out.println("Checking for Black stalemate...");
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
				// System.out.println("i: " + i + ", j: " + j + ", tempChessPiece: " + tempChessPiece);
				if (currentPiece.getAllegiance() == Allegiance.BLACK) {
					String currentPosition = Utilities.getPositionByRowCol(i, j, numOfRows);
					Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

					for (String nextPosition : nextPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(
								currentPosition,
								nextPosition,
								false
						);

						int blackKingRow = Utilities.getRowFromPosition(
								initialChessBoard.getBlackKingPosition(),
								numOfRows
						);
						int blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());

						// If any move exists without getting the Black king in check,
						// then there still are legal moves, and we do not have a stalemate scenario.
						boolean legalMovesExist =
								initialChessBoard.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn] == 0;

						initialChessBoard = new ChessBoard(this);

						if (legalMovesExist) {
							isBlackStalemateDraw = false;
							i = j = 1000000;
							break;
						}

					}
				}
			}
		}

		if (isBlackStalemateDraw) {
			this.gameResult = GameResult.BLACK_STALEMATE_DRAW;
		}

		return isBlackStalemateDraw;
	}

	// Checks if there is insufficient mating material left on the chess board.
	public boolean checkForInsufficientMaterialDraw() {
		boolean whiteHasInsufficientMaterial = isLoneKing(Allegiance.WHITE)
				|| isLoneKingPlusOneOrTwoKnights(Allegiance.WHITE)
				|| isLoneKingPlusABishop(Allegiance.WHITE);

		boolean blackHasInsufficientMaterial = isLoneKing(Allegiance.BLACK)
				|| isLoneKingPlusOneOrTwoKnights(Allegiance.BLACK)
				|| isLoneKingPlusABishop(Allegiance.BLACK);

		boolean isInsufficientMaterialDraw = whiteHasInsufficientMaterial && blackHasInsufficientMaterial;

		if (isInsufficientMaterialDraw) {
			this.gameResult = GameResult.INSUFFICIENT_MATERIAL_DRAW;
		}

		return isInsufficientMaterialDraw;
	}

	// Checks if only a king has remained on the board, on the given player's side.
	public boolean isLoneKing(Allegiance playerAllegiance) {
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = getGameBoard()[i][j];
				if (!(chessPiece instanceof EmptySquare)
						&& !(chessPiece instanceof King)
						&& playerAllegiance == chessPiece.getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + chessPiece);
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
				ChessPiece chessPiece = getGameBoard()[i][j];
				if (!(chessPiece instanceof EmptySquare)
						&& !(chessPiece instanceof King)
						&& !(chessPiece instanceof Pawn)
						&& playerAllegiance == chessPiece.getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + chessPiece);
					return false;
				}
				if (chessPiece instanceof Pawn) {
					numOfPawns++;
				}
			}
		}

		return numOfPawns >= 3;
	}

	// Checks if only a king and one or two knights have remained on the board, on the given player's side.
	public boolean isLoneKingPlusOneOrTwoKnights(Allegiance playerAllegiance) {
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = getGameBoard()[i][j];
				if (!(chessPiece instanceof EmptySquare)
						&& !(chessPiece instanceof King || chessPiece instanceof Knight)
						&& playerAllegiance == chessPiece.getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + chessPiece);
					return false;
				}
			}
		}

		return true;
	}

	// Checks if only a king and one bishop have remained on the board, on the given player's side.
	public boolean isLoneKingPlusABishop(Allegiance playerAllegiance) {
		int numOfBishops = countBishops(playerAllegiance);
		if (numOfBishops != 1) return false;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = getGameBoard()[i][j];
				if (!(chessPiece instanceof EmptySquare)
						&& !(chessPiece instanceof King || chessPiece instanceof Bishop)
						&& playerAllegiance == chessPiece.getAllegiance()) {
					// System.out.println("i: " + i + ", j: " + j + ", chessPiece: " + chessPiece);
					return false;
				}
			}
		}

		return true;
	}

	public boolean isWhiteQueenSideCastlingAvailable() {
		return !whiteKingMoved && !leftWhiteRookMoved;
	}

	public boolean isWhiteKingSideCastlingAvailable() {
		return !whiteKingMoved && !rightWhiteRookMoved;
	}

	public boolean isBlackQueenSideCastlingAvailable() {
		return !blackKingMoved && !leftBlackRookMoved;
	}

	public boolean isBlackKingSideCastlingAvailable() {
		return !blackKingMoved && !rightBlackRookMoved;
	}

	public boolean whitePlays() {
		return this.player;
	}

	public boolean blackPlays() {
		return !this.player;
	}

	public boolean getNextPlayer() {
		return !player;
	}

	public boolean checkForNoPieceCaptureDraw() {
		return this.halfMoveClock >= Constants.NO_CAPTURE_DRAW_HALF_MOVES_LIMIT;
	}

	public void incrementCapturedPiecesCounter(ChessPiece chessPiece) {
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			whiteCapturedPiecesCounter++;
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			blackCapturedPiecesCounter++;
		}
	}

	@Override
	public String toString() {
		return getChessBoardString(this.gameBoard);
	}

}
