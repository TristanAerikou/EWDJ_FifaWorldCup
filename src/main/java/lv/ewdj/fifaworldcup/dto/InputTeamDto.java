package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lv.ewdj.fifaworldcup.model.Team;

public record InputTeamDto(
        @NotNull(message = "{noTeam.create.validation.name.empty}")
        @NotBlank(message = "{noTeam.create.validation.name.empty}")
        @Size(min = 3, max = 20, message = "{noTeam.create.validation.name.size}")
        String name
) {
    public static Team dtoToObj(InputTeamDto dto, String inviteCode) {
        return new Team(
                dto.name(),
                inviteCode
        );
    }
}
