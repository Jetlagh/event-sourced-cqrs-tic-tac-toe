package nl.trifork.tictactoe.domain.exceptions;

public class GameAlreadyFinishedException extends RuntimeException {

	private static final long serialVersionUID = -7036621697498592093L;

	public GameAlreadyFinishedException(final String message) {
		super(message);
	}

	public GameAlreadyFinishedException(final String message, final Throwable error) {
		super(message, error);
	}

}
