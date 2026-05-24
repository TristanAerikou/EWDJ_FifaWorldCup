package lv.ewdj.fifaworldcup.controller;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputPublicTeamDto;
import lv.ewdj.fifaworldcup.dto.OutputUserDto;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
import lv.ewdj.fifaworldcup.service.TeamService;
import lv.ewdj.fifaworldcup.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("public")
@RequiredArgsConstructor
public class PublicRankingController {

    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final UserService userService;

    @GetMapping
    public String showPublicRanking(Model model) {
        List<Team> topTenTeams = teamService.getTopTenTeams();
        List<OutputPublicTeamDto> topTenTeamsDtos = topTenTeams.stream().map(team -> {
            String name = team.getName();
            List<OutputUserDto> users = userService.getUsersByTeamName(team.getName());
            int totalPoints = users.stream()
                    .mapToInt(OutputUserDto::points)
                    .sum();
            int amountOfMembers = users.size();

            return new OutputPublicTeamDto(
                    name,
                    totalPoints,
                    amountOfMembers
            );
        }).toList();
        model.addAttribute("teams", topTenTeamsDtos);

        return "publicRanking";
    }

}
