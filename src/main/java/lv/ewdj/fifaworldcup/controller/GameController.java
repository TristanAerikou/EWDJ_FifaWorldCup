package lv.ewdj.fifaworldcup.controller;

import jakarta.validation.Valid;
import lv.ewdj.fifaworldcup.dto.GameInputDto;
import lv.ewdj.fifaworldcup.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("create")
    public String showCreateForm(GameInputDto gameInputDto) {
        return "wedstrijdCreate";
    }

    @PostMapping("create")
    public String processCreateForm(
            @Valid GameInputDto gameInputDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "wedstrijdCreate";
        }

        gameService.saveGame(gameInputDto);

        return "homeScreen"; // radpleeg wedstrijd //TODO
    }
}
