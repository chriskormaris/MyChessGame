package com.chriskormaris.mychessgame.api.exception;

public class InvalidFenFormatException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 4797768551407700600L;

    public InvalidFenFormatException(String message) {
        super("Invalid FEN format given.\n" + message);
    }

}
