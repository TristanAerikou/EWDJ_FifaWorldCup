package lv.ewdj.fifaworldcup.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputInvitecodeDto;
import lv.ewdj.fifaworldcup.dto.InputTeamDto;
import lv.ewdj.fifaworldcup.dto.OutputTeamDto;
import lv.ewdj.fifaworldcup.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

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
    public String showTeam(Model model, Principal principal) {
        OutputTeamDto team = teamService.getTeamByUsername(principal.getName());
        if (team == null) {
            return "redirect:/team/noTeam";
        }

        model.addAttribute("team", team);

        return "teamPage";
    }

}