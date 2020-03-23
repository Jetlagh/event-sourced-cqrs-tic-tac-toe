package nl.trifork.tictactoe.domain.common;

public class BaseEvent<T> {

	private final T id;

	public BaseEvent(final T id) {
		this.id = id;
	}

	public T getId() {
		return id;
	}
}
