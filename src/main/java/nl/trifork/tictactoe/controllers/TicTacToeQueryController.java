package nl.trifork.tictactoe.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.trifork.tictactoe.queries.PlayerScore;
import nl.trifork.tictactoe.queries.TicTacToeQueryService;

@RestController
public class TicTacToeQueryController {

	private final TicTacToeQueryService queryService;

	public TicTacToeQueryController(final TicTacToeQueryService queryService) {
		this.queryService = queryService;
	}

	@GetMapping("/board")
	public String[][] getBoard(@RequestParam final String gameId) {
		return queryService.getBoard(gameId);
	}

	@GetMapping("/topScores")
	public List<PlayerScore> getScores() {
		return queryService.getTopScores();
	}

}
