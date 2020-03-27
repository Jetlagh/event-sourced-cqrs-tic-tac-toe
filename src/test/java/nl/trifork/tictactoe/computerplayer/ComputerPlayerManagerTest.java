package nl.trifork.tictactoe.computerplayer;

import static nl.trifork.tictactoe.computerplayer.ComputerPlayerManager.CPU_PLAYER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import nl.trifork.tictactoe.commands.ExecuteTurnCmd;
import nl.trifork.tictactoe.domain.Token;
import nl.trifork.tictactoe.domain.events.GameStarted;
import nl.trifork.tictactoe.domain.events.TurnExecuted;

@SpringBootTest
public class ComputerPlayerManagerTest {

	@Autowired
	private ComputerPlayerManager computerPlayer;

	@MockBean
	private CommandGateway gateway;

	@MockBean
	private ComputerGameRepository gameRepository;

	@Test
	void testExecuteTurnOnGameStart() {
		final String gameId = UUID.randomUUID().toString();
		final String playerX = "player";
		final String playerO = CPU_PLAYER_ID;

		computerPlayer.on(new GameStarted(gameId, playerX, playerO, Token.O));
		computerPlayer.on(new GameStarted(gameId, playerX, playerO, Token.X));

		// Test the repository tries to persist the games
		verify(gameRepository, times(2)).save(any(ComputerGame.class));

		// Test the computer player only sends a command when it is the computers turn
		verify(gateway, times(1)).send(any(ExecuteTurnCmd.class));
	}

	@Test
	void testExecuteTurnOnPlayersTurn() {
		final String gameId = UUID.randomUUID().toString();
		final ComputerGame game = new ComputerGame(gameId, Token.X);
		when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

		// Test the computer player only sends a command when it is the computers turn
		computerPlayer.on(new TurnExecuted(gameId, Token.O, 0, 0));
		computerPlayer.on(new TurnExecuted(gameId, Token.X, 0, 0));
		verify(gateway, times(1)).send(any(ExecuteTurnCmd.class));
	}

	@Test
	void testReturnWhenNotComputersGame() {
		final String gameId = UUID.randomUUID().toString();
		final String playerX = "player";
		final String playerO = "another_player";

		// Test the repository doesn't persist a game without a computer player
		computerPlayer.on(new GameStarted(gameId, playerX, playerO, Token.O));
		verify(gameRepository, times(0)).save(any(ComputerGame.class));

		// Test the computer player doesn't execute a turn when there's no computer game
		when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

		computerPlayer.on(new TurnExecuted(gameId, Token.O, 0, 0));
		verify(gateway, times(0)).send(any(ExecuteTurnCmd.class));
	}

}
