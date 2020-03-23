package nl.trifork.tictactoe.domain.events;

public class GameWon extends GameFinished {

	private final String winningPlayerId;

	public GameWon(final String id, final String winningPlayerId) {
		super(id);
		this.winningPlayerId = winningPlayerId;
	}

	public String getWinningPlayerId() {
		return this.winningPlayerId;
	}

}
