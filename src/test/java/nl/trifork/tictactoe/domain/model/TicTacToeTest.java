package nl.trifork.tictactoe.domain.model;

import static nl.trifork.tictactoe.domain.Token.O;
import static nl.trifork.tictactoe.domain.Token.X;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import nl.trifork.tictactoe.commands.ExecuteTurnCmd;
import nl.trifork.tictactoe.domain.TicTacToe;
import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.events.GameFinished;
import nl.trifork.tictactoe.domain.events.GameStarted;
import nl.trifork.tictactoe.domain.events.GameWon;
import nl.trifork.tictactoe.domain.events.TurnExecuted;
import nl.trifork.tictactoe.domain.exceptions.InvalidCellLocationException;
import nl.trifork.tictactoe.domain.exceptions.InvalidPlayerForTurnException;
import nl.trifork.tictactoe.domain.exceptions.TileIsAlreadyFilledException;

public class TicTacToeTest {

	private FixtureConfiguration<TicTacToe> fixture;

	@Before
	public void setUp() {
		fixture = new AggregateTestFixture<>(TicTacToe.class);
	}

	@Test
	public void testInvalidCommands() {
		final String id = UUID.randomUUID().toString();
		final GameStarted startEvent = new GameStarted(id, "player", "cpu", X);

		fixture.given(startEvent)
				.when(new ExecuteTurnCmd(id, O, 0, 0))
				.expectException(InvalidPlayerForTurnException.class);

		fixture.given(startEvent)
				.when(new ExecuteTurnCmd(id, X, 3, 0))
				.expectException(InvalidCellLocationException.class);

		fixture.given(startEvent)
				.andGivenCommands(new ExecuteTurnCmd(id, X, 2, 0))
				.when(new ExecuteTurnCmd(id, O, 2, 0))
				.expectException(TileIsAlreadyFilledException.class);
	}

	@Test
	public void testDraw() {
		final String id = UUID.randomUUID().toString();
		final Token startingToken = X;
		final GameStarted startEvent = new GameStarted(id, "player", "cpu", startingToken);

		final Token[] row1 = { O, X, O };
		final Token[] row2 = { X, X, O };
		final Token[] row3 = { X, O };
		final List<ExecuteTurnCmd> turns = generateTurns(id, startingToken, row1, row2, row3);

		fixture.given(startEvent)
				.andGivenCommands(turns)
				.when(new ExecuteTurnCmd(id, X, 2, 2))
				.expectEvents(new TurnExecuted(id, X, 2, 2), new GameFinished(id));
	}

	@Test
	public void testDiagonalWinningGames() {
		final String id = UUID.randomUUID().toString();
		final Token startingToken = X;
		final GameStarted startEvent = new GameStarted(id, "player", "cpu", startingToken);

		final Token[] game1row1 = { X, O, X };
		final Token[] game1row2 = { O, X };
		final Token[] game1row3 = { O };
		final List<ExecuteTurnCmd> turnsGame1 = generateTurns(id, startingToken, game1row1, game1row2, game1row3);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame1)
				.when(new ExecuteTurnCmd(id, X, 2, 2))
				.expectEvents(new TurnExecuted(id, X, 2, 2), new GameWon(id, "player"));

