package nl.trifork.tictactoe.commands;

import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.common.BaseCommand;

public class ExecuteTurnCmd extends BaseCommand<String> {

	private final Token token;
	private final int column;
	private final int row;

	public ExecuteTurnCmd(final String id, final Token token, final int column, final int row) {
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

	@Override
	public String toString() {
		return "ExecuteTurnCmd [id=" + getId() + ", token=" + token + ", column=" + column + ", row=" + row + "]";
	}

}
