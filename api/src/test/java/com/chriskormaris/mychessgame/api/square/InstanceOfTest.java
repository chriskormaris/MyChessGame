package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import org.junit.jupiter.api.Test;

public class InstanceOfTest {

	@Test
	public void testInstanceOfVsOperatorEqualsSpeed() {
		ChessSquare[] chessSquares = new ChessSquare[100000];

		for (int i = 0; i< chessSquares.length; i++) {
			chessSquares[i] = new Pawn(Allegiance.WHITE);
		}

		/**********************************************************/

		long startTime = System.nanoTime();
		for (ChessSquare chessSquare : chessSquares) {
			if (chessSquare.isPawn()) {
				// System.out.println("Pawn");
			}
		}
		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;

		System.out.println("instanceof elapsedTime: " + elapsedTime + " ns");

		/**********************************************************/

		System.out.println();

		startTime = System.nanoTime();
		for (ChessSquare chessSquare : chessSquares) {
			if (chessSquare.getPieceChar() == 'P') {
				// System.out.println("P");
			}
		}
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;

		System.out.println("operator equals '=' elapsedTime: " + elapsedTime + " ns");
	}

}
