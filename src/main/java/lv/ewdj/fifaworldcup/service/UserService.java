package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.InputUserDto;
import lv.ewdj.fifaworldcup.dto.OutputUserDto;
import lv.ewdj.fifaworldcup.exceptions.UserNotFoundException;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import org.springframework.stereotype.Service;

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

    public void updateUserTeams(String username, Team team, Team owningTeam) {
        User user = repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        user.setTeam(team);
        user.setOwningTeam(owningTeam);

        repository.save(user);
    }

    //TODO if delete user --> what to do with team

    // #### Helper Methods ####
}


