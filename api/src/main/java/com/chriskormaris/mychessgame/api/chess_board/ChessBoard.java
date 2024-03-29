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
import com.chriskormaris.mychessgame.api.util.FenUtils;
import com.chriskormaris.mychessgame.api.util.Utilities;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


@Getter
@Setter
public class ChessBoard {

	private final int numOfRows;
	private final int numOfColumns;

	// Immediate move that led to this board.
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
	private boolean[][] squaresThreatenedByWhite;

	/* A board with:
	 * 1 for areas threatened by black pieces.
	 * 0 for areas not threatened by black pieces. */
	private boolean[][] squaresThreatenedByBlack;

	// These are used to define if a checkmate has occurred.
	private String whiteKingPosition;
	private String blackKingPosition;

	/* The player who plays next. True is for White. False is for Black.
	 * The player who control the white pieces plays first. */
	private boolean player;

	// These variables are used to handle the "castling" implementation.
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

	// 1 full-move corresponds to 2 half-moves.
	private int halfMoveNumber;

	private boolean whiteKingInCheck;
	private boolean blackKingInCheck;

	private GameResult gameResult;

	// These variables should be used on the GUI implementation.
	private Set<String> positionsToRemove;
	private Map<String, ChessPiece> piecesToPlace;

	private ChessPiece capturedEnPassantPiece;

	// This stack of "String" objects is used to check for a threefold repetition of the current Chess board position.
	private Stack<String> previousHalfMoveFenPositions;

	public ChessBoard() {
		this(Constants.DEFAULT_NUM_OF_ROWS);
	}

	public ChessBoard(int numOfRows) {
		this.numOfRows = numOfRows;
		this.numOfColumns = Constants.DEFAULT_NUM_OF_COLUMNS;

		this.lastMove = new Move();

		this.gameBoard = new ChessPiece[numOfRows][numOfColumns];
		placePiecesToStartingPositions();

		// FenUtils.populateGameBoard(this, Constants.DEFAULT_STARTING_PIECES);

		this.squaresThreatenedByWhite = new boolean[numOfRows][numOfColumns];
		this.squaresThreatenedByBlack = new boolean[numOfRows][numOfColumns];

		this.whiteKingPosition = "E1";
		this.blackKingPosition = "E" + numOfRows;

		this.player = Constants.WHITE;  // White plays first.

		this.enPassantPosition = "-";

		this.halfMoveClock = 0;
		this.halfMoveNumber = 1;

		this.gameResult = GameResult.NONE;

		this.positionsToRemove = new HashSet<>();
		this.piecesToPlace = new HashMap<>();
		this.capturedEnPassantPiece = new EmptySquare();

		this.previousHalfMoveFenPositions = new Stack<>();

		setThreats();
	}


	// Copy constructor
	public ChessBoard(ChessBoard otherBoard) {
		this.numOfRows = otherBoard.getNumOfRows();
		this.numOfColumns = otherBoard.getNumOfColumns();

		this.lastMove = new Move(otherBoard.getLastMove());

		this.gameBoard = Utilities.copyGameBoard(otherBoard.getGameBoard());
		this.squaresThreatenedByWhite = otherBoard.getSquaresThreatenedByWhite().clone();
		this.squaresThreatenedByBlack = otherBoard.getSquaresThreatenedByBlack().clone();

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

		this.gameResult = otherBoard.getGameResult();

		this.positionsToRemove = new HashSet<>(otherBoard.getPositionsToRemove());
		this.piecesToPlace = new HashMap<>(otherBoard.getPiecesToPlace());
		this.capturedEnPassantPiece = otherBoard.getCapturedEnPassantPiece();

		this.previousHalfMoveFenPositions = (Stack<String>) otherBoard.getPreviousHalfMoveFenPositions().clone();
	}

	public void placePiecesToStartingPositions() {
		// 1st row
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

		// 2nd row
		for (int j = 0; j < numOfColumns; j++) {
			this.gameBoard[1][j] = new Pawn(Allegiance.BLACK);
		}

		// From 3rd row to (n-th - 2) row.
		for (int i = 2; i < numOfRows - 2; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				this.gameBoard[i][j] = new EmptySquare();
			}
		}

