package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.UserDto;
import lv.ewdj.fifaworldcup.exceptions.UserNotFoundException;
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

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(this::convertToDTO).toList();
    }

    public Optional<UserDto> getUserByUsername(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        return optionalUser.map(this::convertToDTO);
    }

    //TODO if delete user --> what to do with team

    // #### Helper Methods ####

    private UserDto convertToDTO(User u) {
        return new UserDto(
                u.getFirstname(),
                u.getLastname(),
                u.getOwningTeam(),
                u.getTeam()
        );
    }

}


