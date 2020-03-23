package nl.trifork.tictactoe.domain.events;

import nl.trifork.tictactoe.domain.common.BaseEvent;

public class GameFinished extends BaseEvent<String> {

	public GameFinished(final String id) {
		super(id);
	}

}
