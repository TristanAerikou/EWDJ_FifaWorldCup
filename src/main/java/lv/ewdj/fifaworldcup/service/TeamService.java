package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputTeamDto;
import lv.ewdj.fifaworldcup.dto.OutputTeamDto;
import lv.ewdj.fifaworldcup.dto.OutputUserDto;
import lv.ewdj.fifaworldcup.exceptions.InvitecodeException;
import lv.ewdj.fifaworldcup.exceptions.UserNotFoundException;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    public OutputTeamDto getTeamByUsername(String username) {
        Optional<OutputUserDto> optionaUser = userService.getUserByUsername(username);
        if (optionaUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        Optional<Team> teamOptional = Optional.ofNullable(optionaUser.get().team());
        return teamOptional.map(OutputTeamDto::objToDto).orElse(null);
    }

    public void createTeam(InputTeamDto inputTeamDto, String username) {
        String inviteCode = generateInviteCode(inputTeamDto.name());

        checkInviteCode(inviteCode);

        Team team = teamRepository.save(InputTeamDto.dtoToObj(inputTeamDto, inviteCode));

        userService.updateUserTeam(username, team);
        userService.updateUserOwningTeam(username, team);

    }

    private void checkInviteCode(String inviteCode) {
        if (teamRepository.existsByInviteCode(inviteCode))
            throw new InvitecodeException("Already in use", inviteCode);
    }

    private String generateInviteCode(String name) {
        if (!name.matches(".*[a-zA-Z]+.*"))
            throw new InvitecodeException("Name must contain letters for invite code to be generated");
        int counter = 0;
        int nameLength = name.length();
        StringBuilder currentStr = new StringBuilder();
        while (currentStr.length() < 8) {
            char c = name.charAt(counter++ % nameLength);
            if (c >= 'A' && c <= 'z') {
                currentStr.append(c);
            } else {
                counter++;
            }
        }
        return currentStr.toString().toUpperCase();
    }

    public OutputTeamDto getTeamByInviteCode(String inviteCode) {
        Optional<Team> optionalTeam = teamRepository.getTeamByInviteCode(inviteCode);
        if (optionalTeam.isEmpty()) throw new InvitecodeException("Team with invite code %s not found".formatted(inviteCode), inviteCode);

        return OutputTeamDto.objToDto(optionalTeam.get());
    }

    public Team getTeamByTeamname(String teamName) {
        return teamRepository.getTeamsByName(teamName);
    }

    public void joinTeam(String username, String inviteCode) {
        Team team = OutputTeamDto.dtoToObj(getTeamByInviteCode(inviteCode));

        userService.updateUserTeam(username, team);
    }

    public void removeMember(String username) {
        userService.removeUserFromTeam(username);
    }

    public List<Team> getTopTenTeams() {
        return teamRepository.findTopTenTeams();
    }

    public void updateTeamNameAndInviteCode(String oldName, String newName) {
        String inviteCode = generateInviteCode(newName);
        checkInviteCode(inviteCode);

        Team team = getTeamByTeamname(oldName);
        team.setName(newName);
        team.setInviteCode(inviteCode);

        teamRepository.save(team);
    }
}
