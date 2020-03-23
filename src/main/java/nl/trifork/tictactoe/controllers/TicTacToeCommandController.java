package nl.trifork.tictactoe.controllers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.trifork.tictactoe.commands.TicTacToeCommandService;
import nl.trifork.tictactoe.player.PlayerService;

@RestController
public class TicTacToeCommandController {

	private final TicTacToeCommandService commandService;
	private final PlayerService userService;

	public TicTacToeCommandController(final TicTacToeCommandService commandService, final PlayerService userService) {
		this.commandService = commandService;
		this.userService = userService;
	}

	@PostMapping("/startGame")
	public CompletableFuture<String> startGame(
			@CookieValue(value = "userId", required = false) final String idFromCookie,
			final HttpServletResponse response) {
		final String userId = getOrSetUserId(idFromCookie, response);

		return commandService.createGameAgainstComputer(userId);
	}

	private String getOrSetUserId(final String idFromCookie, final HttpServletResponse response) {
		String userId = idFromCookie;
		if (userId == null) {
			userId = UUID.randomUUID().toString();
			final Cookie cookie = new Cookie("userId", userId);
			response.addCookie(cookie);
		}
		return userId;
	}

	@PostMapping("/executeTurn")
	public CompletableFuture<String> executeTurn(@RequestParam final String gameId, @RequestParam final int column,
			@RequestParam final int row) {
		return commandService.executeTurn(gameId, column, row);
	}

	@PostMapping("/registerName")
	public void registerName(@CookieValue(value = "userId", required = false) final String idFromCookie,
			@RequestParam final String name, final HttpServletResponse response) {
		final String userId = getOrSetUserId(idFromCookie, response);

		userService.saveUser(userId, name);
	}

}
