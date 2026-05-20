package lv.ewdj.fifaworldcup.service;

import lombok.RequiredArgsConstructor;
import lv.ewdj.fifaworldcup.dto.UserDTO;
import lv.ewdj.fifaworldcup.model.User;
import lv.ewdj.fifaworldcup.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    // ### Methods ###

    public List<UserDTO> getAllUsers() {
        return convertToDTOList(repository.findAll());
    }

    public List<UserDTO> getUsersByLastname (String lastname) {
        return convertToDTOList(repository.findByLastname(lastname));
    }

    public List<UserDTO> getUsersByFirstname (String firstname) {
        return convertToDTOList(repository.findByFirstname(firstname));
    }

//    public List<UserDTO> getUserByLastnameStartingWith(String str) {
//        return convertToDTOList(repository.findByLastnameStartingWith(str));
//    }
//    public List<UserDTO> getUserByLastnameStartingWith2(String str) {
//        return convertToDTOList(repository.findByLastnameStartingWith(str));
//    }

    // #### Helper Methods ####

    private List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream().map(u -> new UserDTO(u.getFirstname(), u.getLastname())).toList();
    }

}


