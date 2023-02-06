package com.chriskormaris.mychessgame.api.exception;

import lombok.experimental.StandardException;

@StandardException
public class InvalidFenPositionException extends RuntimeException {

	public InvalidFenPositionException(String message) {
		super("Invalid FEN position given.\n" + message);
	}

}
