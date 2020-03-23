package nl.trifork.tictactoe.domain.exceptions;

public class TileIsAlreadyFilledException extends RuntimeException {

	private static final long serialVersionUID = -7036621697498592093L;

	public TileIsAlreadyFilledException(final String message) {
		super(message);
	}

	public TileIsAlreadyFilledException(final String message, final Throwable error) {
		super(message, error);
	}

}
