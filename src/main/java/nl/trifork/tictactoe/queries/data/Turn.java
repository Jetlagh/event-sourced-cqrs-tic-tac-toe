package nl.trifork.tictactoe.queries.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Turn {

	private String value;

	@Column(name = "CELL_ROW")
	private int row;
	private int column;

	public String getValue() {
		return value;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	private void setValue(final String value) {
		this.value = value;
	}

	private void setRow(final int row) {
		this.row = row;
	}

	private void setColumn(final int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "Turn [value=" + value + ", row=" + row + ", column=" + column + "]";
	}

	public static Turn of(final String value, final int column, final int row) {
		final Turn cell = new Turn();
		cell.setValue(value);
		cell.setColumn(column);
		cell.setRow(row);
		return cell;
	}
}