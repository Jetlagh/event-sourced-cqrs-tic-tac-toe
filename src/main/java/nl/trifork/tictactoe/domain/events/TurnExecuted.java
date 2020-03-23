package nl.trifork.tictactoe.domain.events;

import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.common.BaseEvent;

public class TurnExecuted extends BaseEvent<String> {

	private final Token token;
	private final int column;
	private final int row;

	public TurnExecuted(final String id, final Token token, final int column, final int row) {
		super(id);
		this.token = token;
		this.column = column;
		this.row = row;
	}

	public Token getToken() {
		return token;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

}
