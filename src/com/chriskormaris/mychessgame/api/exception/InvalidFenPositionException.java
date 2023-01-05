package com.chriskormaris.mychessgame.api.exception;

public class InvalidFenPositionException extends RuntimeException {

	public InvalidFenPositionException(String message) {
		super("Invalid FEN position given.\n" + message);
	}

}
