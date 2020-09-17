package utilities;

// import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import pieces.Bishop;
import pieces.ChessPiece;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;


public class Utilities {
	
	
	public static String getPositionByRowCol(int row, int column) {
		String columnString = (char) (column + 65) + "";
		
		// We add '0' which is equal to 48 in decimal, 
		// so that we get the correct number.
		String rowString = (row + 1) + "";
		
		String position = columnString + rowString;
		
		return position;
	}
	
	// ALTERNATIVE
	/*
	public static String getPositionByRowCol(int row, int column) {
		return Constants.chessPositions[row][column];
	}
	*/
	
	public static int getRowFromPosition(String position) {
		// example: A2, column = 0, row = 1
		int row = Integer.parseInt(position.substring(1)) - 1;
		return row;
	}
	
	
	public static int getColumnFromPosition(String position) {
		// example: B1, column = 1, row = 0
		int column = (int) position.charAt(0) - 65;
		return column;
	}
	
	
	public static ChessPiece getChessPieceFromPosition(ChessPiece[][] gameBoard, String position) {
		int row = getRowFromPosition(position);
		int column = getColumnFromPosition(position);
		return gameBoard[row][column];
	}
	
	
	public static ChessPiece[][] copyGameBoard(ChessPiece[][] otherGameBoard) {
		int n1 = otherGameBoard.length;
		int n2 = otherGameBoard[0].length;
		
		ChessPiece[][] newGameBoard = new ChessPiece[n1][n2];
		for (int i=0; i<n1; i++) {
			for (int j=0; j<n2; j++) {
				newGameBoard[i][j] = otherGameBoard[i][j].makeCopy();
			}
		}
		return newGameBoard;
	}
	
	
	public static boolean checkEqualGameBoards(ChessPiece[][] gameBoard, ChessPiece[][] otherGameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;
		
		for (int i=0; i<n1; i++) {
			for (int j=0; j<n2; j++) {
				if (!(gameBoard[i][j].getAllegiance() == otherGameBoard[i][j].getAllegiance()
					  && gameBoard[i][j].getChessPieceChar() == otherGameBoard[i][j].getChessPieceChar())) {
					return false;
				}
			}
		}
		return true;
	}
	
   	public static synchronized void playSound(final String path) {
   		
   		try {
	   	     URL defaultSound = Utilities.class.getResource("/sounds/" + path);
	   	     
	   	     // File soundFile = new File(defaultSound.toURI());
	   	     // System.out.println("soundFile: " + soundFile);  // check the URL!
	   	     
	   	     AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(defaultSound);
	   	     Clip clip = AudioSystem.getClip();
	   	     clip.open(audioInputStream);
	   	     clip.start( );
	   	} catch (Exception ex) {
	   		// ex.printStackTrace();
	   	    System.err.println(ex.getMessage());
	   	}
   		
   	}
   	
   	
   	public static double getChessPieceValue(ChessPiece chessPiece, int halfmoveNumber) {
   		double value = 0;
   		if (halfmoveNumber <= Constants.MIDDLEGAME_HALFMOVES_THRESHOLD) {
	   		if (chessPiece instanceof Pawn) {
	   			value = Constants.PAWN_VALUE;
			} else if (chessPiece instanceof Knight) {
				value = Constants.KNIGHT_VALUE;
			} else if (chessPiece instanceof Bishop) {
				value = Constants.BISHOP_VALUE;
			} else if (chessPiece instanceof Rook) {
				value = Constants.ROOK_VALUE;
			} else if (chessPiece instanceof Queen) {
				value = Constants.QUEEN_VALUE;
			}
   		} else {
	   		if (chessPiece instanceof Pawn) {
	   			value = Constants.PAWN_LATE_VALUE;
			} else if (chessPiece instanceof Knight) {
				value = Constants.KNIGHT_LATE_VALUE;
			} else if (chessPiece instanceof Bishop) {
				value = Constants.BISHOP_LATE_VALUE;
			} else if (chessPiece instanceof Rook) {
				value = Constants.ROOK_LATE_VALUE;
			} else if (chessPiece instanceof Queen) {
				value = Constants.QUEEN_LATE_VALUE;
			}
   		}
   		return value;
   	}
   	
   	
}
