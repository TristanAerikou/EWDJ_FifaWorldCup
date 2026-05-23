package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Role;
import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;

public record OutputUserDto(
        long id,
        String username,
        String firstname,
        String lastname,
        Team team,
        Team owningTeam,
        Role role
) {

    public static OutputUserDto objToDto(User user) {
        return new OutputUserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getTeam(),
                user.getOwningTeam(),
                user.getRole()
        );
    }
}
