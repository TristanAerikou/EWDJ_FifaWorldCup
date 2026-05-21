package lv.ewdj.fifaworldcup.controller;

import lombok.AllArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/home")
public class HomeController {

    GameService gameService;

    @GetMapping
    public String showHomeScreen(Model model) {
        List<OutputGameDto> gameDtos = gameService.findAllGames();
        model.addAttribute("allGames", gameDtos);
        return "homeScreen";
    }
}
