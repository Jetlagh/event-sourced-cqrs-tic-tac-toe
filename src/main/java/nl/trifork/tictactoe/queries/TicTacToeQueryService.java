package nl.trifork.tictactoe.queries;

import static java.util.Collections.reverseOrder;
import static java.util.Collections.synchronizedSortedSet;
import static nl.trifork.tictactoe.computerplayer.ComputerPlayerManager.CPU_PLAYER_ID;
import static nl.trifork.tictactoe.domain.TicTacToe.BOARD_SIZE;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Service;

import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.events.GameStarted;
import nl.trifork.tictactoe.domain.events.GameWon;
import nl.trifork.tictactoe.domain.events.TurnExecuted;
import nl.trifork.tictactoe.player.PlayerService;
import nl.trifork.tictactoe.queries.data.GameSummary;
import nl.trifork.tictactoe.queries.data.GameSummaryRepository;
import nl.trifork.tictactoe.queries.data.Turn;

@Service
public class TicTacToeQueryService {

	private final GameSummaryRepository gameRepository;
	private final SortedSet<PlayerScore> topScores = synchronizedSortedSet(new TreeSet<>(reverseOrder()));
	private final PlayerService userService;

	public TicTacToeQueryService(final GameSummaryRepository boardRepository, final PlayerService userService) {
		this.gameRepository = boardRepository;
		this.userService = userService;
	}

	@EventHandler
	private void on(final GameStarted event, @Timestamp final Instant timestamp) {
		final GameSummary game = createGameSummary(event, timestamp);
		gameRepository.save(game);
	}

	@EventHandler
	private void on(final TurnExecuted event) {
		final Optional<GameSummary> optional = gameRepository.findById(event.getId());
		final GameSummary game = optional.orElse(null);

		if (game == null) {
			return;
		}

		final String value = event.getToken() == Token.X ? "X" : "O";
		final Turn cell = Turn.of(value, event.getColumn(), event.getRow());
		game.addTurn(cell);

		gameRepository.save(game);
	}

	@EventHandler
	protected void on(final GameWon event, @Timestamp final Instant timestamp) {
		final GameSummary game = gameRepository.findById(event.getId()).orElse(null);

		if (game == null) {
			return;
		}

		final Duration gameDuration = Duration.between(game.getStartingTime(), timestamp);
		final int turnCount = countWinningPlayerTurns(game, event.getWinningPlayerId());
		final String playerName = getPlayerName(event.getWinningPlayerId());

		final PlayerScore score = new PlayerScore(playerName, turnCount, gameDuration);
		topScores.add(score);

		if (topScores.size() > 10) {
			topScores.remove(topScores.last());
		}
	}

	private String getPlayerName(final String winningPlayerId) {
		if (CPU_PLAYER_ID.equalsIgnoreCase(winningPlayerId)) {
			return "Computer";
		}
		final String name = userService.getUserName(winningPlayerId);
		if (name == null) {
			return "Anonymous";
		}
		return name;
	}

	private int countWinningPlayerTurns(final GameSummary game, final String winningPlayerId) {
		final String winningPlayersToken = game.getPlayerO() == winningPlayerId ? "O" : "X";

		return (int) game.getPlayedTurns().stream()
				.filter(turn -> turn.getValue().equalsIgnoreCase(winningPlayersToken)).count();
	}

	private GameSummary createGameSummary(final GameStarted event, final Instant timestamp) {
		final GameSummary board = new GameSummary();
		board.setId(event.getId());
		board.setStartingTime(timestamp);
		board.setPlayerO(event.getPlayerO());
		board.setPlayerX(event.getPlayerX());

		return board;
	}

	public String[][] getBoard(final String gameId) {
		final GameSummary game = gameRepository.findById(gameId).orElse(null);
		final List<Turn> turns = game.getPlayedTurns();

		final String[][] board = new String[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				board[row][column] = "_";
			}
		}

		for (final Turn turn : turns) {
			board[turn.getRow()][turn.getColumn()] = turn.getValue();
		}

		return board;
	}

	public List<PlayerScore> getTopScores() {
		final List<PlayerScore> scores = new ArrayList<>(topScores);
		return scores;
	}

}
