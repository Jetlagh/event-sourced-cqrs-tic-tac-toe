package nl.trifork.tictactoe.computerplayer;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static nl.trifork.tictactoe.computerplayer.BoardPosition.of;
import static nl.trifork.tictactoe.domain.Token.O;
import static nl.trifork.tictactoe.domain.Token.X;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

import nl.trifork.tictactoe.commands.ExecuteTurnCmd;
import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.events.GameFinished;
import nl.trifork.tictactoe.domain.events.GameStarted;
import nl.trifork.tictactoe.domain.events.TurnExecuted;

@Service
public class ComputerPlayerManager {

	public static final String CPU_PLAYER_ID = "computer";

	private static final List<BoardPosition> ALL_POSITIONS = unmodifiableList(asList(
			of(0, 0), of(0, 1), of(0, 2),
			of(1, 0), of(1, 1), of(1, 2),
			of(2, 0), of(2, 1), of(2, 2)));

	private final ComputerGameRepository gameRepository;
	private final CommandGateway gateway;

	public ComputerPlayerManager(final CommandGateway gateway, final ComputerGameRepository gameRepository) {
		this.gateway = gateway;
		this.gameRepository = gameRepository;
	}

	@EventHandler
	public void on(final GameStarted event) {
		final Token computersToken = getComputerPlayerToken(event);

		// The event is not from a game with a computer player
		if (computersToken == null) {
			return;
		}

		final String gameId = event.getId();
		gameRepository.save(new ComputerGame(gameId, computersToken));

		if (isComputersTurn(event, computersToken)) {
			final int column = 1;
			final int row = 1;
			final ExecuteTurnCmd command = new ExecuteTurnCmd(gameId, computersToken, column, row);

			gateway.send(command);
		}
	}

	@EventHandler
	public void on(final TurnExecuted event) {
		final String gameId = event.getId();
		final ComputerGame game = gameRepository.findById(gameId).orElse(null);

		// The event is not from a game with a computer player
		if (game == null) {
			return;
		}

		game.addPlayedPosition(BoardPosition.of(event.getColumn(), event.getRow()));
		gameRepository.save(game);

		final Token computersToken = game.getTokenOfComputer();
		if (isComputersTurn(event, computersToken) && game.getPlayedPositions().size() < 9) {
			final BoardPosition turn = calculateTurn(game.getPlayedPositions());
			final ExecuteTurnCmd command = new ExecuteTurnCmd(gameId, computersToken, turn.getColumn(), turn.getRow());

			gateway.send(command);
		}
	}

	@EventHandler
	public void on(final GameFinished event) {
		gameRepository.deleteById(event.getId());
	}

	private boolean isComputersTurn(final GameStarted event, final Token computersToken) {
		return event.getStartingToken() == computersToken;
	}

	private boolean isComputersTurn(final TurnExecuted event, final Token computersToken) {
		return event.getToken() != computersToken;
	}

	private BoardPosition calculateTurn(final List<BoardPosition> playedPositions) {
		final List<BoardPosition> options = new ArrayList<>(ALL_POSITIONS);
		options.removeAll(playedPositions);

		return options.get(new Random().nextInt(options.size()));
	}

	private Token getComputerPlayerToken(final GameStarted event) {
		if (CPU_PLAYER_ID.equals(event.getPlayerO())) {
			return O;
		} else if (CPU_PLAYER_ID.equals(event.getPlayerX())) {
			return X;
		}
		return null;
	}

}
