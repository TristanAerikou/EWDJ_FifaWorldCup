package lv.ewdj.fifaworldcup.dto;

import lv.ewdj.fifaworldcup.model.Team;

public record OutputTeamDto (
        int id,
        String name,
        String inviteCode
) {
    public static OutputTeamDto objToDto(Team team) {
        return  new OutputTeamDto(team.getId(), team.getName(), team.getInviteCode());
    }

    public static Team dtoToObj(OutputTeamDto teamDto) {
        return new Team(teamDto.id(), teamDto.name(), teamDto.inviteCode());
    }
}
