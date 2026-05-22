package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputTeamDto;
import lv.ewdj.fifaworldcup.dto.UserDto;
import lv.ewdj.fifaworldcup.exceptions.UserNotFoundException;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    public OutputTeamDto getTeamByUsername(String username){
        Optional<UserDto> optionaUser = userService.getUserByUsername(username);
        if (optionaUser.isEmpty()) {
            throw new UserNotFoundException(username);
        }
        Optional<Team> teamOptional = Optional.ofNullable(optionaUser.get().team());
        return teamOptional.map(OutputTeamDto::objToDto).orElse(null);
    }
}
