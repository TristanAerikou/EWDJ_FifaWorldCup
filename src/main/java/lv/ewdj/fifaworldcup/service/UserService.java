package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.OutputUserDto;
import lv.ewdj.fifaworldcup.exceptions.UserNotFoundException;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    // ### Methods ###

    public List<OutputUserDto> getAllUsers() {
        return repository.findAll().stream().map(OutputUserDto::objToDto).toList();
    }

    public Optional<OutputUserDto> getUserByUsername(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        return optionalUser.map(OutputUserDto::objToDto);
    }

    public void updateUserTeam(String username, Team team) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        user.setTeam(team);

        repository.save(user);
    }

    public void updateUserOwningTeam(String username, Team owningTeam) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        user.setOwningTeam(owningTeam);

        repository.save(user);
    }

    public void removeUserFromTeam(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        user.setTeam(null);
        user.setOwningTeam(null);

        repository.save(user);
    }

    public List<OutputUserDto> getUsersByTeamName(String teamName) {
        return repository.findUsersByTeamName(teamName).stream()
                .map(OutputUserDto::objToDto)
                .sorted(Comparator.comparing(OutputUserDto::points).reversed())
                .toList();
    }

    public OutputUserDto getUserOwningTeam(String teamName) {
        return OutputUserDto.objToDto(
                repository.findUserByOwningTeamName((teamName))
                        .orElseThrow(() -> new UserNotFoundException("No owner was found for this team. Does this team even exist?")
                        )
        );
    }

    //TODO if delete user --> what to do with team

    // #### Helper Methods ####
}


