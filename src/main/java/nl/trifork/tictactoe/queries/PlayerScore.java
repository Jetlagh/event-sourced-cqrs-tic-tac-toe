package nl.trifork.tictactoe.queries;

import java.time.Duration;

public class PlayerScore implements Comparable<PlayerScore> {

	private static int BASE_SCORE = 100;
	private static int PENALTY_PER_TURN = 5;
	private static int PENALTY_PER_SECOND = 2;

	private final String playerName;
	private final int score;

	public PlayerScore(final String playerName, final int amountOfMoves, final Duration gameDuration) {
		this.playerName = playerName;
		this.score = calculateScore(amountOfMoves, gameDuration);
	}

	private int calculateScore(final int amountOfMoves, final Duration gameDuration) {
		return (int) (BASE_SCORE - (amountOfMoves * PENALTY_PER_TURN)
				- (gameDuration.getSeconds() * PENALTY_PER_SECOND));
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		return "PlayerScore [playerName=" + playerName + ", score=" + score + "]";
	}

	@Override
	public int compareTo(final PlayerScore other) {
		return Integer.compare(getScore(), other.getScore());
	}

}
