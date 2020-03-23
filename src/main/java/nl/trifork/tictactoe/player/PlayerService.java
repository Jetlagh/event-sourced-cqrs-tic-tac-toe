package nl.trifork.tictactoe.player;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PlayerService {

	private final PlayerRepository repository;

	public PlayerService(final PlayerRepository repository) {
		this.repository = repository;
	}

	public String getUserName(final String id) {
		final Optional<Player> optional = repository.findById(id);

		if (!optional.isPresent()) {
			return null;
		}

		return optional.get().getName();
	}

	public void saveUser(final String id, final String name) {
		final Player user = new Player();
		user.setId(id);
		user.setName(name);

		repository.save(user);
	}

}
