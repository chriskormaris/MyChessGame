package com.chriskormaris.mychessgame.api.exception;

public class InvalidFenFormatException extends Exception {

	public InvalidFenFormatException(String message) {
		super("Invalid FEN format given.\n" + message);
	}

}