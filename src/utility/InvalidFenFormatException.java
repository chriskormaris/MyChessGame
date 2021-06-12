package utility;

public class InvalidFenFormatException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4797768551407700600L;

	public InvalidFenFormatException() {
		super("Invalid FEN format given!");
	}
	
	public InvalidFenFormatException(String message) {
		super("Invalid FEN format given.\n" + message);
	}

}
