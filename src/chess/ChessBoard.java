package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import chess_gui.ChessGUI;
import enumerations.Allegiance;
import enumerations.GameMode;
import pieces.Bishop;
import pieces.ChessPiece;
import pieces.EmptyTile;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;
import utilities.ChessPieceShortestPath;
import utilities.Constants;
import utilities.Utilities;


public class ChessBoard {

	private final int numOfRows;
	private final static int NUM_OF_COLUMNS = Constants.DEFAULT_NUM_OF_COLUMNS;

    /* Immediate move that led to this board. */
    private Move lastMove;
    
    
    /*
     * The ChessBoard's gameBoard:
     * 
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
	 * 
	 * E.g: A1 = (0,0), H8 = (7,7), B3 = (2,1), C2 = (1,2) etc.
     */
	private ChessPiece[][] gameBoard;

	/* A board with:
	 * 1 for areas threatened by white pieces.
	 * 0 for areas not threatened by white pieces.
	 */ 
	private int[][] tilesThreatenedByWhite;

	/* A board with:
	 * 1 for areas threatened by black pieces.
	 * 0 for areas not threatened by black pieces.
	 */
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
	 * this variable is equal to "-". 
	 */
	private String enPassantPosition;
	
	/* This variable is used to determine a draw, 
	 * if no chessPiece has been captured in 50 fullmoves (100 halfmoves). */
	private int halfmoveClock;

	/* 1 fullmove corresponds to 2 halfmoves. */
	private int halfmoveNumber;
	
	private boolean whiteKingInCheck;
	private boolean blackKingInCheck;
	private Map<String, Set<String>> whiteKingInCheckValidPieceMoves;
	private Map<String, Set<String>> blackKingInCheckValidPieceMoves;
	
	/* These variables are also used for the "undo" functionality. */
	private boolean isWhiteCheckmate;
	private boolean isBlackCheckmate;
	private boolean isInsufficientMaterialDraw;
	private boolean isWhiteStalemateDraw;
	private boolean isBlackStalemateDraw;
	
	private int whiteCapturedPiecesCounter;
	private int blackCapturedPiecesCounter;
	
