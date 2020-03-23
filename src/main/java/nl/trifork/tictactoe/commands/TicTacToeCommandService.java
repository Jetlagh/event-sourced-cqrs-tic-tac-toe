package nl.trifork.tictactoe.commands;

import static nl.trifork.tictactoe.computerplayer.ComputerPlayerManager.CPU_PLAYER_ID;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import nl.trifork.tictactoe.domain.Token;

@Service
public class TicTacToeCommandService {

	private final CommandGateway gateway;

	public TicTacToeCommandService(final CommandGateway gateway) {
		this.gateway = gateway;
	}

	public CompletableFuture<String> createGameAgainstComputer(final String playerId) {
		final String gameId = UUID.randomUUID().toString();

		final Token startingToken = Token.values()[new Random().nextInt(Token.values().length)];
		return gateway.send(new StartGameCmd(gameId, playerId, CPU_PLAYER_ID, startingToken));
	}

	public CompletableFuture<String> executeTurn(final String gameId, final int column, final int row) {
		return gateway.send(new ExecuteTurnCmd(gameId, Token.X, column, row));
	}

}
