package nl.trifork.tictactoe;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameLogicController {

    @PostMapping("/executeTurn")
    public String move(@RequestParam boolean turn, @RequestParam int column, @RequestParam int row) {
        return String.format("Turn executed for player '%b', on column '%d' and row '%d'", turn, column, row);
    }
}
