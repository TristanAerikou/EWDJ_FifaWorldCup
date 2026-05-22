package lv.ewdj.fifaworldcup.controller;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputGameDto;
import lv.ewdj.fifaworldcup.dto.OutputTeamDto;
import lv.ewdj.fifaworldcup.dto.UserDto;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.service.TeamService;
import lv.ewdj.fifaworldcup.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public String redirectToRoute(Model model, Principal principal) {
        String username = principal.getName();
        OutputTeamDto team = teamService.getTeamByUsername(username);
        if (team == null) {
            return "redirect:/team/noTeam";
        }
        return "redirect:/team/" + team.name();
    }
}