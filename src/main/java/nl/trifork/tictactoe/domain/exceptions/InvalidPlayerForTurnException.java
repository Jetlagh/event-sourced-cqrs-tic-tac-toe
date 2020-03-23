package nl.trifork.tictactoe.domain.exceptions;

public class InvalidPlayerForTurnException extends RuntimeException {

	private static final long serialVersionUID = -7036621697498592093L;

	public InvalidPlayerForTurnException(final String message) {
		super(message);
	}

	public InvalidPlayerForTurnException(final String message, final Throwable error) {
		super(message, error);
	}

}
