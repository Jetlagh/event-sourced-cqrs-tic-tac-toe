package nl.trifork.tictactoe.commands;

import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.common.BaseCommand;

public class StartGameCmd extends BaseCommand<String> {

	private final String playerX;
	private final String playerO;
	private final Token startingToken;

	public StartGameCmd(final String id, final String playerX, final String playerO, final Token startingToken) {
		super(id);
		this.playerX = playerX;
		this.playerO = playerO;
		this.startingToken = startingToken;
	}

	public String getPlayerX() {
		return playerX;
	}

	public String getPlayerO() {
		return playerO;
	}

	public Token getStartingToken() {
		return startingToken;
	}

}
