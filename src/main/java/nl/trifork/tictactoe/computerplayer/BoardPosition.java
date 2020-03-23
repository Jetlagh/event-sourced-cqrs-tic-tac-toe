package nl.trifork.tictactoe.computerplayer;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
class BoardPosition {

	private int column;
	@Column(name = "BP_ROW")
	private int row;

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	public void setColumn(final int column) {
		this.column = column;
	}

	public void setRow(final int row) {
		this.row = row;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BoardPosition)) {
			return false;
		}
		final BoardPosition position = (BoardPosition) other;
		return column == position.column && row == position.row;
	}

	@Override
	public int hashCode() {
		return Objects.hash(column, row);
	}

	@Override
	public String toString() {
		return "BoardPosition [column=" + column + ", row=" + row + "]";
	}

	public static BoardPosition of(final int column, final int row) {
		final BoardPosition board = new BoardPosition();
		board.setColumn(column);
		board.setRow(row);
		return board;
	}

}
