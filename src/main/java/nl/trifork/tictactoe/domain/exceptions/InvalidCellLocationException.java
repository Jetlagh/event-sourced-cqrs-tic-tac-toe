package nl.trifork.tictactoe.domain.exceptions;

public class InvalidCellLocationException extends RuntimeException {

	private static final long serialVersionUID = -7036621697498592093L;

	public InvalidCellLocationException(final String message) {
		super(message);
	}

	public InvalidCellLocationException(final String message, final Throwable error) {
		super(message, error);
	}

}
