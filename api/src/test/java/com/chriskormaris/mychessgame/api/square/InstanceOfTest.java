package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import org.junit.jupiter.api.Test;

public class InstanceOfTest {

	@Test
	public void testInstanceOfVsOperatorEqualsSpeed() {
		ChessPiece[] chessPieces = new ChessPiece[100000];

		for (int i = 0; i < chessPieces.length; i++) {
			chessPieces[i] = new Pawn(Allegiance.WHITE);
		}

		/**********************************************************/

		long startTime = System.nanoTime();
		for (ChessPiece chessPiece : chessPieces) {
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
		for (ChessPiece chessPiece : chessPieces) {
			if (chessPiece.getPieceChar() == 'P') {
				// System.out.println("P");
			}
		}
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;

		System.out.println("operator equals '=' elapsedTime: " + elapsedTime + " ns");
	}

}
