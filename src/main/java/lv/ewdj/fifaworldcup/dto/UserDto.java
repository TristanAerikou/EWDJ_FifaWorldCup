package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Team;

public record UserDto(
        String firstname,
        String lastname,
        Team owningTeam,
        Team team
) {
}
