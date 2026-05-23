package lv.ewdj.fifaworldcup.dto;

import java.util.List;

public record OutputFullTeamDto (
        String teamName,
        String inviteCode,
        List<OutputUserDto> users,
        OutputUserDto owner,
        int score
) {
}