		final Token[] game2row1 = { X, O, X };
		final Token[] game2row2 = { O, X, O };
		final List<ExecuteTurnCmd> turnsGame2 = generateTurns(id, startingToken, game2row1, game2row2);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame2)
				.when(new ExecuteTurnCmd(id, X, 0, 2))
				.expectEvents(new TurnExecuted(id, X, 0, 2), new GameWon(id, "player"));
	}

	@Test
	public void testHorizontalWinningGames() {
		final String id = UUID.randomUUID().toString();
		final Token startingToken = X;
		final GameStarted startEvent = new GameStarted(id, "player", "cpu", startingToken);

		final Token[] game1row1 = { X, X, };
		final Token[] game1row2 = { O, O };
		final Token[] game1row3 = { X };
		final List<ExecuteTurnCmd> turnsGame1 = generateTurns(id, startingToken, game1row1, game1row2, game1row3);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame1)
				.when(new ExecuteTurnCmd(id, O, 2, 1))
				.expectEvents(new TurnExecuted(id, O, 2, 1), new GameWon(id, "cpu"));

		final Token[] game2row1 = { X, X };
		final Token[] game2row2 = { O, O };
		final List<ExecuteTurnCmd> turnsGame2 = generateTurns(id, startingToken, game2row1, game2row2);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame2)
				.when(new ExecuteTurnCmd(id, X, 2, 0))
				.expectEvents(new TurnExecuted(id, X, 2, 0), new GameWon(id, "player"));
	}

	@Test
	public void testVerticalWinningGames() {
		final String id = UUID.randomUUID().toString();
		final Token startingToken = X;
		final GameStarted startEvent = new GameStarted(id, "player", "cpu", startingToken);

		final Token[] game1row1 = { X, O, };
		final Token[] game1row2 = { X, O };
		final List<ExecuteTurnCmd> turnsGame1 = generateTurns(id, startingToken, game1row1, game1row2);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame1)
				.when(new ExecuteTurnCmd(id, X, 0, 2))
				.expectEvents(new TurnExecuted(id, X, 0, 2), new GameWon(id, "player"));

		final Token[] game2row1 = { X, X, O };
		final Token[] game2row2 = { O, X, O };
		final Token[] game2row3 = { X };
		final List<ExecuteTurnCmd> turnsGame2 = generateTurns(id, startingToken, game2row1, game2row2, game2row3);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame2)
				.when(new ExecuteTurnCmd(id, O, 2, 2))
				.expectEvents(new TurnExecuted(id, O, 2, 2), new GameWon(id, "cpu"));
	}

	@Test
	public void testWinningGames() {
		final String id = UUID.randomUUID().toString();
		final Token startingToken = X;
		final GameStarted startEvent = new GameStarted(id, "player", "cpu", startingToken);

		final Token[] game1row1 = { X, X };
		final Token[] game1row2 = { O, O, X };
		final Token[] game1row3 = { O, O, X };
		final List<ExecuteTurnCmd> turnsGame1 = generateTurns(id, startingToken, game1row1, game1row2, game1row3);

		fixture.given(startEvent)
				.andGivenCommands(turnsGame1)
				.when(new ExecuteTurnCmd(id, X, 2, 0))
				.expectEvents(new TurnExecuted(id, X, 2, 0), new GameWon(id, "player"));
	}

	private List<ExecuteTurnCmd> generateTurns(final String gameId, final Token startingToken, final Token[]... rows) {
		final List<ExecuteTurnCmd> xTurns = new ArrayList<>();
		final List<ExecuteTurnCmd> oTurns = new ArrayList<>();
		for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < rows[rowIndex].length; columnIndex++) {
				final Token value = rows[rowIndex][columnIndex];
				final ExecuteTurnCmd turn = new ExecuteTurnCmd(
						gameId,
						rows[rowIndex][columnIndex],
						columnIndex,
						rowIndex);
				if (value == X) {
					xTurns.add(turn);
				} else {
					oTurns.add(turn);
				}
			}
		}

		return startingToken == X ? merge(xTurns, oTurns) : merge(oTurns, xTurns);
	}

	public static <T> ArrayList<T> merge(final Collection<T> a, final Collection<T> b) {
		final Iterator<T> itA = a.iterator();
		final Iterator<T> itB = b.iterator();
		final ArrayList<T> result = new ArrayList<T>();

		while (itA.hasNext() || itB.hasNext()) {
			if (itA.hasNext()) {
				result.add(itA.next());
			}
			if (itB.hasNext()) {
				result.add(itB.next());
			}
		}

		return result;
	}

}
