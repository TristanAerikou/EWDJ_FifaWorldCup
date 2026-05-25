package lv.ewdj.fifaworldcup.controller;

import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.GameService;
import lv.ewdj.fifaworldcup.util.DateTimeFormats;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "rest/")
public class RestGameController {

    private final GameService gameService;

//    @GetMapping("allGames") // dto = geen (de)serializer nodig...
//    public List<OutputGameDto> getAllGames() {
//        return gameService.findAllGames();
//    }

    @GetMapping("games")
    public List<Game> getAllGames() {
        return gameService.findAllGames();
    }


    @GetMapping("games/{date}")
    public List<Game> getAllGames(
            @PathVariable String date
    ) {
        LocalDate formattedDate = LocalDate.parse(date, DateTimeFormats.DATE_FORMATTER);
        return gameService.findAllGamesByDate(formattedDate);
    }

    @GetMapping("stadiums")
    public List<String> getAllStadiums() {
        return gameService.getAllStadiums();
    }

}
