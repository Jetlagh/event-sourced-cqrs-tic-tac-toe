package nl.trifork.tictactoe.player;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Player {

	@Id
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
