package nl.trifork.tictactoe.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nl.trifork.tictactoe.commands.TicTacToeCommandService;
import nl.trifork.tictactoe.player.PlayerService;

@RestController
public class TicTacToeCommandController {

	private static final String PLAYER_ID_COOKIE = "PLAYER_ID";
	private final TicTacToeCommandService commandService;
	private final PlayerService playerService;

	public TicTacToeCommandController(final TicTacToeCommandService commandService, final PlayerService playerService) {
		this.commandService = commandService;
		this.playerService = playerService;
	}

	@PostMapping("/startGame")
	@ResponseStatus(CREATED)
	public CompletableFuture<String> startGame(
			@CookieValue(value = PLAYER_ID_COOKIE, required = false) final String idFromCookie,
			final HttpServletResponse response) {
		final String userId = getOrSetPlayerId(idFromCookie, response);

		return commandService.createGameAgainstComputer(userId);
	}

	private String getOrSetPlayerId(final String idFromCookie, final HttpServletResponse response) {
		String playerId = idFromCookie;
		if (playerId == null) {
			playerId = UUID.randomUUID().toString();
			final Cookie cookie = new Cookie(PLAYER_ID_COOKIE, playerId);
			response.addCookie(cookie);
		}
		return playerId;
	}

	@PostMapping("/executeTurn")
	public CompletableFuture<String> executeTurn(@RequestParam final String gameId, @RequestParam final int column,
			@RequestParam final int row) {
		return commandService.executeTurn(gameId, column, row);
	}

	@PostMapping("/registerName")
	public void registerName(@CookieValue(value = PLAYER_ID_COOKIE, required = false) final String idFromCookie,
			@RequestParam final String name, final HttpServletResponse response) {
		final String playerId = getOrSetPlayerId(idFromCookie, response);

		playerService.saveUser(playerId, name);
	}

}
