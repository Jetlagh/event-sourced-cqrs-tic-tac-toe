package nl.trifork.tictactoe.queries.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class GameSummary {

	@Id
	private String id;

	@ElementCollection
	@CollectionTable(name = "PHONE", joinColumns = @JoinColumn(name = "OWNER_ID"))
	private List<Turn> state = new ArrayList<>();

	private String playerX;
	private String playerO;

	private Instant startingTime;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public List<Turn> getPlayedTurns() {
		if (state == null) {
			state = new ArrayList<>();
		}
		return state;
	}

	public void addTurn(final Turn cell) {
		getPlayedTurns().add(cell);
	}

	public String getPlayerX() {
		return playerX;
	}

	public void setPlayerX(final String playerX) {
		this.playerX = playerX;
	}

	public String getPlayerO() {
		return playerO;
	}

	public void setPlayerO(final String playerO) {
		this.playerO = playerO;
	}

	public Instant getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(final Instant startingTime) {
		this.startingTime = startingTime;
	}

}
