package nl.trifork.tictactoe.domain;

import static nl.trifork.tictactoe.domain.TicTacToe.BOARD_SIZE;

class Board {

	private final Cell[][] cells;

	Board() {
		this.cells = new Cell[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				cells[row][column] = new Cell(null);
			}
		}
	}

	private Board(final Cell[][] cells) {
		this.cells = cells;
	}

	public Token getTokenAt(final int column, final int row) {
		return cells[row][column].getToken();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (final Cell[] row : cells) {
			for (final Cell cell : row) {
				sb.append(cell);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	static class Builder {

		private final Cell[][] cells;

		Builder(final Board board) {
			this.cells = board.cells;
		}

		public Builder withCellAt(final Cell cell, final int column, final int row) {
			cells[row][column] = cell;
			return this;
		}

		public Board create() {
			return new Board(cells);
		}

	}

}
