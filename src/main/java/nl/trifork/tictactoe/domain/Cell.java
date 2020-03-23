package nl.trifork.tictactoe.domain;

class Cell {

	private final Token token;

	Cell(final Token token) {
		this.token = token;
	}

	public Token getToken() {
		return this.token;
	}

	@Override
	public String toString() {
		return token != null ? token.toString() : "_";
	}

}
