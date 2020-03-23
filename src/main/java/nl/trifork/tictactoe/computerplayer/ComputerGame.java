package nl.trifork.tictactoe.computerplayer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import nl.trifork.tictactoe.domain.Token;

@Entity
class ComputerGame {

	@Id
	private String id;
	private Token tokenOfComputer;

	@ElementCollection
	private List<BoardPosition> playedPositions;

	public ComputerGame() {
	}

	public ComputerGame(final String id, final Token tokenOfComputer) {
		this.id = id;
		this.tokenOfComputer = tokenOfComputer;
	}

	public String getId() {
		return id;
	}

	public Token getTokenOfComputer() {
		return tokenOfComputer;
	}

	public List<BoardPosition> getPlayedPositions() {
		if (playedPositions == null) {
			playedPositions = new ArrayList<>();
		}
		return playedPositions;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setTokenOfComputer(final Token tokenOfComputer) {
		this.tokenOfComputer = tokenOfComputer;
	}

	public void addPlayedPosition(final BoardPosition position) {
		getPlayedPositions().add(position);
	}

}