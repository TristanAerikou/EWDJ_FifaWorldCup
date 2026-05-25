package lv.ewdj.fifaworldcup.controller;

import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "rest/games")
public class RestGameController {

    private final GameService gameService;

    @GetMapping("allGames")
    public List<OutputGameDto> getAllGames() {
        return gameService.findAllGames();
    }

}
