package com.chriskormaris.mychessgame.api.chess_board;

import com.chriskormaris.mychessgame.api.ai.MinimaxAI;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameResult;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.EmptySquare;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.BFS;
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
	 * if no chessPiece has been captured in 50 full-moves (100 half-moves). */
	private int halfMoveClock;

	/* 1 full-move corresponds to 2 half-moves. */
	private int halfMoveNumber;

	private boolean whiteKingInCheck;
	private boolean blackKingInCheck;

	private Map<String, Set<String>> whiteKingInCheckValidMoves;
	private Map<String, Set<String>> blackKingInCheckValidMoves;

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

		this.whiteKingInCheckValidMoves = new HashMap<>();
		this.blackKingInCheckValidMoves = new HashMap<>();

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

		this.whiteKingInCheckValidMoves = new HashMap<>(otherBoard.getWhiteKingInCheckValidMoves());
		this.blackKingInCheckValidMoves = new HashMap<>(otherBoard.getBlackKingInCheckValidMoves());

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
				output.append(" ").append(chessBoard[i][j].getPieceChar()).append(" |");
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

		int rowStart = getRowFromPosition(positionStart);
		int columnStart = getColumnFromPosition(positionStart);
		ChessPiece chessPiece = this.gameBoard[rowStart][columnStart];
		// if (displayMove) {
		// 	System.out.println("rowStart: " + rowStart + ", columnStart: " + columnStart);
		// 	System.out.println("chessPiece:" + chessPiece);
		// }

		int rowEnd = getRowFromPosition(positionEnd);
		int columnEnd = getColumnFromPosition(positionEnd);
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
						!(getChessPieceFromPosition("A1") instanceof Rook))) {
					this.setLeftWhiteRookMoved(true);
					this.setWhiteCastlingDone(false);
				} else if (!this.isRightWhiteRookMoved() && (positionStart.equals("H1") ||
						!(getChessPieceFromPosition("H1") instanceof Rook))) {
					this.setRightWhiteRookMoved(true);
					this.setWhiteCastlingDone(false);
				} else if (!this.isLeftBlackRookMoved() && (positionStart.equals("A" + numOfRows) ||
						!(getChessPieceFromPosition("A" + numOfRows) instanceof Rook))) {
					this.setLeftBlackRookMoved(true);
					this.setBlackCastlingDone(false);
				} else if (!this.isRightBlackRookMoved() && (positionStart.equals("H" + numOfRows) ||
						!(getChessPieceFromPosition("H" + numOfRows) instanceof Rook))) {
					this.setRightBlackRookMoved(true);
					this.setBlackCastlingDone(false);
				}
			}

			// Implementation of "en passant" functionality and "pawn promotion" here.
			if (chessPiece instanceof Pawn) {

				/* En passant implementation */

				// Remove the captured en passant pieces.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd + 1 < numOfRows) {

					String twoStepsForwardBlackPawnPosition = getPositionByRowCol(rowEnd + 1, columnEnd);
					// if (displayMove)
					// System.out.println("twoStepsForwardBlackPawnPosition: " + twoStepsForwardBlackPawnPosition);
					int twoStepsForwardBlackPawnPositionRow = getRowFromPosition(twoStepsForwardBlackPawnPosition);
					int twoStepsForwardBlackPawnPositionColumn = getColumnFromPosition(twoStepsForwardBlackPawnPosition);

					// White pawn captures black pawn.
					ChessPiece possibleBlackEnPassantCapturedPawn = this.gameBoard[rowEnd + 1][columnEnd];
					if (possibleBlackEnPassantCapturedPawn instanceof Pawn
							&& possibleBlackEnPassantCapturedPawn.getAllegiance() == Allegiance.BLACK
							&& this.enPassantPosition.equals(
							getPositionByRowCol(rowEnd, columnEnd))) {

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

					String twoStepsForwardWhitePawnPosition = getPositionByRowCol(rowEnd - 1, columnEnd);
					int twoStepsForwardWhitePawnPositionRow = getRowFromPosition(twoStepsForwardWhitePawnPosition);
					int twoStepsForwardWhitePawnPositionColumn = getColumnFromPosition(twoStepsForwardWhitePawnPosition);

					// Black pawn captures white pawn.
					ChessPiece possibleWhiteEnPassantCapturedPawn = this.gameBoard[rowEnd - 1][columnEnd];
					if (possibleWhiteEnPassantCapturedPawn instanceof Pawn
							&& possibleWhiteEnPassantCapturedPawn.getAllegiance() == Allegiance.WHITE
							&& this.enPassantPosition.equals(getPositionByRowCol(rowEnd, columnEnd))) {

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
					this.enPassantPosition = getPositionByRowCol(rowStart - 1, columnStart);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == rowStart + 2) {
					this.enPassantPosition = getPositionByRowCol(rowStart + 1, columnStart);
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
			if (endSquare.getAllegiance() == Allegiance.NONE) {
				halfMoveClock++;
			} else {  // a capture has occurred
				halfMoveClock = 0;
			}

			// If a chessPiece capture has occurred.
			if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)) {
				updateScore(endSquare);

				if (!displayMove) {
					incrementCapturedPiecesCounter(endSquare);
				}
			}
		}
	}

	public void updateScore(ChessPiece chessPiece) {
		if (chessPiece.isPromoted()) {
			if (chessPiece.getAllegiance() == Allegiance.WHITE) {
				score -= Constants.PAWN_SCORE_VALUE;
			} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				score += Constants.PAWN_SCORE_VALUE;
			}
		} else if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			if (chessPiece instanceof Pawn) {
				score -= Constants.PAWN_SCORE_VALUE;
			} else if (chessPiece instanceof Rook) {
				score -= Constants.ROOK_SCORE_VALUE;
			} else if (chessPiece instanceof Knight) {
				score -= Constants.KNIGHT_SCORE_VALUE;
			} else if (chessPiece instanceof Bishop) {
				score -= Constants.BISHOP_SCORE_VALUE;
			} else if (chessPiece instanceof Queen) {
				score -= Constants.QUEEN_SCORE_VALUE;
			}
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			if (chessPiece instanceof Pawn) {
				score += Constants.PAWN_SCORE_VALUE;
			} else if (chessPiece instanceof Rook) {
				score += Constants.ROOK_SCORE_VALUE;
			} else if (chessPiece instanceof Knight) {
				score += Constants.KNIGHT_SCORE_VALUE;
			} else if (chessPiece instanceof Bishop) {
				score += Constants.BISHOP_SCORE_VALUE;
			} else if (chessPiece instanceof Queen) {
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

		int rowEnd = getRowFromPosition(positionEnd);
		int columnEnd = chessBoard.getColumnFromPosition(positionEnd);

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
	public List<ChessBoard> getChildren(Allegiance allegiance, MinimaxAI minimaxAI) {
		List<ChessBoard> children = new ArrayList<>();

		// int childPlayer = (getLastPlayer() == Constants.WHITE) ? Constants.BLACK : Constants.BLACK;

		// int i = 0;
		// System.out.println("**********************************************");
		for (int row = 0; row < numOfRows; row++) {
			for (int column = 0; column < NUM_OF_COLUMNS; column++) {
				ChessPiece chessPiece = this.gameBoard[row][column];
				if (allegiance == chessPiece.getAllegiance()) {
					String initialPosition = getPositionByRowCol(row, column);
					Set<String> nextPositions = new HashSet<>();
					if (!this.whiteKingInCheck && whitePlays() || !this.blackKingInCheck && blackPlays()) {
						nextPositions = getNextPositions(initialPosition);
					} else if (this.whiteKingInCheck && this.player) {
						nextPositions = this.whiteKingInCheckValidMoves.get(initialPosition);
					} else if (this.blackKingInCheck && !this.player) {
						nextPositions = this.blackKingInCheckValidMoves.get(initialPosition);
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
							child.getLastMove().setValue(child.evaluate(minimaxAI));
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
	public double evaluate(MinimaxAI minimaxAI) {
		if (checkForWhiteCheckmate(false)) return Constants.CHECKMATE_VALUE;
		if (checkForBlackCheckmate(false)) return -Constants.CHECKMATE_VALUE;
		if (checkForWhiteStalemateDraw()) return 0;
		if (checkForBlackStalemateDraw()) return 0;
		if (checkForInsufficientMatingMaterialDraw()) return 0;
		if (checkForNoCaptureDraw(50)) return 0;

		return minimaxAI.getEvaluation().evaluate(this);
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

		if (checkForInsufficientMatingMaterialDraw()) return true;

		return checkForNoCaptureDraw(50);
	}

	public boolean isTerminalState() {
		return this.gameResult != GameResult.NONE;
	}

	public Set<String> getNextPositions(String position) {
		Set<String> nextPositions;

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = getRowFromPosition(position);
		int column = getColumnFromPosition(position);
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

			int whiteKingRow = getRowFromPosition(initialChessBoard.getWhiteKingPosition());
			int whiteKingColumn = getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

			int blackKingRow = getRowFromPosition(initialChessBoard.getBlackKingPosition());
			int blackKingColumn = getColumnFromPosition(initialChessBoard.getBlackKingPosition());

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
				String position = getPositionByRowCol(i, j);

				Set<String> threatPositions;

				threatPositions = chessPiece.getNextPositions(position, this, true);

				// System.out.println("position: " + position + ", threatPositions: " + threatPositions);

				if (threatPositions != null && threatPositions.size() != 0) {
					for (String threatPosition : threatPositions) {
						int row = getRowFromPosition(threatPosition);
						int column = getColumnFromPosition(threatPosition);
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

		int blackKingRow = getRowFromPosition(this.getBlackKingPosition());
		int blackKingColumn = getColumnFromPosition(this.getBlackKingPosition());
		int blackKingThreatened = getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn];

		if (storeKingInCheckMoves) {
			this.blackKingInCheckValidMoves = new HashMap<>();
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
						String currentPosition = getPositionByRowCol(i, j);
						Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

						Set<String> validBlackKingInCheckTempNextPosition = new HashSet<>();
						for (String nextPosition : nextPositions) {
							initialChessBoard.movePieceFromAPositionToAnother(
									currentPosition,
									nextPosition,
									false
							);

							blackKingRow = getRowFromPosition(initialChessBoard.getBlackKingPosition());
							blackKingColumn = getColumnFromPosition(initialChessBoard.getBlackKingPosition());

							if (initialChessBoard.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn] == 0) {
								blackKingThreatened = 0;
								validBlackKingInCheckTempNextPosition.add(nextPosition);
							}

							initialChessBoard = new ChessBoard(this);
						}
						if (storeKingInCheckMoves && validBlackKingInCheckTempNextPosition.size() > 0) {
							this.blackKingInCheckValidMoves.put(
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
				this.blackKingInCheckValidMoves = new HashMap<>();
			}
		}

		return isWhiteCheckmate;
	}

	// Check for Black checkmate (if Black wins!)
	public boolean checkForBlackCheckmate(boolean storeKingInCheckMoves) {
		boolean isBlackCheckmate = false;

		ChessBoard initialChessBoard = new ChessBoard(this);

		int whiteKingRow = getRowFromPosition(this.getWhiteKingPosition());
		int whiteKingColumn = getColumnFromPosition(this.getWhiteKingPosition());
		int whiteKingThreatened = getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn];

		if (storeKingInCheckMoves) {
			this.whiteKingInCheckValidMoves = new HashMap<>();
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
						String currentPosition = getPositionByRowCol(i, j);
						Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

						Set<String> validWhiteKingInCheckTempNextPositions = new HashSet<>();
						for (String nextPosition : nextPositions) {
							initialChessBoard.movePieceFromAPositionToAnother(
									currentPosition,
									nextPosition,
									false
							);

							whiteKingRow = getRowFromPosition(initialChessBoard.getWhiteKingPosition());
							whiteKingColumn = getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

							if (initialChessBoard.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 0) {
								whiteKingThreatened = 0;
								validWhiteKingInCheckTempNextPositions.add(nextPosition);
							}

							initialChessBoard = new ChessBoard(this);
						}
						if (storeKingInCheckMoves && validWhiteKingInCheckTempNextPositions.size() > 0) {
							this.whiteKingInCheckValidMoves.put(
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
				this.whiteKingInCheckValidMoves = new HashMap<>();
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
					String currentPosition = getPositionByRowCol(i, j);
					Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

					for (String nextPosition : nextPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(
								currentPosition,
								nextPosition,
								false
						);

						int whiteKingRow = getRowFromPosition(initialChessBoard.getWhiteKingPosition());
						int whiteKingColumn = getColumnFromPosition(initialChessBoard.getWhiteKingPosition());

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
					String currentPosition = getPositionByRowCol(i, j);
					Set<String> nextPositions = initialChessBoard.getNextPositions(currentPosition);

					for (String nextPosition : nextPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(
								currentPosition,
								nextPosition,
								false
						);

						int blackKingRow = getRowFromPosition(initialChessBoard.getBlackKingPosition());
						int blackKingColumn = getColumnFromPosition(initialChessBoard.getBlackKingPosition());

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
	public boolean checkForInsufficientMatingMaterialDraw() {
		boolean isInsufficientMatingMaterialDraw = isInsufficientMatingMaterialDraw();

		if (!isInsufficientMatingMaterialDraw && checkForBlockedKingAndPawnsDraw()) {
			isInsufficientMatingMaterialDraw = true;
		}

		if (isInsufficientMatingMaterialDraw) {
			this.gameResult = GameResult.INSUFFICIENT_MATERIAL_DRAW;
		}

		return isInsufficientMatingMaterialDraw;
	}

	/* Checks if the only pieces left on the Chess board are:
	 * King Vs King
	 * King & Bishop Vs King
	 * King & Knight Vs King
	 * King & Bishop Vs King & Bishop with the Bishops on the same color. */
	public boolean isInsufficientMatingMaterialDraw() {
		int numOfWhiteKnights = 0;
		int numOfBlackKnights = 0;
		int numOfWhiteBishops = 0;
		int numOfBlackBishops = 0;
		int numOfWhiteBishopsInWhiteSquare = 0;
		int numOfWhiteBishopsInBlackSquare = 0;
		int numOfBlackBishopsInWhiteSquare = 0;
		int numOfBlackBishopsInBlackSquare = 0;

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = getGameBoard()[i][j];
				if (chessPiece instanceof Pawn) {
					return false;
				} else if (chessPiece instanceof Knight) {
					if (Allegiance.WHITE == chessPiece.getAllegiance()) {
						numOfWhiteKnights++;
						if (numOfWhiteKnights > 1) {
							return false;
						}
					} else if (Allegiance.BLACK == chessPiece.getAllegiance()) {
						numOfBlackKnights++;
						if (numOfBlackKnights > 1) {
							return false;
						}
					}
				} else if (chessPiece instanceof Bishop) {
					if (Allegiance.WHITE == chessPiece.getAllegiance()) {
						numOfWhiteBishops++;
						if (numOfWhiteBishops > 1) {
							return false;
						}
						if ((i + j) % 2 == 0) {
							numOfWhiteBishopsInWhiteSquare++;
						} else {
							numOfWhiteBishopsInBlackSquare++;
						}
					} else if (Allegiance.BLACK == chessPiece.getAllegiance()) {
						numOfBlackBishops++;
						if (numOfBlackBishops > 1) {
							return false;
						}
						if ((i + j) % 2 == 0) {
							numOfBlackBishopsInWhiteSquare++;
						} else {
							numOfBlackBishopsInBlackSquare++;
						}
					}
				} else if (chessPiece instanceof Rook) {
					return false;
				} else if (chessPiece instanceof Queen) {
					return false;
				}
			}
		}

		// Note, that if we have reached this far, all the helper variables are going to have a value of at most 1.

		boolean kingVsKing = numOfWhiteKnights + numOfBlackKnights + numOfWhiteBishops + numOfBlackBishops == 0;
		boolean kingAndBishopVsKing = numOfWhiteKnights + numOfBlackKnights == 0
				&& numOfWhiteBishops + numOfBlackBishops == 1;
		boolean kingAndKnightVsKing = numOfWhiteBishops + numOfBlackBishops == 0
				&& numOfWhiteKnights + numOfBlackKnights == 1;
		boolean kingAndBishopVsKingAndBishopSameColor = numOfWhiteKnights + numOfBlackKnights == 0 &&
				(numOfWhiteBishopsInWhiteSquare + numOfBlackBishopsInWhiteSquare == 2
						|| numOfWhiteBishopsInBlackSquare + numOfBlackBishopsInBlackSquare == 2);

		return kingVsKing || kingAndBishopVsKing || kingAndKnightVsKing || kingAndBishopVsKingAndBishopSameColor;
	}

	// Check for a special case of draw.
	// It occurs when no pieces other than the kings can be moved
	// and neither king can capture any enemy pieces.
	// It usually happens when only Kings and Pawns are left on the Chess board.
	public boolean checkForBlockedKingAndPawnsDraw() {
		// Check if any pieces other than the Kings can make any move.
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (!(gameBoard[i][j] instanceof King || gameBoard[i][j] instanceof EmptySquare)) {
					String position = getPositionByRowCol(i, j);
					Set<String> nextPositions = getNextPositions(position);
					if (nextPositions.size() > 0) {
						return false;
					}
				}
			}
		}

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				if (!(gameBoard[i][j] instanceof King || gameBoard[i][j] instanceof EmptySquare)) {
					String endingPosition = getPositionByRowCol(i, j);
					String opposingKingPosition;
					if (gameBoard[i][j].getAllegiance() == Allegiance.BLACK) {
						opposingKingPosition = whiteKingPosition;
					} else {
						opposingKingPosition = blackKingPosition;
					}
					ChessPiece opposingKing = getChessPieceFromPosition(opposingKingPosition);
					boolean canGoToPosition = BFS.canGoToPosition(
							this,
							opposingKing,
							opposingKingPosition,
							endingPosition,
							Constants.BLOCKED_KING_AND_PAWNS_DRAW_MAX_BFS_DEPTH
					);
					if (canGoToPosition) {
						return false;
					}
				}
			}
		}

		return true;
	}

	// The Queen and the Rook are considered as major pieces.
	// The Knight and the Bishop are considered as minor pieces.
	// The endgame begins if each player has no Queens
	// or he has one major piece maximum and one minor piece maximum.
	public boolean isEndGame() {
		int numOfWhiteRooks = 0;
		int numOfBlackRooks = 0;
		int numOfWhiteQueens = 0;
		int numOfBlackQueens = 0;
		int numOfWhiteMinorPieces = 0;
		int numOfBlackMinorPieces = 0;
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = getGameBoard()[i][j];
				if (chessPiece instanceof Knight || chessPiece instanceof Bishop) {
					if (Allegiance.WHITE == chessPiece.getAllegiance()) {
						numOfWhiteMinorPieces++;
					} else if (Allegiance.BLACK == chessPiece.getAllegiance()) {
						numOfBlackMinorPieces++;
					}
				} else if (chessPiece instanceof Rook) {
					if (Allegiance.WHITE == chessPiece.getAllegiance()) {
						numOfWhiteRooks++;
					} else if (Allegiance.BLACK == chessPiece.getAllegiance()) {
						numOfBlackRooks++;
					}
				} else if (chessPiece instanceof Queen) {
					if (Allegiance.WHITE == chessPiece.getAllegiance()) {
						numOfWhiteQueens++;
						if (numOfWhiteQueens > 1) {
							return false;
						}
					} else if (Allegiance.BLACK == chessPiece.getAllegiance()) {
						numOfBlackQueens++;
						if (numOfBlackQueens > 1) {
							return false;
						}
					}
				}
			}
		}
		int numOfWhiteMajorPieces = numOfWhiteQueens + numOfWhiteRooks;
		int numOfBlackMajorPieces = numOfBlackQueens + numOfBlackRooks;
		boolean isWhiteEndGame = numOfWhiteQueens == 0 || numOfWhiteMajorPieces <= 1 && numOfWhiteMinorPieces <= 1;
		boolean isBlackEndGame = numOfBlackQueens == 0 || numOfBlackMajorPieces <= 1 && numOfBlackMinorPieces <= 1;
		return isWhiteEndGame && isBlackEndGame;
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

	public boolean checkForNoCaptureDraw(int numOfFullMoves) {
		return this.halfMoveClock >= numOfFullMoves * 2;
	}

	public void incrementCapturedPiecesCounter(ChessPiece chessPiece) {
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			whiteCapturedPiecesCounter++;
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			blackCapturedPiecesCounter++;
		}
	}

	public String getPositionByRowCol(int row, int column) {
		String columnString = (char) (column + 65) + "";
		String rowString = (numOfRows - row) + "";

		return columnString + rowString;
	}

	// ALTERNATIVE
	/*
	public String getPositionByRowCol(int row, int column) {
		return Constants.CHESS_POSITIONS[row][column];
	}
	*/

	public int getRowFromPosition(String position) {
		// examples:
		// A8, column = 0, row = 0
		// A2, column = 0, row = 6
		// A1, column = 0, row = 7
		return numOfRows - Character.getNumericValue(position.charAt(1));
	}

	public int getColumnFromPosition(String position) {
		// examples:
		// A1, column = 0, row = 7
		// B1, column = 1, row = 7
		return (int) position.charAt(0) - (int) 'A';
	}

	public ChessPiece getChessPieceFromPosition(String position) {
		int row = getRowFromPosition(position);
		int column = getColumnFromPosition(position);
		return gameBoard[row][column];
	}

	@Override
	public String toString() {
		return getChessBoardString(this.gameBoard);
	}

}
