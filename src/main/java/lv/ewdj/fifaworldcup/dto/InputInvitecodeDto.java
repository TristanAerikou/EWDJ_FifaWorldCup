package lv.ewdj.fifaworldcup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

public record InputInvitecodeDto(
        //TODO messages uit resourcebundle
        @NotBlank
        @NotNull
        @Pattern(regexp = ".*[a-zA-Z]*.*")
        @Size(min = 8, max = 8)
        String inviteCode
) {

}