		// (n-th - 1) row
		for (int j = 0; j < numOfColumns; j++) {
			this.gameBoard[numOfRows - 2][j] = new Pawn(Allegiance.WHITE);
		}

		// n-th row
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

	// Make a move; it moves a Chess piece on the board.
	public void makeMove(Move move, boolean displayMove) {
		String positionStart = move.getPositionStart();
		String positionEnd = move.getPositionEnd();

		makeMove(positionStart, positionEnd, displayMove);

		this.lastMove = new Move(move);
	}

	// The parameter "displayMove" should be set to true,
	// when we make a move on the actual GUI board.
	// If the method is called while running the Minimax AI algorithm,
	// then the parameter "displayMove" should be set to false.
	public void makeMove(String positionStart, String positionEnd, boolean displayMove) {
		int rowStart = getRowFromPosition(positionStart);
		int columnStart = getColumnFromPosition(positionStart);
		ChessPiece chessPiece = this.gameBoard[rowStart][columnStart];

		int rowEnd = getRowFromPosition(positionEnd);
		int columnEnd = getColumnFromPosition(positionEnd);
		ChessPiece endSquare = this.gameBoard[rowEnd][columnEnd];

		if (!positionsToRemove.isEmpty()) {
			positionsToRemove.clear();
		}
		if (!piecesToPlace.isEmpty()) {
			piecesToPlace.clear();
		}
		if (!(capturedEnPassantPiece instanceof EmptySquare)) {
			capturedEnPassantPiece = new EmptySquare();
		}

		// Allow only valid moves, for all the chess board pieces.
		// Move only if the square is empty or the square contains an opponent chessPiece.
		// Also allow castling, en passant and promotion moves.
		if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()) {
			previousHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(this));

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
					setWhiteKingMoved(true);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					setBlackKingPosition(positionEnd);
					setBlackKingMoved(true);
				}

				/* Castling implementation */

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
					int twoStepsForwardBlackPawnPositionRow = getRowFromPosition(twoStepsForwardBlackPawnPosition);
					int twoStepsForwardBlackPawnPositionColumn = getColumnFromPosition(twoStepsForwardBlackPawnPosition);

					ChessPiece possibleBlackEnPassantCapturedPawn = this.gameBoard[rowEnd + 1][columnEnd];

					// White pawn captures black pawn.
					if (possibleBlackEnPassantCapturedPawn instanceof Pawn
							&& possibleBlackEnPassantCapturedPawn.getAllegiance() == Allegiance.BLACK
							&& this.enPassantPosition.equals(getPositionByRowCol(rowEnd, columnEnd))) {

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


				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd - 1 >= 0) {
					String twoStepsForwardWhitePawnPosition = getPositionByRowCol(rowEnd - 1, columnEnd);
					int twoStepsForwardWhitePawnPositionRow = getRowFromPosition(twoStepsForwardWhitePawnPosition);
					int twoStepsForwardWhitePawnPositionColumn = getColumnFromPosition(twoStepsForwardWhitePawnPosition);

					ChessPiece possibleWhiteEnPassantCapturedPawn = this.gameBoard[rowEnd - 1][columnEnd];

					// Black pawn captures white pawn.
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

			// Increase the halfMoveClock if no capture has occurred and no Pawn has been moved.
			if (!(chessPiece instanceof Pawn) && endSquare.getAllegiance() == Allegiance.NONE) {
				halfMoveClock++;
			}
			// If a capture has occurred.
			else {
				halfMoveClock = 0;
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

		int rowEnd = getRowFromPosition(positionEnd);
		int columnEnd = chessBoard.getColumnFromPosition(positionEnd);

		chessBoard.getGameBoard()[rowEnd][columnEnd] = knight;
		chessBoard.setThreats();

		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			chessBoard.checkForWhiteCheckmate();
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			chessBoard.checkForBlackCheckmate();
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
	 * Some special cases include "en passant" and castling. */
	public List<ChessBoard> getChildren(Allegiance allegiance, MinimaxAI minimaxAI) {
		List<ChessBoard> children = new ArrayList<>();

		for (int row = 0; row < numOfRows; row++) {
			for (int column = 0; column < numOfColumns; column++) {
				ChessPiece chessPiece = this.gameBoard[row][column];
				if (allegiance == chessPiece.getAllegiance()) {
					String startingPosition = getPositionByRowCol(row, column);
					Set<String> nextPositions = getNextPositions(startingPosition);

					for (String nextPosition : nextPositions) {
						ChessBoard child = new ChessBoard(this);

						Move move = new Move(startingPosition, nextPosition);

						child.makeMove(move, false);

						// Increase halfMove.
						child.setHalfMoveNumber(child.getHalfMoveNumber() + 1);
						child.setPlayer(child.getNextPlayer());

						child.getLastMove().setValue(child.evaluate(minimaxAI));
						children.add(child);
					}
				}
			}
		}

		return children;
	}

	/* Evaluation Function. 4 options:
	 * 1) Simplified
	 * 2) PeSTO's
	 * 3) Wukong
	 * 4) Shannon's */
	public double evaluate(MinimaxAI minimaxAI) {
		if (checkForWhiteCheckmate()) return Constants.CHECKMATE_VALUE;
		if (checkForBlackCheckmate()) return -Constants.CHECKMATE_VALUE;
		if (checkForWhiteStalemateDraw()) return 0;
		if (checkForBlackStalemateDraw()) return 0;
		if (checkForInsufficientMatingMaterialDraw()) return 0;
		if (checkForConditionalNoCaptureDraw()) return 0;
		if (checkForThreefoldRepetitionDraw()) return 0;

		return minimaxAI.getEvaluation().evaluate(this);
	}

	/* A state is terminal if there is a win or draw condition.
	 * A state is terminal if it's unconditional, without for example having to claim a draw. */
	public boolean checkForTerminalState() {
		setThreats();

		if (checkForWhiteCheckmate()) return true;

		if (checkForBlackCheckmate()) return true;

		// Check for White stalemate, only if the last player was Black,
		// meaning that the next player should be White.
		if (whitePlays() && checkForWhiteStalemateDraw()) return true;

		// Check for Black stalemate, only if the last player was White,
		// meaning that the next player should be Black.
		if (blackPlays() && checkForBlackStalemateDraw()) return true;

		if (checkForInsufficientMatingMaterialDraw()) return true;

		if (checkForUnconditionalNoCaptureDraw()) return true;

		checkForThreefoldRepetitionDraw();
		return isTerminalState();
	}

	public boolean isTerminalState() {
		return this.gameResult != GameResult.NONE;
	}

	public Set<String> getNextPositions(String startingPosition) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = getRowFromPosition(startingPosition);
		int column = getColumnFromPosition(startingPosition);
		ChessPiece chessPiece = this.getGameBoard()[row][column];

		setThreats();

		Set<String> nextPositions = gameBoard[row][column].getNextPositions(startingPosition, this, false);

		removePositionsLeadingToOppositeCheck(startingPosition, chessPiece, nextPositions);

		return nextPositions;
	}

	// Remove positions that lead to the King being in check.
	public void removePositionsLeadingToOppositeCheck(
			String startingPosition,
			ChessPiece chessPiece,
			Set<String> nextPositions
	) {
		int whiteKingRow = getRowFromPosition(this.getWhiteKingPosition());
		int whiteKingColumn = getColumnFromPosition(this.getWhiteKingPosition());

		int blackKingRow = getRowFromPosition(this.getBlackKingPosition());
		int blackKingColumn = getColumnFromPosition(this.getBlackKingPosition());

		if (whiteKingRow < 0 || whiteKingRow >= numOfRows
				|| whiteKingColumn < 0 || whiteKingColumn >= numOfColumns
				|| blackKingRow < 0 || blackKingRow >= numOfRows
				|| blackKingColumn < 0 || blackKingColumn >= numOfColumns) {
			return;
		}

		Set<String> positionsToRemove = new HashSet<>();
		for (String nextPosition : nextPositions) {
			ChessBoard nextPositionChessBoard = new ChessBoard(this);

			nextPositionChessBoard.makeMove(startingPosition, nextPosition, false);

			whiteKingRow = getRowFromPosition(nextPositionChessBoard.getWhiteKingPosition());
			whiteKingColumn = getColumnFromPosition(nextPositionChessBoard.getWhiteKingPosition());

			blackKingRow = getRowFromPosition(nextPositionChessBoard.getBlackKingPosition());
			blackKingColumn = getColumnFromPosition(nextPositionChessBoard.getBlackKingPosition());

			if (chessPiece.getAllegiance() == Allegiance.WHITE
					&& nextPositionChessBoard.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn]
					|| chessPiece.getAllegiance() == Allegiance.BLACK
					&& nextPositionChessBoard.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn]) {
				positionsToRemove.add(nextPosition);
			}
		}
		nextPositions.removeAll(positionsToRemove);
	}

	// It should be called after we move any ChessPiece in the ChessBoard.
	// For many ChessPiece moves at once we only need to call this method once in the end
	// (for example when calling the initial state of the chessBoard).
	public void setThreats() {
		// First, remove all the threatened areas.
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				this.squaresThreatenedByWhite[i][j] = false;
				this.squaresThreatenedByBlack[i][j] = false;
			}
		}

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				ChessPiece chessPiece = this.gameBoard[i][j];
				String position = getPositionByRowCol(i, j);

				Set<String> threatPositions = chessPiece.getNextPositions(position, this, true);

				for (String threatPosition : threatPositions) {
					int row = getRowFromPosition(threatPosition);
					int column = getColumnFromPosition(threatPosition);
					if (chessPiece.getAllegiance() == Allegiance.WHITE) {
						this.squaresThreatenedByWhite[row][column] = true;
					} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
						this.squaresThreatenedByBlack[row][column] = true;
					}
				}
			}
		}
	}

	// Check for White checkmate (if White wins!)
	public boolean checkForWhiteCheckmate() {
		int blackKingRow = getRowFromPosition(this.getBlackKingPosition());
		int blackKingColumn = getColumnFromPosition(this.getBlackKingPosition());

		if (blackKingRow < 0 || blackKingRow >= numOfRows || blackKingColumn < 0 || blackKingColumn >= numOfColumns) {
			return false;
		}

		boolean blackKingThreatened = getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn];
		if (blackKingThreatened) {
			this.blackKingInCheck = true;

			// Check for all possible moves made by Black,
			// that can get the Black king out of a possible check
			// and store them in a variable called "blackKingInCheckValidPieceMoves".
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < numOfColumns; j++) {
					ChessBoard nextPositionChessBoard = new ChessBoard(this);
					ChessPiece currentPiece = nextPositionChessBoard.getGameBoard()[i][j];
					if (currentPiece.getAllegiance() == Allegiance.BLACK) {
						String currentPosition = getPositionByRowCol(i, j);
						Set<String> nextPositions = nextPositionChessBoard.getNextPositions(currentPosition);

						for (String nextPosition : nextPositions) {
							nextPositionChessBoard.makeMove(
									currentPosition,
									nextPosition,
									false
							);

							blackKingRow = getRowFromPosition(nextPositionChessBoard.getBlackKingPosition());
							blackKingColumn = getColumnFromPosition(nextPositionChessBoard.getBlackKingPosition());

							if (!nextPositionChessBoard.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn]) {
								return false;
							}
						}
					}
				}
			}

			this.gameResult = GameResult.WHITE_CHECKMATE;
			return true;
		} else {
			this.blackKingInCheck = false;
			return false;
		}
	}

	// Check for Black checkmate (if Black wins!)
	public boolean checkForBlackCheckmate() {
		int whiteKingRow = getRowFromPosition(this.getWhiteKingPosition());
		int whiteKingColumn = getColumnFromPosition(this.getWhiteKingPosition());

		if (whiteKingRow < 0 || whiteKingRow >= numOfRows || whiteKingColumn < 0 || whiteKingColumn >= numOfColumns) {
			return false;
		}

		boolean whiteKingThreatened = getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn];
		if (whiteKingThreatened) {
			this.whiteKingInCheck = true;

			// Check for all possible moves made by White,
			// that can get the White king out of a possible check
			// and store them in a variable called "whiteKingInCheckValidPieceMoves".
			for (int i = 0; i < numOfRows; i++) {
				for (int j = 0; j < numOfColumns; j++) {
					ChessBoard nextPositionChessBoard = new ChessBoard(this);
					ChessPiece currentPiece = nextPositionChessBoard.getGameBoard()[i][j];
					if (currentPiece.getAllegiance() == Allegiance.WHITE) {
						String currentPosition = getPositionByRowCol(i, j);
						Set<String> nextPositions = nextPositionChessBoard.getNextPositions(currentPosition);

						for (String nextPosition : nextPositions) {
							nextPositionChessBoard.makeMove(
									currentPosition,
									nextPosition,
									false
							);

							whiteKingRow = getRowFromPosition(nextPositionChessBoard.getWhiteKingPosition());
							whiteKingColumn = getColumnFromPosition(nextPositionChessBoard.getWhiteKingPosition());

							if (!nextPositionChessBoard.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn]) {
								return false;
							}
						}
					}
				}
			}

			this.gameResult = GameResult.BLACK_CHECKMATE;
			return true;
		} else {
			this.whiteKingInCheck = false;
			return false;
		}
	}

	// It checks for a stalemate. It gets called after the opposing player, makes a move.
	// A stalemate occurs when a player has no legal moves to make. Then, the game ends in a draw.
	// If the Black player makes a move, then we check for a White player stalemate and vice-versa.
	public boolean checkForWhiteStalemateDraw() {
		if (whiteKingInCheck) return false;

		int whiteKingRow = getRowFromPosition(this.getWhiteKingPosition());
		int whiteKingColumn = getColumnFromPosition(this.getWhiteKingPosition());

		if (whiteKingRow < 0 || whiteKingRow >= numOfRows || whiteKingColumn < 0 || whiteKingColumn >= numOfColumns) {
			return false;
		}

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				ChessBoard nextPositionChessBoard = new ChessBoard(this);
				ChessPiece currentPiece = nextPositionChessBoard.getGameBoard()[i][j];
				if (currentPiece.getAllegiance() == Allegiance.WHITE) {
					String currentPosition = getPositionByRowCol(i, j);
					Set<String> nextPositions = nextPositionChessBoard.getNextPositions(currentPosition);

					for (String nextPosition : nextPositions) {
						nextPositionChessBoard.makeMove(
								currentPosition,
								nextPosition,
								false
						);

						whiteKingRow = getRowFromPosition(nextPositionChessBoard.getWhiteKingPosition());
						whiteKingColumn = getColumnFromPosition(nextPositionChessBoard.getWhiteKingPosition());

						// If any move exists without getting the White king in check,
						// then there still are legal moves, and we do not have a stalemate scenario.
						boolean legalMovesExist = !nextPositionChessBoard
								.getSquaresThreatenedByBlack()[whiteKingRow][whiteKingColumn];

						if (legalMovesExist) {
							return false;
						}
					}
				}
			}
		}

		this.gameResult = GameResult.WHITE_STALEMATE_DRAW;
		return true;
	}

	// It checks for a stalemate. It gets called after the opposing player, makes a move.
	// A stalemate occurs when a player has no legal moves to make. Then, the game ends in a draw.
	// If the White player makes a move, then we check for a Black player stalemate and vice-versa.
	public boolean checkForBlackStalemateDraw() {
		if (blackKingInCheck) return false;

		int blackKingRow = getRowFromPosition(this.getBlackKingPosition());
		int blackKingColumn = getColumnFromPosition(this.getBlackKingPosition());

		if (blackKingRow < 0 || blackKingRow >= numOfRows || blackKingColumn < 0 || blackKingColumn >= numOfColumns) {
			return false;
		}

		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				ChessBoard nextPositionChessBoard = new ChessBoard(this);
				ChessPiece currentPiece = nextPositionChessBoard.getGameBoard()[i][j];
				if (currentPiece.getAllegiance() == Allegiance.BLACK) {
					String currentPosition = getPositionByRowCol(i, j);
					Set<String> nextPositions = nextPositionChessBoard.getNextPositions(currentPosition);

					for (String nextPosition : nextPositions) {
						nextPositionChessBoard.makeMove(
								currentPosition,
								nextPosition,
								false
						);

						blackKingRow = getRowFromPosition(nextPositionChessBoard.getBlackKingPosition());
						blackKingColumn = getColumnFromPosition(nextPositionChessBoard.getBlackKingPosition());

						// If any move exists without getting the Black king in check,
						// then there still are legal moves, and we do not have a stalemate scenario.
						boolean legalMovesExist = !nextPositionChessBoard
								.getSquaresThreatenedByWhite()[blackKingRow][blackKingColumn];

						if (legalMovesExist) {
							return false;
						}
					}
				}
			}
		}

		this.gameResult = GameResult.BLACK_STALEMATE_DRAW;
		return true;
	}

	// Checks if there is insufficient mating material left on the chess board.
	public boolean checkForInsufficientMatingMaterialDraw() {
		if (isInsufficientMatingMaterialDraw() || checkForBlockedKingsDraw()) {
			this.gameResult = GameResult.INSUFFICIENT_MATERIAL_DRAW;
			return true;
		}

		return false;
	}

	/* Checks if the only pieces left on the Chess board are:
	 * - King vs King
	 * - King & Bishop vs King
	 * - King & Knight vs King
	 * - King & Bishop vs King & Bishop with the Bishops on the same color. */
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
			for (int j = 0; j < numOfColumns; j++) {
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

	/* Check for a special case of draw.
	 * It occurs when no pieces other than the Kings can be moved
	 * and neither King can capture any enemy pieces.
	 * It usually happens when only Kings and Pawns are left on the Chess board. */
	private boolean checkForBlockedKingsDraw() {
		int whiteKingRow = getRowFromPosition(whiteKingPosition);
		int whiteKingColumn = getColumnFromPosition(whiteKingPosition);
		int blackKingRow = getRowFromPosition(blackKingPosition);
		int blackKingColumn = getColumnFromPosition(blackKingPosition);

		if (whiteKingRow < 0 || whiteKingRow >= numOfRows
				|| whiteKingColumn < 0 || whiteKingColumn >= numOfColumns
				|| blackKingRow < 0 || blackKingRow >= numOfRows
				|| blackKingColumn < 0 || blackKingColumn >= numOfColumns) {
			return false;
		}

		// Remove the Kings from the board and check if any pieces can make any move.
		ChessBoard chessBoardWithoutKings = new ChessBoard(this);
		chessBoardWithoutKings.getGameBoard()[whiteKingRow][whiteKingColumn] = new EmptySquare();
		chessBoardWithoutKings.setWhiteKingPosition("Z0");
		chessBoardWithoutKings.getGameBoard()[blackKingRow][blackKingColumn] = new EmptySquare();
		chessBoardWithoutKings.setBlackKingPosition("Z0");
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				if (!(chessBoardWithoutKings.getGameBoard()[i][j] instanceof EmptySquare)) {
					String position = getPositionByRowCol(i, j);
					Set<String> nextPositions = chessBoardWithoutKings.getNextPositions(position);
					if (!nextPositions.isEmpty()) {
						return false;
					}
				}
			}
		}

		// Check if any King can capture any opponent's pieces, in any number of moves.
		// If they can, then it's not a draw.
		ChessBoard chessBoardWithoutWhiteKing = new ChessBoard(this);
		chessBoardWithoutWhiteKing.getGameBoard()[whiteKingRow][whiteKingColumn] = new EmptySquare();
		chessBoardWithoutWhiteKing.setWhiteKingPosition("Z0");

		ChessBoard chessBoardWithoutBlackKing = new ChessBoard(this);
		chessBoardWithoutBlackKing.getGameBoard()[blackKingRow][blackKingColumn] = new EmptySquare();
		chessBoardWithoutBlackKing.setBlackKingPosition("Z0");
		for (int i = 0; i < numOfRows; i++) {
			for (int j = 0; j < numOfColumns; j++) {
				if (!(gameBoard[i][j] instanceof King || gameBoard[i][j] instanceof EmptySquare)) {
					String endingPosition = getPositionByRowCol(i, j);
					String opposingKingPosition;
					ChessBoard chessBoardWithOneKing;
					if (gameBoard[i][j].getAllegiance() == Allegiance.WHITE) {
						opposingKingPosition = blackKingPosition;
						chessBoardWithOneKing = chessBoardWithoutWhiteKing;
					} else {
						opposingKingPosition = whiteKingPosition;
						chessBoardWithOneKing = chessBoardWithoutBlackKing;
					}
					ChessPiece opposingKing = getChessPieceFromPosition(opposingKingPosition);
					boolean canGoToPosition = BFS.canGoToPosition(
							chessBoardWithOneKing,
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
			for (int j = 0; j < numOfColumns; j++) {
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
		boolean isWhiteEndgame = numOfWhiteQueens == 0 || numOfWhiteMajorPieces <= 1 && numOfWhiteMinorPieces <= 1;
		boolean isBlackEndgame = numOfBlackQueens == 0 || numOfBlackMajorPieces <= 1 && numOfBlackMinorPieces <= 1;
		return isWhiteEndgame && isBlackEndgame;
	}

	// We are comparing FEN positions, but without checking the half-move clock and the full-move number.
	public boolean checkForThreefoldRepetitionDraw() {
		int numOfRepeats = 1;
		if (!previousHalfMoveFenPositions.isEmpty()) {
			String currentHalfMoveFenPosition = FenUtils.getFenPositionFromChessBoard(this);
			currentHalfMoveFenPosition = FenUtils.skipCounters(currentHalfMoveFenPosition);
			int N = previousHalfMoveFenPositions.size();
			for (int i = N - 1; i >= 0; i--) {
				// Skip the last 2 iterations, if the number of repeats is 1.
				// and there are less 2 or fewer iterations left.
				if (!(numOfRepeats == 1 && i < 2)) {
					String otherHalfMoveFenPosition = previousHalfMoveFenPositions.get(i);
					otherHalfMoveFenPosition = FenUtils.skipCounters(otherHalfMoveFenPosition);
					if (currentHalfMoveFenPosition.equals(otherHalfMoveFenPosition)) {
						numOfRepeats++;
						if (numOfRepeats == 5) {
							setGameResult(GameResult.FIVEFOLD_REPETITION_DRAW);
							return true;
						}
					}
				}
			}
		}

		return numOfRepeats >= 3;
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

	public boolean checkForConditionalNoCaptureDraw() {
		return this.halfMoveClock >= Constants.CONDITIONAL_NO_CAPTURE_DRAW_MOVES_LIMIT * 2;
	}

	public boolean checkForUnconditionalNoCaptureDraw() {
		if (this.halfMoveClock >= Constants.UNCONDITIONAL_NO_CAPTURE_DRAW_MOVES_LIMIT * 2) {
			setGameResult(GameResult.NO_CAPTURE_DRAW);
		}
		return this.gameResult == GameResult.NO_CAPTURE_DRAW;
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

	private String getGameBoardAsString() {
		StringBuilder output = new StringBuilder();
		output.append("    A   B   C   D   E   F   G   H").append("\n");
		output.append("   -------------------------------").append("\n");
		for (int i = 0; i < numOfRows; i++) {
			output.append(numOfRows - i).append(" |");
			for (int j = 0; j < numOfColumns; j++) {
				output.append(" ").append(gameBoard[i][j].getPieceChar()).append(" |");
			}
			output.append(" ").append(numOfRows - i).append("\n");
		}
		output.append("   -------------------------------").append("\n");
		output.append("    A   B   C   D   E   F   G   H").append("\n");

		return output.toString();
	}

	@Override
	public String toString() {
		return getGameBoardAsString();
	}

}
