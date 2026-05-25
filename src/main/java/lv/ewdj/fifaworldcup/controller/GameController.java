package lv.ewdj.fifaworldcup.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lv.ewdj.fifaworldcup.dto.*;
import lv.ewdj.fifaworldcup.model.Game;
import lv.ewdj.fifaworldcup.model.Prognosis;
import lv.ewdj.fifaworldcup.service.GameService;
import lv.ewdj.fifaworldcup.service.PrognosisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("game")
public class GameController {
    private final GameService gameService;

    private final PrognosisService prognosisService;

    @Value("#{messageSource.getMessage('points.X',null,'en')}")
    private int pointsX;
    @Value("#{messageSource.getMessage('points.Y',null,'en')}")
    private int pointsY;
    @Value("#{messageSource.getMessage('points.B',null,'en')}")
    private int pointsB;
    @Value("#{messageSource.getMessage('points.C',null,'en')}")
    private int pointsC;


    @Value("#{messageSource.getMessage('wedstrijd.edit.form.success', null, 'en')}")
    private String successMessage;

    public GameController(GameService gameService, PrognosisService prognosisService) {
        this.gameService = gameService;
        this.prognosisService = prognosisService;
    }

    @GetMapping("allGames")
    public String showallGames(Model model) {
        List<OutputGameDto> gameDtos = gameService.findAllGamesDtos();
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

        return "redirect:/game/allGames"; // radpleeg wedstrijd //TODO
    }

    @GetMapping("prognosis/{id}")
    public String viewPrognosis(@PathVariable int id, InputPrognosisDto inputPrognosisDto, Model model, Principal principal) {
        Optional<Game> optionalGame = gameService.getGameById(id);
        if (optionalGame.isEmpty()) throw new EntityNotFoundException("Game not found");

        OutputGameDto gameDto = OutputGameDto.objToDto(optionalGame.get());
        model.addAttribute("game", gameDto);

        List<Prognosis> prognosisList = prognosisService.getPrognosisByGameAndUser(id, principal.getName());
        if (!prognosisList.isEmpty()) {
            // hoe de .html pagina eruitziet hangt af van de game. Om minder if-checks te doen in de controller,
            // worden beide mogelijke nodige attributen aan het model gehanden, en de .html gebruikt degene die
            // het nodig heeft.
            model.addAttribute("prognosis", OutputPrognosisDto.objToDto(prognosisList.getFirst()));
            model.addAttribute("inputPrognosisDto", InputPrognosisDto.objToDto(prognosisList.getFirst()));
        }

        return "prognosisView";
    }

    @PostMapping("/prognosis/{gameId}")
    public String processPrognosisForm(
            @PathVariable int gameId,
            @Valid InputPrognosisDto inputPrognosisDto,
            BindingResult result,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            gameService.getGameById(gameId)
                    .map(OutputGameDto::objToDto)
                    .ifPresent(dto ->
                            model.addAttribute("game", dto)
                    );
            return "prognosisView";
        }

        prognosisService.createOrUpdatePrognosis(inputPrognosisDto, gameId, principal.getName());

        redirectAttributes.addFlashAttribute("successMessage", "Your prognosis has been registered successfully");
        return "redirect:/game/prognosis/" + gameId;
    }

    @GetMapping("edit/{gameId}")
    public String showEditForm(@PathVariable int gameId, Model model) {
        Optional<Game> optionalGame = gameService.getGameById(gameId);
        if (optionalGame.isEmpty()) throw new EntityNotFoundException("Game not found");

        Game game = optionalGame.get();
        InputEditGameDto inputEditGameDto = InputEditGameDto.objToDto(game);
        model.addAttribute("inputEditGameDto", inputEditGameDto);

        LocalDateTime exactPlayTime = LocalDateTime.of(game.getDateOfGame(), game.getTimeOfGame());
        boolean canEditScore = LocalDateTime.now().isAfter(exactPlayTime);
        model.addAttribute("canEditScore", canEditScore);
        return "gameEdit";
    }

    @PostMapping("edit/{gameId}")
    public String processEditform(
            @PathVariable int gameId,
            @Valid InputEditGameDto inputEditGameDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("gameId", gameId);
            return "gameEdit";
        }

        if (inputEditGameDto.scoreA() != null && inputEditGameDto.scoreB() != null) {
            gameService.updatePoints(
                    pointsX, pointsY, pointsB, pointsC,
                    inputEditGameDto.scoreA(), inputEditGameDto.scoreB(), gameId);
        }
        gameService.updateGame(inputEditGameDto, gameId);

        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/game/allGames";
    }
}
