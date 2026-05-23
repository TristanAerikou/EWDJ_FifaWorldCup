package lv.ewdj.fifaworldcup.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lv.ewdj.fifaworldcup.dto.InputGameDto;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("allGames")
    public String showallGames(Model model) {
        List<OutputGameDto> gameDtos = gameService.findAllGames();
        model.addAttribute("allGames", gameDtos);
        return "homeScreen";
    }

    @GetMapping("create")
    public String showCreateForm(InputGameDto inputGameDto) {
        return "gameCreate";
    }

    @PostMapping("create")
    public String processCreateForm(
            @Valid InputGameDto inputGameDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "gameCreate";
        }

        gameService.saveGame(inputGameDto);

        return "redirect:/game/allgames"; // radpleeg wedstrijd //TODO
    }

    @GetMapping("prognosis/{id}")
    public String viewPrognosis(@PathVariable int id/*, InputPrognosisDto inputPrognosisDto*/, Model model) {
        Optional<Game> optionalGame = gameService.getGameById(id);
        if (optionalGame.isEmpty()) throw new EntityNotFoundException("Game not found");

        OutputGameDto gameDto = OutputGameDto.objToDto(optionalGame.get());
        model.addAttribute("game", gameDto);
        return "prognosisView";
    }

}
