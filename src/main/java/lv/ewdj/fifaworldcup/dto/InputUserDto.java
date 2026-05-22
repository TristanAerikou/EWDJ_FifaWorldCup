package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Team;
import lv.ewdj.fifaworldcup.model.User;

public record InputUserDto(
        String username,
        String firstname,
        String lastname,
        Team owningTeam,
        Team team
) {

//    public static InputUserDto objToDto(User user) {
//        return new InputUserDto(
//                user.getUsername(),
//                user.getFirstname(),
//                user.getLastname(),
//                user.getOwningTeam(),
//                user.getTeam()
//        );
//    }

}
