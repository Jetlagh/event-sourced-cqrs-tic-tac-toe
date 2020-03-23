package nl.trifork.tictactoe.domain.events;

import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.common.BaseEvent;

public class GameStarted extends BaseEvent<String> {

	private final String playerX;
	private final String playerO;
	private final Token startingToken;

	public GameStarted(final String id, final String playerX, final String playerO, final Token startingToken) {
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
