package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Team;

public record OutputTeamDto (
        String name,
        String inviteCode
) {
    public static OutputTeamDto objToDto(Team team) {
        return  new OutputTeamDto(team.getName(), team.getInviteCode());
    }
}
