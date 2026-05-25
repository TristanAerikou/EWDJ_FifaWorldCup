package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InputChangeTeamNameAndCodeDto(
        @NotNull(message = "{noTeam.create.validation.name.empty}")
        @NotBlank(message = "{noTeam.create.validation.name.empty}")
        @Size(min = 3, max = 20, message = "{noTeam.create.validation.name.size}")
        String newTeamName
) {

}
