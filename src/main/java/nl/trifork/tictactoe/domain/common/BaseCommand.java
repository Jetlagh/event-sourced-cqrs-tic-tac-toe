package nl.trifork.tictactoe.domain.common;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class BaseCommand<T> {

	@TargetAggregateIdentifier
	private final T id;

	public BaseCommand(final T id) {
		this.id = id;
	}

	public T getId() {
		return id;
	}
}
