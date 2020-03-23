package nl.trifork.tictactoe.queries;

import static java.time.Duration.ofSeconds;
import static nl.trifork.tictactoe.computerplayer.ComputerPlayerManager.CPU_PLAYER_ID;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import nl.trifork.tictactoe.domain.events.GameWon;
import nl.trifork.tictactoe.player.PlayerService;
import nl.trifork.tictactoe.queries.data.GameSummary;
import nl.trifork.tictactoe.queries.data.GameSummaryRepository;
import nl.trifork.tictactoe.queries.data.PlayerScore;
import nl.trifork.tictactoe.queries.data.Turn;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TicTacToeQueryServiceTest {

	@MockBean
	private GameSummaryRepository gameRepository;

	@MockBean
	private PlayerService playerService;

	@Autowired
	private TicTacToeQueryService queryService;

	@Test
	void testMaxTopScores() {
		for (int i = 15; i > 0; i--) {
			simulateGame(ofSeconds(10 + i), "XXXOO");
		}

		final List<PlayerScore> scores = queryService.getTopScores();
		assertTrue(scores.size() == 10);
	}

	@Test
	void testTopScoresOrderByTurns() {
		// Random saving order
		simulateGame(ofSeconds(10), "XXX");
		simulateGame(ofSeconds(10), "XXXXX");
		simulateGame(ofSeconds(10), "XXXX");

		final List<PlayerScore> scores = queryService.getTopScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.get(0).getScore() > scores.get(1).getScore());
		assertTrue(scores.get(1).getScore() > scores.get(2).getScore());
		assertTrue(scores.get(2).getScore() < scores.get(0).getScore());
	}

	@Test
	void testTopScoresOrderBySeconds() {
		// Random saving order
		simulateGame(ofSeconds(10), "XXX");
		simulateGame(ofSeconds(15), "XXX");
		simulateGame(ofSeconds(20), "XXX");

		final List<PlayerScore> scores = queryService.getTopScores();
		assertTrue(scores.size() == 3);
		assertTrue(scores.get(0).getScore() > scores.get(1).getScore());
		assertTrue(scores.get(1).getScore() > scores.get(2).getScore());
		assertTrue(scores.get(2).getScore() < scores.get(0).getScore());
	}

	@Test
	void testPlayerNames() {
		final String playerId = "player_id";
		when(playerService.getUserName(playerId)).thenReturn("Player name");

		// Random saving order
		simulateGame(ofSeconds(10), "XXX", "unknown");
		simulateGame(ofSeconds(10), "XXXX", CPU_PLAYER_ID);
		simulateGame(ofSeconds(10), "XXXXX", playerId);

		final List<PlayerScore> scores = queryService.getTopScores();
		assertSame(scores.get(0).getPlayerName(), "Anonymous");
		assertSame(scores.get(1).getPlayerName(), "Computer");
		assertSame(scores.get(2).getPlayerName(), "Player name");
	}

	private void simulateGame(final Duration duration, final String cells) {
		simulateGame(duration, cells, "player_id");
	}

	private void simulateGame(final Duration duration, final String cells, final String winningPlayerId) {
		final String id = UUID.randomUUID().toString();
		final GameSummary gameSummary = generateSummary(id, cells);
		when(gameRepository.findById(id)).thenReturn(Optional.of(gameSummary));

		final GameWon event = new GameWon(id, winningPlayerId);
		queryService.on(event, gameSummary.getStartingTime().plus(duration));
	}

	private GameSummary generateSummary(final String gameId, final String cells) {
		final String[] rows = cells.split("(?<=\\G.{3})");
		final GameSummary game = new GameSummary();
		game.setId(gameId);
		game.setStartingTime(Instant.now());
		for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < rows[rowIndex].length(); columnIndex++) {
				final String value = Character.toString(rows[rowIndex].charAt(columnIndex));

				if (!value.equals("_")) {
					game.addTurn(Turn.of(value, columnIndex, rowIndex));
				}
			}
		}

		return game;
	}

}
