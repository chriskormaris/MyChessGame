package chessGUI;

import org.junit.jupiter.api.Test;

import enums.Allegiance;
import pieces.ChessPiece;
import pieces.Pawn;
import utilities.Constants;

public class InstanceOfTest {
	
	@Test
	public void testInstanceofVsOperatorEqualsSpeed() {
		ChessPiece[] chessPieces = new ChessPiece[100000];
		
		for (int i=0; i<chessPieces.length; i++) {
			chessPieces[i] = new Pawn(Allegiance.WHITE);
		}
		
		/**********************************************************/

		long startTime = System.nanoTime();
		for (ChessPiece chessPiece: chessPieces) {
			if (chessPiece instanceof Pawn) {
				// System.out.println("Pawn");
			}
		}
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		
		System.out.println("instanceof elapsedTime: " + elapsedTime + " ns");
		
		/**********************************************************/
		
		System.out.println();
		
		startTime = System.nanoTime();
		for (ChessPiece chessPiece: chessPieces) {
			if (Math.abs(chessPiece.getPieceCode()) == Constants.PAWN) {
				//System.out.println("Pawn");
			}
		}
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;
		
		System.out.println("operator equals '=' elapsedTime: " + elapsedTime + " ns");
		
	}

}
