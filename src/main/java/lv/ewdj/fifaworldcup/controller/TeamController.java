package lv.ewdj.fifaworldcup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.*;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.service.TeamService;
import lv.ewdj.fifaworldcup.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("team")
@RequiredArgsConstructor
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;
    private final UserService userService;

    @GetMapping
    public String redirectToRoute(Model model, Principal principal) {
        String username = principal.getName();
        OutputTeamDto team = teamService.getTeamByUsername(username);
        if (team == null) {
            return "redirect:/team/noTeam";
        }
        return "redirect:/team/myTeam";
    }

    @GetMapping("noTeam")
    public String showNoTeam(Principal principal, Model model) {
        OutputTeamDto team = teamService.getTeamByUsername(principal.getName());
        if (team != null) {
            return "redirect:/team/myTeam";
        }

        if (!model.containsAttribute("inputTeamDto")) {
            model.addAttribute("inputTeamDto", new InputTeamDto(""));
        }
        if (!model.containsAttribute("inputInvitecodeDto")) {
            model.addAttribute("inputInvitecodeDto", new InputInvitecodeDto(""));
        }
        return "noTeam";
    }

    @PostMapping("create")
    public String createTeam(
            @Valid @ModelAttribute("inputTeamDto") InputTeamDto inputTeamDto,
            BindingResult result,
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Waarom Redirect? Heeft te maken met het feit dat /team/noTeam twee dto's verwacht, waarvan deze methode
            // maar één meegeeft. Ook Moet BindingResult van deze dto meegegeven worden doordat SpringBoot de dto met de
            // BindingResult samen behandelt.
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.inputTeamDto", result);
            redirectAttributes.addFlashAttribute("inputTeamDto", inputTeamDto);
            return "redirect:/team/noTeam";
        }

        teamService.createTeam(inputTeamDto, principal.getName());

        return "redirect:/team";

    }

    @PostMapping("join")
    public String joinTeam(
            @Valid @ModelAttribute("inputInvitecodeDto") InputInvitecodeDto inputInvitecodeDto,
//            InputTeamDto inputTeamDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.inputInvitecodeDto", result);
            redirectAttributes.addFlashAttribute("inputInvitecodeDto", inputInvitecodeDto);
            return "redirect:/team/noTeam";
        }

        teamService.joinTeam(principal.getName(), inputInvitecodeDto.inviteCode());

        return  "redirect:/team/myTeam";
    }

    @GetMapping("myTeam")
    public String showTeam(InputRemovememberDto inputRemovememberDto, Model model, Principal principal) {
        OutputTeamDto team = teamService.getTeamByUsername(principal.getName());
        if (team == null) {
            return "redirect:/team/noTeam";
        }

        List<OutputUserDto> users = userService.getUsersByTeamName(team.name());
        OutputUserDto owner = userService.getUserOwningTeam(team.name());
        OutputFullTeamDto fullTeamDto = new OutputFullTeamDto(
                team.name(),
                team.inviteCode(),
                users,
                owner,
                0
        );
        model.addAttribute("team", fullTeamDto);

        int totalScore = 0;
        for (OutputUserDto user : users) {
            totalScore += user.points();
        }
        model.addAttribute("totalScore", totalScore);

        return "teamPage";
    }

    @PostMapping("user/remove")
    public String removeMember(InputRemovememberDto inputRemovememberDto) {

        Team team = teamService.getTeamByTeamname(inputRemovememberDto.teamName());
        if (Objects.equals(inputRemovememberDto.username(), userService.getUserOwningTeam(team.getName()).username())) {
            throw new IllegalArgumentException("You cannot remove the owner of a team, incidently being yourself.");
        }

        teamService.removeMember(inputRemovememberDto.username());

        return "redirect:/team/myTeam";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        // flashattribute ipv model.addattribute door redirect (flash wordt behouden, niet-flash attribuut niet on redirect)
        ex.printStackTrace();
        redirectAttributes.addFlashAttribute("illArgExc", ex.getMessage());

        return "redirect:/team/myTeam";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDuplicateEntry(DataIntegrityViolationException ex, RedirectAttributes redirectAttributes) {

        log.error(ex.getMessage(), ex);

       if (ex.getMessage().toLowerCase().contains("duplicate entry")) {
           redirectAttributes.addFlashAttribute("duplEntryExc", "This team already exists.");
       }

        return "redirect:/team/noTeam";
    }
}