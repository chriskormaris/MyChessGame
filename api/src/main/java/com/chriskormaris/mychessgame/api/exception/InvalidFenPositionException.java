package com.chriskormaris.mychessgame.api.exception;

public class InvalidFenPositionException extends RuntimeException {

	public InvalidFenPositionException(String message) {
		super(String.format("Invalid FEN position given.\n %s", message));
	}

}