	private int score;
	private double lastCapturedPieceValue;
	private Set<ChessPiece> promotedPieces;
	
	
    public ChessBoard() {
		this.numOfRows = ChessGUI.gameParameters.numOfRows;

    	this.lastMove = new Move();

    	int n1 = numOfRows;
    	int n2 = NUM_OF_COLUMNS;
    	this.gameBoard = new ChessPiece[n1][n2];
        for (int i=0; i<n1; i++) {
            for (int j=0; j<n2; j++) {
            	this.gameBoard[i][j] = new EmptyTile();
            }
		}
		
    	// String fenStartingPieces = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    	// this.gameBoard = FenUtilities.createGameBoard(this, fenStartingPieces);
    	
		this.tilesThreatenedByWhite = new int[numOfRows][NUM_OF_COLUMNS];
		this.tilesThreatenedByBlack = new int[numOfRows][NUM_OF_COLUMNS];
		
		this.whiteKingPosition = "E1";
		this.blackKingPosition = "E" + ChessGUI.gameParameters.numOfRows;
		
		this.player = Constants.WHITE;  // White plays first.
		
		this.enPassantPosition = "-";

		this.halfmoveClock = 0;
		this.halfmoveNumber = 1;
		
		this.whiteCapturedPiecesCounter = 0;
		this.blackCapturedPiecesCounter = 0;
		
		this.score = 0;
		this.lastCapturedPieceValue = 0;
		this.promotedPieces = new HashSet<ChessPiece>();
		
		this.whiteKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>();
		this.blackKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>();
		
		setThreats();
		
	}
    
    
    // Copy constructor
    public ChessBoard(ChessBoard otherBoard) {
		this.numOfRows = otherBoard.numOfRows;

		this.lastMove = new Move(otherBoard.lastMove);
		
		int n1 = otherBoard.getGameBoard().length;
		int n2 = otherBoard.getGameBoard()[0].length;
		this.gameBoard = new ChessPiece[n1][n2];
        for (int i=0; i<n1; i++) {
            for (int j=0; j<n2; j++) {
            	this.gameBoard[i][j] = otherBoard.getGameBoard()[i][j];
            }
		}
        
		n1 = otherBoard.getTilesThreatenedByWhite().length;
		n2 = otherBoard.getTilesThreatenedByWhite()[0].length;
		this.tilesThreatenedByWhite = new int[n1][n2];
        for (int i=0; i<n1; i++) {
            for (int j=0; j<n2; j++) {
            	this.tilesThreatenedByWhite[i][j] = otherBoard.getTilesThreatenedByWhite()[i][j];
            }
		}
        
		n1 = otherBoard.getTilesThreatenedByBlack().length;
		n2 = otherBoard.getTilesThreatenedByBlack()[0].length;
		this.tilesThreatenedByBlack = new int[n1][n2];
        for (int i=0; i<n1; i++) {
            for (int j=0; j<n2; j++) {
            	this.tilesThreatenedByBlack[i][j] = otherBoard.getTilesThreatenedByBlack()[i][j];
            }
		}

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

		this.halfmoveClock = otherBoard.getHalfmoveClock();
		this.halfmoveNumber = otherBoard.getHalfmoveNumber();
		
		this.whiteKingInCheck = otherBoard.isWhiteKingInCheck();
		this.blackKingInCheck = otherBoard.isBlackKingInCheck();
		
		this.whiteKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>(otherBoard.whiteKingInCheckValidPieceMoves);
		this.blackKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>(otherBoard.blackKingInCheckValidPieceMoves);
		
		this.isWhiteCheckmate = otherBoard.isWhiteCheckmate();
		this.isBlackCheckmate = otherBoard.isBlackCheckmate();
		this.isInsufficientMaterialDraw = otherBoard.isInsufficientMaterialDraw();
		this.isWhiteStalemateDraw = otherBoard.isWhiteStalemateDraw();
		this.isBlackStalemateDraw = otherBoard.isBlackStalemateDraw();
		
		this.whiteCapturedPiecesCounter = otherBoard.getWhiteCapturedPiecesCounter();
		this.blackCapturedPiecesCounter = otherBoard.getBlackCapturedPiecesCounter();
		
		this.score = otherBoard.getScore();
		this.lastCapturedPieceValue = otherBoard.getLastCapturedPieceValue();
		this.promotedPieces = new HashSet<>(otherBoard.getPromotedPieces());
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
    
    
    // The parameter "displayMove" should be set to true when we
 	// make the move on the actual board. If the method is called
 	// while searching for a checkmate, then the parameter "displayMove" should be set to false.
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

 		// Allow only valid moves, for all the chess pieces.
 		// Move only if the tile is empty or the tile contains an opponent chessPiece.
 		// Also allow castling, en passant and promotion moves.
 		// System.out.println("hintPositions: " + hintPositions);
 		if (endTile instanceof EmptyTile || 
 				chessPiece.getAllegiance() != endTile.getAllegiance()) {
 			
			this.lastCapturedPieceValue = Utilities.getChessPieceValue(endTile, this.halfmoveNumber);
 			
			if (chessPiece.getAllegiance() == Allegiance.BLACK)
				this.lastCapturedPieceValue = -this.lastCapturedPieceValue;	
			
 			Set<String> castlingPositions = null;
 			if (chessPiece instanceof King) {
 				castlingPositions = King.getCastlingPositions(positionStart, this);
 			}
 			
 			if (displayMove) {
 				ChessGUI.removePieceFromPosition(positionStart);
 				ChessGUI.placePieceToPosition(positionEnd, chessPiece);
 			} else {
 				this.gameBoard[rowStart][columnStart] = new EmptyTile();
 				this.gameBoard[rowEnd][columnEnd] = chessPiece;
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
 				if (castlingPositions != null && castlingPositions.contains(positionEnd)) {
 					// White queenside castling
 					if (positionEnd.equals("C1")) {
 						// Move the left white rook to the correct position.
 						if (displayMove) {
 							ChessGUI.removePieceFromPosition("A1");
 							ChessGUI.placePieceToPosition("D1", new Rook(Allegiance.WHITE));
 						} else {
 							this.gameBoard[0][0] = new EmptyTile();
 							this.gameBoard[0][3] = new Rook(Allegiance.WHITE);
 						}
 						setWhiteKingMoved(true);
 						setLeftWhiteRookMoved(true);
 						setWhiteCastlingDone(true);
 					}
 					// White kingside castling
 					else if (positionEnd.equals("G1")) {
 						// Move the right white rook to the correct position.
 						if (displayMove) {
 							ChessGUI.removePieceFromPosition("H1");
 							ChessGUI.placePieceToPosition("F1", new Rook(Allegiance.WHITE));
 						} else {
 							this.gameBoard[0][7] = new EmptyTile();
 							this.gameBoard[0][5] = new Rook(Allegiance.WHITE);
 						}
 						setWhiteKingMoved(true);
 						setRightWhiteRookMoved(true);
 						setWhiteCastlingDone(true);
 					}
 					// Black queenside castling
 					else if (positionEnd.equals("C" + this.numOfRows)) {
 						// Move the left black rook to the correct position.
 						if (displayMove) {
 							ChessGUI.removePieceFromPosition("A" + this.numOfRows);
 							ChessGUI.placePieceToPosition("D" + this.numOfRows, new Rook(Allegiance.BLACK));
 						} else {
 							this.gameBoard[this.numOfRows-1][0] = new EmptyTile();
 							this.gameBoard[this.numOfRows-1][3] = new Rook(Allegiance.BLACK);
 						}
 						setBlackKingMoved(true);
 						setLeftBlackRookMoved(true);
 						setBlackCastlingDone(true);
 					}
 					// Black kingside castling
 					else if (positionEnd.equals("G" + this.numOfRows)) {
 						// Move the right black rook to the correct position.
 						if (displayMove) {
 							ChessGUI.removePieceFromPosition("H" + this.numOfRows);
 							ChessGUI.placePieceToPosition("F" + this.numOfRows, new Rook(Allegiance.BLACK));
 						} else {
 							this.gameBoard[this.numOfRows-1][7] = new EmptyTile();
 							this.gameBoard[this.numOfRows-1][5] = new Rook(Allegiance.BLACK);
 						}
 						setBlackKingMoved(true);
 						setRightBlackRookMoved(true);
 						setBlackCastlingDone(true);
 					}
 				}
 				
 			}
 			
 			else if (chessPiece instanceof Rook) {
 				if (!this.isLeftWhiteRookMoved() &&
 						(positionStart.equals("A1") || 
 							!(Utilities.getChessPieceFromPosition(this.gameBoard, "A1") instanceof Rook))) {
 					this.setLeftWhiteRookMoved(true);
					this.setWhiteCastlingDone(false);
 				} else if (!this.isRightWhiteRookMoved() && 
 						(positionStart.equals("H1") ||
 							!(Utilities.getChessPieceFromPosition(this.gameBoard, "H1") instanceof Rook))) {
 					this.setRightWhiteRookMoved(true);
					this.setWhiteCastlingDone(false);
 				} else if (!this.isLeftBlackRookMoved() 
 						&& (positionStart.equals("A" + numOfRows) || 
 							!(Utilities.getChessPieceFromPosition(this.gameBoard, "A" + numOfRows) instanceof Rook))) {
 					this.setLeftBlackRookMoved(true);
					this.setBlackCastlingDone(false);
 				} else if (!this.isRightBlackRookMoved() && 
 						(positionStart.equals("H" + numOfRows) || 
 							!(Utilities.getChessPieceFromPosition(this.gameBoard, "H" + numOfRows) instanceof Rook))) {
 					this.setRightBlackRookMoved(true);
					this.setBlackCastlingDone(false);
 				}
 			}
 			
 			// Implementation of "en passant" functionality and "pawn promotion" here.
 			if (chessPiece instanceof Pawn) {
 				
 				/* En passant implementation */
 				
 				// Remove the captured en passant pieces.
 				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
 	
 					String twoStepsForwardBlackPawnPosition = Utilities.getPositionByRowCol(rowEnd-1, columnEnd);
 					// if (displayMove)
 						// System.out.println("twoStepsForwardBlackPawnPosition: " + twoStepsForwardBlackPawnPosition);
 					int twoStepsForwardBlackPawnPositionRow = Utilities.getRowFromPosition(twoStepsForwardBlackPawnPosition);
 					int twoStepsForwardBlackPawnPositionColumn = Utilities.getColumnFromPosition(twoStepsForwardBlackPawnPosition);
 					
 					// White pawn captures black pawn.
 					if (rowEnd-1 >= 0 
 							&& this.gameBoard[rowEnd-1][columnEnd] instanceof Pawn
 							&& this.gameBoard[rowEnd-1][columnEnd].getAllegiance() == Allegiance.BLACK
 						&& this.enPassantPosition.equals(Utilities.getPositionByRowCol(rowEnd, columnEnd))) {
 						
 						if (displayMove) {
 							ChessGUI.removePieceFromPosition(twoStepsForwardBlackPawnPosition);

 	 						// Add chessPiece to capturedPiecesPanel.
 			 				ImageIcon pieceImage = ChessGUI.preparePieceIcon(Constants.BLACK_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 		 					ChessGUI.capturedPiecesImages[31 - blackCapturedPiecesCounter - 1].setIcon(pieceImage);
 		 					this.blackCapturedPiecesCounter++;
 						} else {
 							this.gameBoard[twoStepsForwardBlackPawnPositionRow][twoStepsForwardBlackPawnPositionColumn] = new EmptyTile();
 						}
 						this.enPassantPosition = "-";
 					}
 					
 				// Black pawn captures white pawn.
 				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
 					
 					String twoStepsForwardWhitePawnPosition = Utilities.getPositionByRowCol(rowEnd+1, columnEnd);
 					int twoStepsForwardWhitePawnPositionRow = Utilities.getRowFromPosition(twoStepsForwardWhitePawnPosition);
 					int twoStepsForwardWhitePawnPositionColumn = Utilities.getColumnFromPosition(twoStepsForwardWhitePawnPosition);
 					
 					if (rowEnd+1 < numOfRows 
 							&& this.gameBoard[rowEnd+1][columnEnd] instanceof Pawn
							&& this.gameBoard[rowEnd+1][columnEnd].getAllegiance() == Allegiance.WHITE
 						&& this.enPassantPosition.equals(Utilities.getPositionByRowCol(rowEnd, columnEnd))) {
 						
 						if (displayMove) {
 							ChessGUI.removePieceFromPosition(twoStepsForwardWhitePawnPosition);
 							
 							// Add chessPiece to capturedPiecesPanel.
 			 				ImageIcon pieceImage = ChessGUI.preparePieceIcon(Constants.WHITE_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 		 					ChessGUI.capturedPiecesImages[whiteCapturedPiecesCounter].setIcon(pieceImage);
 		 					this.whiteCapturedPiecesCounter++;
 						} else {
 							this.gameBoard[twoStepsForwardWhitePawnPositionRow][twoStepsForwardWhitePawnPositionColumn] = new EmptyTile();
 						}
 						this.enPassantPosition = "-";
 					}
 	 				
 				}
				
 				// Save the two step forward moves as one step forward move.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd == rowStart + 2) {
 					this.enPassantPosition = Utilities.getPositionByRowCol(rowStart+1, columnStart);
 				} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == rowStart - 2) {
 					this.enPassantPosition = Utilities.getPositionByRowCol(rowStart-1, columnStart);
 				} else {
 					this.enPassantPosition = "-";
 				}
	 				
 				/* Pawn promotion implementation */
				
				if (chessPiece.getAllegiance() == Allegiance.WHITE && rowEnd == this.numOfRows -1
					|| chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == 0) {

 					ChessPiece queen = null;
 					ChessPiece rook = null;
 					ChessPiece bishop = null;
 					ChessPiece knight = null;
 					
 					if (chessPiece.getAllegiance() == Allegiance.WHITE) {
 						queen = new Queen(Allegiance.WHITE);
 						rook = new Rook(Allegiance.WHITE);
 						bishop = new Bishop(Allegiance.WHITE);
 						knight = new Knight(Allegiance.WHITE);
 					} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
 						queen = new Queen(Allegiance.BLACK);
 						rook = new Rook(Allegiance.BLACK);
 						bishop = new Bishop(Allegiance.BLACK);
 						knight = new Knight(Allegiance.BLACK);
 					}
					
					// If AI plays, choose the best promotion piece,
					// based on the outcome of the immediately next move. 
	 				if (displayMove 
						&& 
						((ChessGUI.gameParameters.gameMode == GameMode.HUMAN_VS_AI &&
						(!this.player && ChessGUI.gameParameters.humanPlayerAllegiance == Allegiance.WHITE)
						|| (this.player && ChessGUI.gameParameters.humanPlayerAllegiance == Allegiance.BLACK))
						|| ChessGUI.gameParameters.gameMode == GameMode.AI_VS_AI)) {
 					
						ChessPiece[] promotionChessPieces = {queen, rook, bishop, knight};
	
						// System.out.println("Printing Knight promotion board (before)...");
						// System.out.println(this);
						
						this.getGameBoard()[rowEnd][columnEnd] = knight;
	 					this.setThreats();
	
						// System.out.println("Printing Knight promotion board (after)...");
						// System.out.println(this);
						
						// System.out.println("Checking for Knight checkmate...");
						if (chessPiece.getAllegiance() == Allegiance.WHITE) {
							this.checkForWhiteCheckmate(false);
							// System.out.println("this.isWhiteCheckmate: " + this.isWhiteCheckmate);
	 					} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
	 						this.checkForBlackCheckmate(false);
	 						// System.out.println("this.isBlackCheckmate: " + this.isBlackCheckmate);
	 					}
	
						if (chessPiece.getAllegiance() == Allegiance.WHITE && this.isWhiteCheckmate()
							|| 
							chessPiece.getAllegiance() == Allegiance.BLACK && this.isBlackCheckmate()) {
							ChessGUI.placePieceToPosition(positionEnd, knight);
	 						promotedPieces.add(knight);
						} else {
							for (ChessPiece promotionChessPiece : promotionChessPieces) {
								this.getGameBoard()[rowEnd][columnEnd] = promotionChessPiece;
								this.setThreats();
		 						
		 						if (chessPiece.getAllegiance() == Allegiance.WHITE 
		 								&& !this.checkForBlackStalemateDraw()
		 							|| 
		 							chessPiece.getAllegiance() == Allegiance.BLACK 
		 								&& !this.checkForWhiteStalemateDraw()) {
		 							ChessGUI.placePieceToPosition(positionEnd, promotionChessPiece);
	 								promotedPieces.add(promotionChessPiece);
		 	 						break;
		 						}
							}
						}
						
	 				}
	 				// Select which promotion you want and display it on the GUI.
	 				else if (displayMove) {
	 					
	 					String[] promotionPieces = {"Queen", "Rook", "Bishop", "Knight"};
	 				    String initialSelection = "Queen";
	 				    
	 					if (chessPiece.getAllegiance() == Allegiance.WHITE  && rowEnd == numOfRows - 1) {
	 						String value = (String) JOptionPane.showInputDialog(ChessGUI.gui, "Promote white pawn to:",
	 						        "White pawn promotion", JOptionPane.QUESTION_MESSAGE, null, promotionPieces, initialSelection);
	 						// System.out.println("value: " + value);
	 						
	 						if (value == null || value.equals("Queen")) {
	 							ChessGUI.placePieceToPosition(positionEnd, queen);
	 							promotedPieces.add(queen);
	 						} else if (value.equals("Rook")) {
	 							ChessGUI.placePieceToPosition(positionEnd, rook);
	 							promotedPieces.add(rook);
	 						} else if (value.equals("Bishop")) {
	 							ChessGUI.placePieceToPosition(positionEnd, bishop);
	 							promotedPieces.add(bishop);
	 						} else if (value.equals("Knight")) {
	 							ChessGUI.placePieceToPosition(positionEnd, knight);
	 							promotedPieces.add(knight);
	 						}
	 					} else if (chessPiece.getAllegiance() == Allegiance.BLACK && rowEnd == 0) {
	 						String value = (String) JOptionPane.showInputDialog(ChessGUI.gui, "Promote black pawn to:",
	 						        "Black pawn promotion", JOptionPane.QUESTION_MESSAGE, null, promotionPieces, initialSelection);
	 						// System.out.println("value: " + value);
	 						
	 						if (value == null || value.equals("Queen")) {
	 							ChessGUI.placePieceToPosition(positionEnd, queen);
	 							promotedPieces.add(queen);
	 						} else if (value.equals("Rook")) {
	 							ChessGUI.placePieceToPosition(positionEnd, rook);
	 							promotedPieces.add(rook);
	 						} else if (value.equals("Bishop")) {
	 							ChessGUI.placePieceToPosition(positionEnd, bishop);
	 							promotedPieces.add(bishop);
	 						} else if (value.equals("Knight")) {
	 							ChessGUI.placePieceToPosition(positionEnd, knight);
	 							promotedPieces.add(knight);
	 						}
	 					}
	 				
	 				}
	 				
		 			// Automatically choose promotion to Queen and do NOT display it on the GUI. 
					else {
						this.gameBoard[rowEnd][columnEnd] = queen;
						promotedPieces.add(queen);
					}
 				
				}
 				
 			} else {
				this.enPassantPosition = "-";
 			}
 			
 			setThreats();
 			
 			// Increase the halfmoveClock if no capture has occurred.
 	 		if (endTile.getAllegiance() == Allegiance.EMPTY) {
 	 			halfmoveClock++;
 	 		} else {  // a capture has occurred
 	 			halfmoveClock = 0;
 	 		}
	 	 		
 	 		// If a chessPiece capture has occurred.
 	 		if (chessPiece.getAllegiance() != endTile.getAllegiance()
 	 				&& !(endTile instanceof EmptyTile)) {
 	 			
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
 	 			
 	 			if (displayMove) {
 	 				ChessGUI.configureCapturedPiecesImages(endTile);
 	 			}

				if (endTile.getAllegiance() == Allegiance.WHITE) {
					this.whiteCapturedPiecesCounter++;
				} else if (endTile.getAllegiance() == Allegiance.BLACK) {
					this.blackCapturedPiecesCounter++;
				}
				
 	 		}
 	 		
 		}
 		
 	}
 	
 	
    // Checks whether a given tile (with given row and column) contains a chessPiece of the given player.
    // White pieces have positive values. Black pieces have negative values.
    private boolean rowColContainsPlayerPiece(int row, int column, boolean player) {
		if (player == Constants.WHITE && gameBoard[row][column].getAllegiance() == Allegiance.WHITE
			|| player == Constants.BLACK && gameBoard[row][column].getAllegiance() == Allegiance.BLACK) {
			return true;
		}
		return false;
	}
    
    /* Generates the children of the state
     * Any square in the board that is empty, 
     * or is an opponent chessPiece, results to a child.
     * Some special cases include "en passant" and castling.
     */
    public List<ChessBoard> getChildren(boolean player) {
        List<ChessBoard> children = new ArrayList<ChessBoard>();
        
        // int childPlayer = (getLastPlayer() == Constants.WHITE) ? Constants.BLACK : Constants.BLACK;
        
		// int i = 0;
        // System.out.println("**********************************************");
		for (int row=0; row<numOfRows; row++) {
			for (int column=0; column<NUM_OF_COLUMNS; column++) {
				if (rowColContainsPlayerPiece(row, column, player)) {
					String initialPosition = Utilities.getPositionByRowCol(row, column);
					Set<String> nextPositions = new TreeSet<String>();
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
				        for (String nextPosition: nextPositions) {
				        	// System.out.println(initialPosition + ": " + nextPositions);
							ChessBoard child = new ChessBoard(this);
							List<String> moves = new ArrayList<String>();
		                    moves.add(initialPosition);
		                    moves.add(nextPosition);
		                    // Move move = new Move(moves, evaluate());
		                    Move move = new Move(moves);
		                    
		                    // move.setValue(this.evaluate());
		                    		                    
		                    child.makeMove(move, false);
		                    this.player = !this.player;
		                    
		                    // System.out.println("**********************************************");
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
    	
		for (int row=0; row<numOfRows; row++) {
			for (int column=0; column<NUM_OF_COLUMNS; column++) {
				if (rowColContainsPlayerPiece(row, column, player)) {
					String initialPosition = Utilities.getPositionByRowCol(row, column);
					Set<String> nextPositions = new TreeSet<String>();
					if (!this.blackKingInCheck && !this.player
						|| !this.whiteKingInCheck && this.player) {
						nextPositions = getNextPositions(initialPosition);
					} else if (this.blackKingInCheck && !this.player) {
						nextPositions = this.blackKingInCheckValidPieceMoves.get(initialPosition);
					} else if (this.whiteKingInCheck && this.player) {
						nextPositions = this.whiteKingInCheckValidPieceMoves.get(initialPosition);
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
			Set<String> nextPositions = new TreeSet<String>();
			if (!this.blackKingInCheck && !this.player
				|| !this.whiteKingInCheck && this.player) {
				nextPositions = getNextPositions(initialPosition);
			} else if (this.blackKingInCheck && !this.player) {
				nextPositions = this.blackKingInCheckValidPieceMoves.get(initialPosition);
			} else if (this.whiteKingInCheck && this.player) {
				nextPositions = this.whiteKingInCheckValidPieceMoves.get(initialPosition);
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
    	// System.out.println("EVALUATE!");
    	int whiteScore = 0;
		int blackScore = 0;
		
		this.isWhiteCheckmate = checkForWhiteCheckmate(false);
		this.isBlackCheckmate = checkForBlackCheckmate(false);
		this.isInsufficientMaterialDraw = checkForInsufficientMaterialDraw();
		this.isWhiteStalemateDraw = checkForWhiteStalemateDraw();
		this.isBlackStalemateDraw = checkForBlackStalemateDraw();
		
		if (this.isWhiteCheckmate) return Constants.CHECKMATE_VALUE;
    	if (this.isBlackCheckmate) return -Constants.CHECKMATE_VALUE;
    	if (this.isInsufficientMaterialDraw) return 0;
    	if (this.isWhiteStalemateDraw) return 0;
    	if (this.isBlackStalemateDraw) return 0;
//    	if (this.halfmoveClock >= Constants.NO_PIECE_CAPTURE_DRAW_HALFMOVES_LIMIT) return 0;
    	
    	
		// String startPosition = lastMove.getPositions().get(0);
		String endPosition = lastMove.getPositions().get(1);
    	ChessPiece lastMovedPiece = Utilities.getChessPieceFromPosition(this.gameBoard, endPosition);
    	double lastMovedChessPieceValue = Utilities.getChessPieceValue(lastMovedPiece, this.halfmoveNumber);
		// System.out.println("lastPiece: " + Utilities.getPieceNameByValue(lastPiece));
    	
    	int endRow = Utilities.getRowFromPosition(endPosition);
    	int endColumn = Utilities.getColumnFromPosition(endPosition);
    	
    	
    	/* DEBUGGING. */
    	// System.out.println(lastMove);
    	// System.out.println("lastCapturedPieceValue: " + this.lastCapturedPieceValue);
    	/*
    	System.out.println("gameBoard");
    	printChessBoard(gameBoard);
    	*/

    	// If Castling has occurred, add to the score.
    	if (whiteCastlingDone) {
    		// System.out.println("White castling done!");
    		whiteScore += Constants.CASTLING_VALUE;
    	}
    	else if (blackCastlingDone) {
    		// System.out.println("Black castling done!");
    		blackScore += Constants.CASTLING_VALUE;
    	}
    	else if (!whiteCastlingDone && !isWhiteQueensideCastlingAvailable() && !isWhiteKingsideCastlingAvailable()) {
    		// System.out.println("White castling lost!");
    		whiteScore -= 10 * Constants.CASTLING_VALUE;
    	}
    	else if (!blackCastlingDone && !isBlackQueensideCastlingAvailable() && !isBlackKingsideCastlingAvailable()) {
    		// System.out.println("Black castling lost!");
    		blackScore -= 10 * Constants.CASTLING_VALUE;
    	}
    	
    	// Calculate the values of each chessPiece and store them in the "valueBoard" array.
    	int n1 = numOfRows;
    	int n2 = NUM_OF_COLUMNS;
    	double[][] valueBoard = new double[n1][n2];
    	for (int i=0; i<n1; i++) {
			for (int j=0; j<n2; j++) {
				
				/* In the beginning, these sum up to 39, for each player. */
				valueBoard[i][j] = Utilities.getChessPieceValue(this.gameBoard[i][j], this.halfmoveNumber);
				
			}
    	}
    	
    	double checkValue = 0;
    	if (this.halfmoveNumber <= Constants.MIDDLEGAME_HALFMOVES_THRESHOLD)
    		checkValue = Constants.CHECK_VALUE;
		else
			checkValue = Constants.CHECK_LATE_VALUE;

    	/* Evaluation for the check move. */
    	// If last the chessPiece is White and the Black King is in check.
    	if (lastMovedPiece.getAllegiance() == Allegiance.WHITE && this.blackKingInCheck) {
			// If the White chessPiece is threatened and it has no cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
	    		whiteScore += checkValue / (double) lastMovedChessPieceValue;
			} 
			// If the White chessPiece is threatened and it has cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
	    		whiteScore += checkValue - lastMovedChessPieceValue;
			} 
			// If the White chessPiece is not threatened and it has no cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += checkValue;
			}
			// If the White chessPiece is not threatened and it has cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
				whiteScore += checkValue;
			}
		}
    	// If the last chessPiece is Black and the White King is in check.
		else if (lastMovedPiece.getAllegiance() == Allegiance.BLACK && this.whiteKingInCheck) {
			// If the White chessPiece is threatened and it has no cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
	    		blackScore += checkValue / (double) lastMovedChessPieceValue;
			} 
			// If the White chessPiece is threatened and it has cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += checkValue - lastMovedChessPieceValue;
			} 
			// If the White chessPiece is not threatened and it has no cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += checkValue;
			}
			// If the White chessPiece is not threatened and it has cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += checkValue;
			}
		}
    	
    	/* Evaluation for a capture move. */
    	// Check if a White chessPiece captured a Black chessPiece, which means that the score has increased.
    	if (lastMovedPiece.getAllegiance() == Allegiance.WHITE && this.lastCapturedPieceValue != 0) {
			// System.out.println("White captured a Black chessPiece.");
			// If the White chessPiece is threatened and it has no cover.
			if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += 5 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// whiteScore += 1;
			} 
			// If the White chessPiece is threatened and it has cover.
			else if (this.tilesThreatenedByBlack[endRow][endColumn] == 1 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
	    		whiteScore += 10 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// whiteScore += 1;
			}
			// If the White chessPiece is not threatened and it has no cover.
			else if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 0) {
				whiteScore += 80 * this.lastCapturedPieceValue;
				// whiteScore += 1;
			}
			// If the White chessPiece is not threatened and it has cover.
			else if (this.tilesThreatenedByBlack[endRow][endColumn] == 0 && this.tilesThreatenedByWhite[endRow][endColumn] == 1) {
				whiteScore += 100 * this.lastCapturedPieceValue;
				// whiteScore += 2;
			}
		}
    	// If a Black chessPiece captured a White chessPiece, which means that the score has decreased.
    	if (lastMovedPiece.getAllegiance() == Allegiance.BLACK && this.lastCapturedPieceValue != 0) {
			// System.out.println("Black captured a White chessPiece.");
    		// If the Black chessPiece is threatened and it has no cover.
			if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += 5 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// blackScore += 1;
			} 
			// If the Black chessPiece is threatened and it has cover.
			else if (this.tilesThreatenedByWhite[endRow][endColumn] == 1 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += 10 * (this.lastCapturedPieceValue - lastMovedChessPieceValue);
				// blackScore += 1;
			}
			// If the Black chessPiece is not threatened and it has no cover.
			else if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 0) {
				blackScore += 80 * this.lastCapturedPieceValue;
				// blackScore += 1;
			}
			// If the Black chessPiece is not threatened and it has cover.
			else if (this.tilesThreatenedByWhite[endRow][endColumn] == 0 && this.tilesThreatenedByBlack[endRow][endColumn] == 1) {
				blackScore += 100 * this.lastCapturedPieceValue;
				// blackScore += 2;
			}
		}
    	
		/* Add to the score the sum of the values of all the pieces of each player. */
		/* Lower the score, for each chessPiece threatened by the opponent. */
    	int whiteLegalMoves = 0;
    	int blackLegalMoves = 0;
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				
				/* Lower the score if the chessPiece is threatened by the opponent.
				 * Else, if the chessPiece is not threatened, increment the score. */
		    	// If the chessPiece is White.
				if (this.gameBoard[i][j].getAllegiance() == Allegiance.WHITE) {
					// If the White chessPiece is threatened and it has no cover.
					if (this.tilesThreatenedByBlack[i][j] == 1 && this.tilesThreatenedByWhite[i][j] == 0) {
						whiteScore -= 50 * valueBoard[i][j];
						// whiteScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the White chessPiece is threatened and it has cover.
					else if (this.tilesThreatenedByBlack[i][j] == 1 && this.tilesThreatenedByWhite[i][j] == 1) {
			    		whiteScore -= 20 * valueBoard[i][j];
						// whiteScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the White chessPiece is not threatened and it has no cover.
					else if (this.tilesThreatenedByBlack[i][j] == 0 && this.tilesThreatenedByWhite[i][j] == 0) {
						// whiteScore += valueBoard[i][j];
						whiteScore += valueBoard[i][j];
					}
					// If the White chessPiece is not threatened and it has cover.
					else if (this.tilesThreatenedByBlack[i][j] == 0 && this.tilesThreatenedByWhite[i][j] == 1) {
						// whiteScore += valueBoard[i][j];
						whiteScore += valueBoard[i][j];
					}
				}
		    	// If the chessPiece is Black.
				if (this.gameBoard[i][j].getAllegiance() == Allegiance.BLACK) {
					// If the Black chessPiece is threatened and it has no cover.
					if (this.tilesThreatenedByWhite[i][j] == 1 && this.tilesThreatenedByBlack[i][j] == 0) {
						blackScore -= 50 * valueBoard[i][j];
						// blackScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the Black chessPiece is threatened and it has cover.
					else if (this.tilesThreatenedByWhite[i][j] == 1 && this.tilesThreatenedByBlack[i][j] == 1) {
						blackScore -= 20 * valueBoard[i][j];
						// blackScore -= Constants.ATTACK_MULTIPLIER * valueBoard[i][j];
					}
					// If the Black chessPiece is not threatened and it has no cover.
					else if (this.tilesThreatenedByWhite[i][j] == 0 && this.tilesThreatenedByBlack[i][j] == 0) {
						// blackScore += valueBoard[i][j];
						blackScore += valueBoard[i][j];
					}
					// If the Black chessPiece is not threatened and it has cover.
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
		if (numOfWhiteBishops == 2)
			whiteScore += Constants.TWO_BISHOPS_VALUE;
		if (numOfBlackBishops == 2)
			blackScore += Constants.TWO_BISHOPS_VALUE;
		
		// Add extra penalty, if the Queen, or any Rook is lost, in late game. 
    	if (this.halfmoveNumber > Constants.MIDDLEGAME_HALFMOVES_THRESHOLD) {
    		whiteScore -= (isQueenLost(Allegiance.WHITE)) ? Constants.QUEEN_LATE_VALUE * 130 : 0;
			blackScore -= (isQueenLost(Allegiance.BLACK)) ? Constants.QUEEN_LATE_VALUE * 130 : 0;
			
			whiteScore -= (2 - getNumOfRooks(Allegiance.WHITE)) * Constants.ROOK_LATE_VALUE * 130;
			blackScore -= (2 - getNumOfRooks(Allegiance.BLACK)) * Constants.ROOK_LATE_VALUE * 130;
			
			whiteScore -= (2 - numOfWhiteBishops) * Constants.BISHOP_LATE_VALUE * 130;
			blackScore -= (2 - numOfBlackBishops) * Constants.BISHOP_LATE_VALUE * 130;
			
			whiteScore -= (2 - getNumOfKnights(Allegiance.WHITE)) * Constants.KNIGHT_LATE_VALUE * 130;
			blackScore -= (2 - getNumOfKnights(Allegiance.BLACK)) * Constants.KNIGHT_LATE_VALUE * 130;
    	} 
    	
		// Add extra penalty, if the Queen, or any Rook is lost, in early game. 
    	else {
    		whiteScore -= (isQueenLost(Allegiance.WHITE)) ? Constants.QUEEN_VALUE * 130 : 0;
			blackScore -= (isQueenLost(Allegiance.BLACK)) ? Constants.QUEEN_VALUE * 130 : 0;
			
			whiteScore -= (2 - getNumOfRooks(Allegiance.WHITE)) * Constants.ROOK_VALUE * 130;
			blackScore -= (2 - getNumOfRooks(Allegiance.BLACK)) * Constants.ROOK_VALUE * 130;
			
			whiteScore -= (2 - numOfWhiteBishops) * Constants.BISHOP_VALUE * 130;
			blackScore -= (2 - numOfBlackBishops) * Constants.BISHOP_VALUE * 130;
			
			whiteScore -= (2 - getNumOfKnights(Allegiance.WHITE)) * Constants.KNIGHT_VALUE * 130;
			blackScore -= (2 - getNumOfKnights(Allegiance.BLACK)) * Constants.KNIGHT_VALUE * 130;
    	}
		
		return whiteScore - blackScore;
	}
    
    
    @SuppressWarnings("unused")
    // Might be used to determine if we are in a late game state.
	private int getPlayerNumOfPiecesLeft(boolean player) {
    	int numOfPieces = 0;
    	for (int i=0; i<numOfRows; i++) {
    		for (int j=0; j<NUM_OF_COLUMNS; j++) {
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
    	for (int i=0; i<n1; i++) {
    		for (int j=0; j<n2; j++) {
    			if (this.gameBoard[i][j] instanceof Queen &&
    					playerAllegiance == this.gameBoard[i][j].getAllegiance())
    				return false;
    		}
    	}
    	
    	return true;
    }
	
    
	private int getNumOfRooks(Allegiance playerAllegiance) {
    	int numOfRooks = 0;
    	
    	int n1 = this.gameBoard.length;
    	int n2 = this.gameBoard[0].length;
    	for (int i=0; i<n1; i++) {
    		for (int j=0; j<n2; j++) {
    			if (this.gameBoard[i][j] instanceof Rook &&
    					playerAllegiance == this.gameBoard[i][j].getAllegiance())
    				numOfRooks++;
    		}
    	}
    	
    	return numOfRooks;
    }
	
	
	private int getNumOfBishops(Allegiance playerAllegiance) {
    	int numOfBishops = 0;
    	
    	int n1 = this.gameBoard.length;
    	int n2 = this.gameBoard[0].length;
    	for (int i=0; i<n1; i++) {
    		for (int j=0; j<n2; j++) {
    			if (this.gameBoard[i][j] instanceof Bishop &&
    					(playerAllegiance == this.gameBoard[i][j].getAllegiance()))
    				numOfBishops++;
    		}
    	}
    	
    	return numOfBishops;
    }
	

	private int getNumOfKnights(Allegiance playerAllegiance) {
    	int numOfKnights = 0;
    	
    	int n1 = this.gameBoard.length;
    	int n2 = this.gameBoard[0].length;
    	for (int i=0; i<n1; i++) {
    		for (int j=0; j<n2; j++) {
    			if (this.gameBoard[i][j] instanceof Knight &&
    					playerAllegiance == this.gameBoard[i][j].getAllegiance())
    				numOfKnights++;
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

    	if (checkForInsufficientMaterialDraw()) return true;
    	
    	// if (getHalfmoveClock() >= Constants.NO_PIECE_CAPTURE_DRAW_HALFMOVES_LIMIT)
    	// 	return true;

        return false;
    }
    

	public boolean isTerminalState() {
		if (isWhiteCheckmate() || isBlackCheckmate() || 
			isWhiteStalemateDraw() || isBlackStalemateDraw() || isInsufficientMaterialDraw()) {
			// getHalfmoveClock() >= Constants.NO_PIECE_CAPTURE_HALFMOVES_DRAW_LIMIT) {
			return true;
		}
		return false;
	}

	
    public void emptyTheChessBoard() {
        for (int i=0; i<numOfRows; i++) {
            for (int j=0; j<NUM_OF_COLUMNS; j++) {
                gameBoard[i][j] = new EmptyTile();
            }
        }
    }
    
    
    // This function is used for the Draw implementation.
    // If the number of empty tiles before the halfmove, 
    // is the same as the number of empty tiles after the halfmove, 
    // the halfmoveClock increments by 1.
    public int countNumberOfEmptyTiles() {
    	int numEmptyTiles = 0;
        for (int i=0; i<numOfRows; i++) {
            for (int j=0; j<NUM_OF_COLUMNS; j++) {
                if (gameBoard[i][j] instanceof EmptyTile) {
                	numEmptyTiles++;
                }
            }
        }
        return numEmptyTiles;
    }
    
    
	public Set<String> getNextPositions(String position) {
		Set<String> nextPositions = new TreeSet<String>();
		
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
		
		Set<String> tempNextPositions = new TreeSet<String>(nextPositions);
		for (String tempNextPosition: tempNextPositions) {
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
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				this.tilesThreatenedByWhite[i][j] = 0;
				this.tilesThreatenedByBlack[i][j] = 0;
			}
		}
		
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {

				ChessPiece chessPiece = this.gameBoard[i][j];
				String position = Utilities.getPositionByRowCol(i, j);
				
				Set<String> threatPositions = null;
				
				threatPositions = chessPiece.getNextPositions(position, this, true);
				
				// System.out.println("position: " + position + ", threatPositions: " + threatPositions);
				
				if (threatPositions != null && threatPositions.size() != 0) {
					for (String threatPosition: threatPositions) {
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
			this.blackKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>();
		}
		
		if (blackKingThreatened == 1) {
			this.blackKingInCheck = true;
			
			// Check for all possible moves made by Black, 
			// that can get the Black king out of a possible check
			// and store them in a variable called "blackKingInCheckValidPieceMoves".
			for (int i=0; i<numOfRows; i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
					if (currentPiece.getAllegiance() == Allegiance.BLACK) {
						String currentPosition = Utilities.getPositionByRowCol(i, j);
						Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);
						
						Set<String> validBlackKingInCheckTempHintPosition = new TreeSet<String>();
						for (String currentHintPosition: currentHintPositions) {
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
				this.blackKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>();
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
			this.whiteKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>();
		}
		
		if (whiteKingThreatened == 1) {
			this.whiteKingInCheck = true;
			
			// Check for all possible moves made by White, 
			// that can get the White king out of a possible check
			// and store them in a variable called "whiteKingInCheckValidPieceMoves".
			for (int i=0; i<numOfRows; i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
					if (currentPiece.getAllegiance() == Allegiance.WHITE) {
						String currentPosition = Utilities.getPositionByRowCol(i, j);
						Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);
						
						Set<String> validWhiteKingInCheckTempHintPositions = new TreeSet<String>();
						for (String currentHintPosition: currentHintPositions) {
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
				this.whiteKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>();
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
	        for (int i=0; i<n1; i++) {
	            for (int j=0; j<n2; j++) {
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
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
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
		
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
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
		
		if (numOfPawns <= 2) return false;
		
		return true;
	}
	
	
	// Checks if only a king and one or two knights have remained on the board, on the given player's side.
	public boolean isLoneKingPlusOneOrTwoKnights(Allegiance playerAllegiance) {
		
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
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
		
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
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
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
				// System.out.println("i: " + i + ", j: " + j + ", tempChessPiece: " + tempChessPiece);
				if (currentPiece.getAllegiance() == Allegiance.WHITE) {
					String currentPosition = Utilities.getPositionByRowCol(i, j);
					Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);
					
					for (String currentHintPosition: currentHintPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(currentPosition, currentHintPosition, false);
						
						int whiteKingRow = Utilities.getRowFromPosition(initialChessBoard.getWhiteKingPosition());
						int whiteKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getWhiteKingPosition());
						
						// If any move exists without getting the White king in check,
						// then there still are legal moves and we do not have a stalemate scenario.
						boolean legalMovesExist = 
								(initialChessBoard.getTilesThreatenedByBlack()[whiteKingRow][whiteKingColumn] == 0) ? true : false; 

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
		for (int i=0; i<numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				ChessPiece currentPiece = initialChessBoard.getGameBoard()[i][j];
				// System.out.println("i: " + i + ", j: " + j + ", tempChessPiece: " + tempChessPiece);
				if (currentPiece.getAllegiance() == Allegiance.BLACK) {
					String currentPosition = Utilities.getPositionByRowCol(i, j);
					Set<String> currentHintPositions = initialChessBoard.getNextPositions(currentPosition);
					
					for (String currentHintPosition: currentHintPositions) {
						initialChessBoard.movePieceFromAPositionToAnother(currentPosition, currentHintPosition, false);
						
						int blackKingRow = Utilities.getRowFromPosition(initialChessBoard.getBlackKingPosition());
						int blackKingColumn = Utilities.getColumnFromPosition(initialChessBoard.getBlackKingPosition());
	
						// If any move exists without getting the Black king in check,
						// then there still are legal moves and we do not have a stalemate scenario.
						boolean legalMovesExist = 
								(initialChessBoard.getTilesThreatenedByWhite()[blackKingRow][blackKingColumn] == 0) ? true : false; 
						
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
		for (int i=0; i<n1; i++) {
			for (int j=0; j<n2; j++) {
				this.gameBoard[i][j] = gameBoard[i][j];
			}
		}
	}
	
	public int[][] getTilesThreatenedByWhite() {
		return tilesThreatenedByWhite;
	}

	public void setTilesThreatenedByWhite(int[][] tilesThreatenedByWhite) {
		int n1 = tilesThreatenedByWhite.length;
		int n2 = tilesThreatenedByWhite[0].length;
		this.tilesThreatenedByWhite = new int[n1][n2];
		for (int i=0; i<n1; i++) {
			for (int j=0; j<n2; j++) {
				this.tilesThreatenedByWhite[i][j] = tilesThreatenedByWhite[i][j];
			}
		}
	}

	public int[][] getTilesThreatenedByBlack() {
		return tilesThreatenedByBlack;
	}

	public void setTilesThreatenedByBlack(int[][] tilesThreatenedByBlack) {
		int n1 = tilesThreatenedByBlack.length;
		int n2 = tilesThreatenedByBlack[0].length;
		this.tilesThreatenedByBlack = new int[n1][n2];
		for (int i=0; i<n1; i++) {
			for (int j=0; j<n2; j++) {
				this.tilesThreatenedByBlack[i][j] = tilesThreatenedByBlack[i][j];
			}
		}
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

	public boolean isWhiteQueensideCastlingAvailable() {
		if (!whiteKingMoved && !leftWhiteRookMoved) {
			return true;
		}
		return false;
	}
	
	public boolean isWhiteKingsideCastlingAvailable() {
		if (!whiteKingMoved && !rightWhiteRookMoved) {
			return true;
		}
		return false;
	}
	
	public void setRightWhiteRookMoved(boolean rightWhiteRookMoved) {
		this.rightWhiteRookMoved = rightWhiteRookMoved;
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

	public boolean isBlackQueensideCastlingAvailable() {
		if (!blackKingMoved && !leftBlackRookMoved) {
			return true;
		}
		return false;
	}
	
	public boolean isBlackKingsideCastlingAvailable() {
		if (!blackKingMoved && !rightBlackRookMoved) {
			return true;
		}
		return false;
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
		this.whiteKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>(whiteKingInCheckValidPieceMoves);
	}

	public Map<String, Set<String>> getBlackKingInCheckValidPieceMoves() {
		return blackKingInCheckValidPieceMoves;
	}

	public void setBlackKingInCheckValidPieceMoves(Map<String, Set<String>> blackKingInCheckValidPieceMoves) {
		this.blackKingInCheckValidPieceMoves = new TreeMap<String, Set<String>>(blackKingInCheckValidPieceMoves);
	}
	
	public boolean whitePlays() {
		return this.player == Constants.WHITE;
	}
	
	public boolean blackPlays() {
		return this.player == Constants.BLACK;
	}
	
	public boolean getPlayer() {
		return player;
	}
	
	public void setPlayer(boolean player) {
		this.player = player;
	}
	
	public int getHalfmoveNumber() {
		return halfmoveNumber;
	}
	
	public void setHalfmoveNumber(int halfmoveNumber) {
		this.halfmoveNumber = halfmoveNumber;
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

	public int getHalfmoveClock() {
		return halfmoveClock;
	}

	public void setHalfmoveClock(int halfmoveClock) {
		this.halfmoveClock = halfmoveClock;
	}
	
	public boolean isNoCaptureDraw() {
		return this.halfmoveClock >= Constants.NO_PIECE_CAPTURE_DRAW_HALFMOVES_LIMIT;
	}

	public int getWhiteCapturedPiecesCounter() {
		return whiteCapturedPiecesCounter;
	}

	public void setWhiteCapturedPiecesCounter(int whiteCapturedPiecesCounter) {
		this.whiteCapturedPiecesCounter = whiteCapturedPiecesCounter;
	}

	public int getBlackCapturedPiecesCounter() {
		return blackCapturedPiecesCounter;
	}

	public void setBlackCapturedPiecesCounter(int blackCapturedPiecesCounter) {
		this.blackCapturedPiecesCounter = blackCapturedPiecesCounter;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public double getLastCapturedPieceValue() {
		return lastCapturedPieceValue;
	}

	public void setLastCapturedPieceValue(int lastCapturedPieceValue) {
		this.lastCapturedPieceValue = lastCapturedPieceValue;
	}
	
	public Set<ChessPiece> getPromotedPieces() {
		return promotedPieces;
	}

	public void setPromotedPieces(Set<ChessPiece> promotedPieces) {
		this.promotedPieces = promotedPieces;
	}
	
	// It prints the chess board on the console.
   	public static void printChessBoard(ChessPiece[][] chessBoard) {
		System.out.println(getChessBoardString(chessBoard));
	}
   	
   	public static String getChessBoardString(ChessPiece[][] chessBoard) {
   		String output = "";
   		output += "    A   B   C   D   E   F   G   H\n";
   		output += "  ---------------------------------\n";
   		int n1 = chessBoard.length;
   		int n2 = chessBoard[0].length;
		for (int i=0; i<n1; i++) {
			output += (n1-i) + " |";
			for (int j=0; j<n2; j++) {
				output += " " + chessBoard[n1-1-i][j].getChessPieceChar() + " |";
			}
			output += " " + (n1-i) + "\n";
		}
		output += "  ---------------------------------\n";
		output += "    A   B   C   D   E   F   G   H\n";
		
		return output;
   	}
   	
   	
   	@Override
   	public String toString() {
   		return getChessBoardString(this.gameBoard);
   	}
   	
}
