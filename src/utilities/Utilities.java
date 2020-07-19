package utilities;

// import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import pieces.ChessPiece;


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
	   	     ex.printStackTrace();
	   	}
   		
   	}
   	
   	
   	public static String getPieceNameByValue(int pieceValue) {
   		switch (pieceValue) {
   			case Constants.WHITE_PAWN:
   				return "WHITE PAWN";
   			case Constants.WHITE_KNIGHT:
   				return "WHITE KNIGHT";
   			case Constants.WHITE_BISHOP:
   				return "WHITE BISHOP";
   			case Constants.WHITE_ROOK:
   				return "WHITE ROOK";
   			case Constants.WHITE_QUEEN:
   				return "WHITE QUEEN";
   			case Constants.WHITE_KING:
   				return "WHITE KING";
   				
   			case Constants.BLACK_PAWN:
   				return "BLACK PAWN";
   			case Constants.BLACK_KNIGHT:
   				return "BLACK KNIGHT";
   			case Constants.BLACK_BISHOP:
   				return "BLACK BISHOP";
   			case Constants.BLACK_ROOK:
   				return "BLACK ROOK";
   			case Constants.BLACK_QUEEN:
   				return "BLACK QUEEN";
   			case Constants.BLACK_KING:
   				return "BLACK KING";
   				
   			case Constants.EMPTY:
   				return "BLACK KING";
   			
   			default:
   				return "UNKNOWN PIECE";
   		}
   	}
   	
   	
   	public static double getPieceValueByRowCol(ChessPiece gameBoard[][], int row, int column, int halfmoveNumber) {
   		double value = Constants.EMPTY;
   		int absPieceCode = Math.abs(gameBoard[row][column].getPieceCode());
   		if (halfmoveNumber <= Constants.MIDDLEGAME_HALFMOVES_THRESHOLD) {
	   		if (absPieceCode == Constants.PAWN) {
	   			value = Constants.PAWN_VALUE;
			} else if (absPieceCode == Constants.KNIGHT) {
				value = Constants.KNIGHT_VALUE;
			} else if (absPieceCode == Constants.BISHOP) {
				value = Constants.BISHOP_VALUE;
			} else if (absPieceCode == Constants.ROOK) {
				value = Constants.ROOK_VALUE;
			} else if (absPieceCode == Constants.QUEEN) {
				value = Constants.QUEEN_VALUE;
			}
   		} else {
   			if (absPieceCode == Constants.PAWN) {
	   			value = Constants.PAWN_LATE_VALUE;
			} else if (absPieceCode == Constants.KNIGHT) {
				value = Constants.KNIGHT_LATE_VALUE;
			} else if (absPieceCode == Constants.BISHOP) {
				value = Constants.BISHOP_LATE_VALUE;
			} else if (absPieceCode == Constants.ROOK) {
				value = Constants.ROOK_LATE_VALUE;
			} else if (absPieceCode == Constants.QUEEN) {
				value = Constants.QUEEN_LATE_VALUE;
			}
   		}
   		return value;
   	}
   	
	
}
