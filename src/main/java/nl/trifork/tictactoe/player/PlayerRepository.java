package nl.trifork.tictactoe.player;

import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, String> {

}
