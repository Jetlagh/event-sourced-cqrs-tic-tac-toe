package nl.trifork.tictactoe.domain;

import static nl.trifork.tictactoe.domain.Token.O;
import static nl.trifork.tictactoe.domain.Token.X;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.trifork.tictactoe.commands.ExecuteTurnCmd;
import nl.trifork.tictactoe.commands.StartGameCmd;
import nl.trifork.tictactoe.domain.events.GameFinished;
import nl.trifork.tictactoe.domain.events.GameStarted;
import nl.trifork.tictactoe.domain.events.GameWon;
import nl.trifork.tictactoe.domain.events.TurnExecuted;
import nl.trifork.tictactoe.domain.exceptions.GameAlreadyFinishedException;
import nl.trifork.tictactoe.domain.exceptions.InvalidCellLocationException;
import nl.trifork.tictactoe.domain.exceptions.InvalidPlayerForTurnException;
import nl.trifork.tictactoe.domain.exceptions.TileIsAlreadyFilledException;

@Aggregate
public class TicTacToe {

	private final Logger logger = LoggerFactory.getLogger(TicTacToe.class);

	public static final int BOARD_SIZE = 3;

	@AggregateIdentifier
	private String id;
	private Player playerX;
	private Player playerO;
	private Board board;

	private Token nextTurn;

	private boolean finished;

	public TicTacToe() {
	}

	@CommandHandler
	public TicTacToe(final StartGameCmd cmd) {
		apply(new GameStarted(cmd.getId(), cmd.getPlayerX(), cmd.getPlayerO(), cmd.getStartingToken()));
	}

	@CommandHandler
	public void handle(final ExecuteTurnCmd cmd) {
		final int column = cmd.getColumn();
		final int row = cmd.getRow();

		if (finished) {
			throw new GameAlreadyFinishedException("The game is already finished. Failed at " + cmd);
		}

		if (nextTurn != cmd.getToken()) {
			throw new InvalidPlayerForTurnException("Turn is for [ token=" + nextTurn + " ]. Failed at " + cmd);
		}

		if (!isValidTileLocation(column, row)) {
			throw new InvalidCellLocationException("Column or row is outside the board. Failed at " + cmd);
		}

		if (!isTileEmpty(column, row)) {
			throw new TileIsAlreadyFilledException("The tile is already filled. Failed at " + cmd);
		}

		apply(new TurnExecuted(cmd.getId(), cmd.getToken(), column, row));
	}

	@EventSourcingHandler
	public void on(final GameStarted event) {
		id = event.getId();
		playerX = new Player(event.getPlayerX());
		playerO = new Player(event.getPlayerO());
		nextTurn = event.getStartingToken();

		board = new Board();
	}

	@EventSourcingHandler
	public void on(final TurnExecuted event) {
		final Token token = event.getToken();
		final int column = event.getColumn();
		final int row = event.getRow();

		final Cell newCell = new Cell(token);
		board = new Board.Builder(board).withCellAt(newCell, column, row).create();
		nextTurn = token == X ? O : X;

		if (isRowWinner(token, column) || isColumnWinner(token, row) || isDiagonalWinner(token, column, row)) {
			final String winningPlayer = token == X ? playerX.getId() : playerO.getId();
			apply(new GameWon(id, winningPlayer));
		} else if (isBoardFilled()) {
			apply(new GameFinished(id));
		}

		if (AggregateLifecycle.isLive()) {
			logger.info("Turn executed: \n" + board);
		}
	}

	@EventSourcingHandler
	public void on(final GameFinished event) {
		finished = true;
	}

	private boolean isBoardFilled() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				if (isTileEmpty(column, row)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isColumnWinner(final Token tokenOfTurn, final int rowOfTurn) {
		return board.getTokenAt(0, rowOfTurn) == tokenOfTurn
				&& board.getTokenAt(1, rowOfTurn) == tokenOfTurn
				&& board.getTokenAt(2, rowOfTurn) == tokenOfTurn;
	}

	private boolean isRowWinner(final Token tokenOfTurn, final int columnOfTurn) {
		return board.getTokenAt(columnOfTurn, 0) == tokenOfTurn
				&& board.getTokenAt(columnOfTurn, 1) == tokenOfTurn
				&& board.getTokenAt(columnOfTurn, 2) == tokenOfTurn;
	}

	private boolean isDiagonalWinner(final Token tokenOfTurn, final int columnOfTurn, final int rowOfTurn) {
		// X__ = column 0, row 0
		// _X_ = column 1, row 1
		// __X = column 2, row 2
		if (columnOfTurn == rowOfTurn) {
			return board.getTokenAt(0, 0) == tokenOfTurn
					&& board.getTokenAt(1, 1) == tokenOfTurn
					&& board.getTokenAt(2, 2) == tokenOfTurn;
		}

		// __O = column 2, row 0
		// _O_ = column 1, row 1
		// O__ = column 0, row 2
		if (columnOfTurn + rowOfTurn == 2) {
			return board.getTokenAt(2, 0) == tokenOfTurn
					&& board.getTokenAt(1, 1) == tokenOfTurn
					&& board.getTokenAt(0, 2) == tokenOfTurn;
		}

		return false;
	}

	private boolean isValidTileLocation(final int column, final int row) {
		return column < BOARD_SIZE && row < BOARD_SIZE;
	}

	private boolean isTileEmpty(final int column, final int row) {
		return board.getTokenAt(column, row) == null;
	}

}
